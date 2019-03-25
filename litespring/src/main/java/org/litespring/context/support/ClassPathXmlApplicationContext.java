package org.litespring.context.support;

import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.context.ApplicationContext;

/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-03-25 18:00:17
 * 对BeanFactory和XmlBeanDefinitionReader做了包装
 */
public class ClassPathXmlApplicationContext implements ApplicationContext {

	private DefaultBeanFactory factory = null;
	
	public ClassPathXmlApplicationContext(String configFile) {
		this.factory = new DefaultBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		reader.loadBeanDefinitions(configFile);
	}

	public Object getBean(String beanID) {
		return factory.getBean(beanID);
	}

}
