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
import org.litespring.util.ClassUtils;

public class XmlBeanDefinitionReader {

	public static final String ID_ATTRIBUTE = "id";
	
	public static final String CLASS_ATTRIBUTE = "class";
	
	private BeanDefinitionRegistry registry;
	
	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
		this.registry = registry;
	}
	
	public void loadBeanDefinitions(String configFile) {
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
				this.registry.registerBeanDefinition(id, beanDefinition);
				
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
}
