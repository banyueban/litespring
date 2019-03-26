package org.litespring.beans.factory.support;

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
import org.litespring.beans.factory.BeanCreationException;
//import org.litespring.beans.factory.BeanDefinitionStoreException;
//import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.util.ClassUtils;

public class DefaultBeanFactory extends DefaultSingletonBeanRegistry implements BeanDefinitionRegistry, ConfigurableBeanFactory {
	
//	public static final String ID_ATTRIBUTE = "id";
//	
//	public static final String CLASS_ATTRIBUTE = "class";
	
	private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
	
	private ClassLoader beanClassLoader;
	
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
	 * @User: MAQUN
	 * @Date: 2019-03-26 16:03:15
	 * 对生成bean的方法做封装,用于处理singleton和prototype
	 */
	private Object createBean(BeanDefinition bd) {
		ClassLoader cl = this.getBeanClassLoader();
		String beanClassName = bd.getBeanClassName();
		try {
			Class<?> clazz = cl.loadClass(beanClassName);
			return clazz.newInstance();
		} catch (Exception e) {
			throw new BeanCreationException("create bean for " + beanClassName +" fail");
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

}
