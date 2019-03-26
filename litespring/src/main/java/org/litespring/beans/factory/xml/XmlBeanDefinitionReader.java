package org.litespring.beans.factory.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.core.io.Resource;
import org.litespring.util.ClassUtils;

/**
 * @author MAQUN
 * 加载bean的定义,由原始的DefaultBeanFactory抽取而来
 *
 */
public class XmlBeanDefinitionReader {

	public static final String ID_ATTRIBUTE = "id";
	
	public static final String CLASS_ATTRIBUTE = "class";
	
	public static final String SCOPE_ATTRIBUTE = "scope";
	
	private BeanDefinitionRegistry registry;
	
	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
		this.registry = registry;
	}
	
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
			Iterator<Element> it = root.elementIterator();
			while(it.hasNext()) {
				Element ele = it.next();
				String id = ele.attributeValue(ID_ATTRIBUTE);
				String beanClassName = ele.attributeValue(CLASS_ATTRIBUTE);
				BeanDefinition beanDefinition = new GenericBeanDefinition(id, beanClassName);
				if (null != ele.attribute(SCOPE_ATTRIBUTE)) {
					beanDefinition.setScope(ele.attributeValue(SCOPE_ATTRIBUTE));
				}
				this.registry.registerBeanDefinition(id, beanDefinition);
				
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
}
