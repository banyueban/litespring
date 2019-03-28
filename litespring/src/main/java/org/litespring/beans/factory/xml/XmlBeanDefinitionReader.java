package org.litespring.beans.factory.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.core.io.Resource;
import org.litespring.util.StringUtils;

/**
 * @author MAQUN
 * 加载bean的定义,由原始的DefaultBeanFactory抽取而来
 *
 */
public class XmlBeanDefinitionReader {

	public static final String ID_ATTRIBUTE = "id";
	
	public static final String CLASS_ATTRIBUTE = "class";
	
	public static final String SCOPE_ATTRIBUTE = "scope";
	
	public static final String PROPERTY_ATTRIBUTE = "property";
	
	public static final String NAME_ATTRIBUTE = "name";
	
	public static final String VALUE_ATTRIBUTE = "value";
	
	public static final String REF_ATTRIBUTE = "ref";
	
	private BeanDefinitionRegistry registry;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
		this.registry = registry;
	}
	
	/**
	 * @User: MAQUN
	 * @Date: 2019-03-28 15:50:53
	 * 就是个读取.xml配置文件的方法,发文件内的bean配置信息读到BeanDefinition对象中
	 */
	public void loadBeanDefinitions(Resource resource) {
		InputStream is = null;
		try {
			// 定义了resource的不同实现来读取配置文件
//			ClassLoader cl = ClassUtils.getDefaultClassLoader();
//			is = cl.getResourceAsStream(configFile);
			is = resource.getInputStream();
			SAXReader reader = new SAXReader();
			Document doc = reader.read(is);
			Element root = doc.getRootElement(); // <beans>
			Iterator<Element> iter = root.elementIterator();
			while(iter.hasNext()) {
				Element ele = iter.next();
				String id = ele.attributeValue(ID_ATTRIBUTE);
				String beanClassName = ele.attributeValue(CLASS_ATTRIBUTE);
				BeanDefinition bd = new GenericBeanDefinition(id, beanClassName);
				if (null != ele.attribute(SCOPE_ATTRIBUTE)) {
					bd.setScope(ele.attributeValue(SCOPE_ATTRIBUTE));
				}
				parsePropertyElement(ele, bd);
				this.registry.registerBeanDefinition(id, bd);
			}
		} catch(Exception e) {
			throw new BeanDefinitionStoreException("IOException parsing XML document from " + resource.getDescription(), e);
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
	/**
	 * @User: MAQUN
	 * @Date: 2019-03-28 17:12:41
	 * 解析property标签
	 */
	private void parsePropertyElement(Element ele, BeanDefinition bd) {
		Iterator<Element> iter = ele.elementIterator(PROPERTY_ATTRIBUTE);
		while(iter.hasNext()) {
			Element propElem = iter.next();
			String propertyName = propElem.attributeValue(NAME_ATTRIBUTE);
			if (!StringUtils.hasLength(propertyName)) {
				logger.fatal("Tag 'property' must have a name attribute");
			}
			Object val = parsePropertyValue(propElem, bd, propertyName);
			PropertyValue pv = new PropertyValue(propertyName, val);
			bd.getPropertyValues().add(pv);
		}
	}
	
	/**
	 * @User: MAQUN
	 * @Date: 2019-03-28 17:12:58
	 * 将property标签中的属性ref或value解析为RuntimeBeanReference或TypedStringValue,记录property的值是引用类型还是字符串类型
	 */
	private Object parsePropertyValue(Element ele, BeanDefinition bd, String propertyName) {
		String elementName = null != propertyName ? 
				"<property> element for property " + propertyName + "'" :
				"<constructor-arg> element";
		boolean hasRefAttribute = null != ele.attribute(REF_ATTRIBUTE);
		boolean hasValueAttribute = null != ele.attribute(VALUE_ATTRIBUTE);
		if (hasRefAttribute) {
			String refName = ele.attributeValue(REF_ATTRIBUTE);
			if (!StringUtils.hasText(refName)) {
				logger.error(elementName + " containt empty 'ref' attribute");
			}
			RuntimeBeanReference ref = new RuntimeBeanReference(refName);
			return ref;
		} else if (hasValueAttribute) {
			TypedStringValue valueHolder = new TypedStringValue(ele.attributeValue(VALUE_ATTRIBUTE));
			return valueHolder;
		} else {
			throw new RuntimeException(elementName + " must specify ref or value");
		}
	}
}
