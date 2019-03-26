package org.litespring.context.support;

import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.context.ApplicationContext;
import org.litespring.core.io.Resource;
import org.litespring.util.ClassUtils;
/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-03-26 09:42:37
 * AbstractApplicationContext抽取了FileSystemXmlApplicationContext和ClassPathXmlApplicationContext的共同
 * 构造方法和getBean()方法,通过模版方法优化了代码设计
 */
public abstract class AbstractApplicationContext implements ApplicationContext{
	
	private DefaultBeanFactory factory = null;
	
	private ClassLoader beanClassLoader;
	
	public AbstractApplicationContext(String configFile) {
		this.factory = new DefaultBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this.factory);
		Resource resource = this.getResourceByPath(configFile);
		reader.loadBeanDefinitions(resource);
		factory.setBeanClassLoader(this.getBeanClassLoader());
	}
	
	@Override
	public Object getBean(String beanID) {
		return this.factory.getBean(beanID);
	}
	
	@Override
	public ClassLoader getBeanClassLoader() {
		return null != this.beanClassLoader ? this.beanClassLoader : ClassUtils.getDefaultClassLoader();
	}

	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}

	/*
	 * 抽取单独的模版方法由子类各自实现
	 */
	protected abstract Resource getResourceByPath(String path);
		
}
