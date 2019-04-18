package org.litespring.beans.factory.support;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.Element;
//import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.NoSuchBeanDefinitionException;
import org.litespring.beans.factory.annotation.AutowiredAnnotationProcessor;
import org.litespring.beans.factory.config.BeanPostProcessor;
//import org.litespring.beans.factory.BeanDefinitionStoreException;
//import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.util.ClassUtils;

public class DefaultBeanFactory extends DefaultSingletonBeanRegistry implements BeanDefinitionRegistry, ConfigurableBeanFactory {
	
//	public static final String ID_ATTRIBUTE = "id";
//	
//	public static final String CLASS_ATTRIBUTE = "class";
	
	private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
	
	private ClassLoader beanClassLoader;
	
	private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();
	
	/*public DefaultBeanFactory(String configFile) {
		loadBeanDefinitions(configFile);
	}*/

	public DefaultBeanFactory() {
		
	}
	
	// 由于单一职责原则,方法由XmlBeanDefinitionReader实现
	/*
	private void loadBeanDefinitions(String configFile) {
		InputStream is = null;
		try {
			ClassLoader cl = ClassUtils.getDefaultClassLoader();
			is = cl.getResourceAsStream(configFile);
			SAXReader reader = new SAXReader();
			Document doc = reader.read(is);
			Element root = doc.getRootElement(); // <beans>
			Iterator<Element> it = root.elementIterator();
			while(it.hasNext()) {
				Element ele = it.next();
				String id = ele.attributeValue(ID_ATTRIBUTE);
				String beanClassName = ele.attributeValue(CLASS_ATTRIBUTE);
				BeanDefinition beanDefinition = new GenericBeanDefinition(id, beanClassName);
				this.beanDefinitionMap.put(beanID, beanDefinition);
				
			}
		} catch(DocumentException e) {
			throw new BeanDefinitionStoreException("IOException parsing XML " + configFile, e);
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	*/
	@Override
	public BeanDefinition getBeanDefinition(String beanID) {
		return this.beanDefinitionMap.get(beanID);
	}
	
	@Override
	public Object getBean(String beanID) {
		BeanDefinition bd = this.getBeanDefinition(beanID);
		if (null == bd) {
			throw new BeanCreationException("Bean Definition does not exist");
		}
		if (bd.isSingleton()) {
			Object bean = this.getSingleton(beanID);
			if (null == bean) {
				bean = createBean(bd);
				this.registerSingleton(beanID, bean);
			}
			return bean;
		}
		return createBean(bd);
	}
	
	/**
	 * @param bd
	 * @return
	 * 重构getbean方法,实现setter注入
	 */
	private Object createBean(BeanDefinition bd) {
		// 实例化bean
		Object bean = instantiateBean(bd);
		// 初始化bean
		populateBean(bd, bean);
		
		return bean;
	}
	
	/**
	 * @User: MAQUN
	 * @Date: 2019-03-26 16:03:15
	 * 对生成bean的方法做封装,用于处理singleton和prototype
	 * 初实例化bean
	 */
	private Object instantiateBean(BeanDefinition bd) {
		if (bd.hasConstructorArgumentValues()) {
			ConstructorResolver resolver = new ConstructorResolver(this);
			return resolver.autowireConstructor(bd);
		} else {
			ClassLoader cl = this.getBeanClassLoader();
			String beanClassName = bd.getBeanClassName();
			try {
				Class<?> clazz = cl.loadClass(beanClassName);
				return clazz.newInstance();
			} catch (Exception e) {
				throw new BeanCreationException("create bean for " + beanClassName +" fail");
			}
		}
	}
	/**
	 * @param bd
	 * @param bean
	 * 初始化bean,实现setter注入
	 */
	protected void populateBean(BeanDefinition bd, Object bean) {
		// 调用BeanPostProcessor实现bean注入
		for (BeanPostProcessor processor : this.beanPostProcessors) {
			if (processor instanceof AutowiredAnnotationProcessor) {
				((AutowiredAnnotationProcessor) processor).postProcessPropertyValues(bean, bd.getID());
			}
		}
		
		List<PropertyValue> pvs = bd.getPropertyValues();
		if (null == pvs || pvs.isEmpty()) {
			return;
		}
		BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this);
		SimpleTypeConverter converter = new SimpleTypeConverter();
		try {
			for (PropertyValue pv : pvs) {
				String propertyName = pv.getName();
				Object originalValue = pv.getValue();
				Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);
				//java.bean包下的方法,实现setter注入
				BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
				PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
				for (PropertyDescriptor pd : pds) {
					if (pd.getName().equals(propertyName)) {
						Object convertedValue = converter.convertIfNecessary(resolvedValue, pd.getPropertyType());
						// 增加了对string转int的转换器
//						pd.getWriteMethod().invoke(bean, resolvedValue);
						pd.getWriteMethod().invoke(bean, convertedValue);
						break;
					}
				}
			}
		} catch(Exception ex) {
			throw new BeanCreationException("Failed to obtain BeanInfo for class [" + bd.getBeanClassName() + "]", ex);
		}
	}
	
	@Override
	public void registerBeanDefinition(String beanID, BeanDefinition beanDefinition) {
		this.beanDefinitionMap.put(beanID, beanDefinition);
	}

	@Override
	public ClassLoader getBeanClassLoader() {
		return null != this.beanClassLoader ? this.beanClassLoader : ClassUtils.getDefaultClassLoader();
	}

	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}

	@Override
	public Object resolveDependency(DependencyDescriptor descriptor) {
		Class<?> typeToMatch = descriptor.getDependencyType();
		for (BeanDefinition bd : this.beanDefinitionMap.values()) {
			resolveBeanClass(bd);
			Class<?> beanClass = bd.getBeanClass();
			if (typeToMatch.isAssignableFrom(beanClass)) {
				return this.getBean(bd.getID());
			}
		}
		return null;
	}

	public void resolveBeanClass(BeanDefinition bd) {
		if (bd.hasBeanClass()) {
			return;
		} else {
			try {
				bd.resolveBeanClass(this.getBeanClassLoader());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException();
			}
		}
	}

	@Override
	public void addBeanPostProcessor(BeanPostProcessor beanProcessor) {
		this.beanPostProcessors.add(beanProcessor);
	}

	@Override
	public List<BeanPostProcessor> getBeanPostProcessor() {
		return this.beanPostProcessors;
	}

	@Override
	public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		BeanDefinition bd = this.getBeanDefinition(name);
		if (bd == null) {
			throw new NoSuchBeanDefinitionException(name);
		}
		resolveBeanClass(bd);
		return bd.getBeanClass();
	}

}
