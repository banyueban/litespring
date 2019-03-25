package org.litespring.context.support;

import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.context.ApplicationContext;
import org.litespring.core.io.Resource;

public abstract class AbstractApplicationContext implements ApplicationContext{
	private DefaultBeanFactory factory = null;
	
	public AbstractApplicationContext(String configFile) {
		this.factory = new DefaultBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this.factory);
		Resource resource = this.getResourceByPath(configFile);
		reader.loadBeanDefinitions(resource);
	}
	
	@Override
	public Object getBean(String beanID) {
		return this.factory.getBean(beanID);
	}
	
	/*
	 * 抽取单独的模版方法由子类各自实现
	 */
	protected abstract Resource getResourceByPath(String path);
		
}
