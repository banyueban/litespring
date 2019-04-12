package org.litespring.beans.factory.config;

import java.util.List;

/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-03-26 14:11:09
 * 对DefaultBeanFactory和ApplicationContext获取ClassLoader做了优化,使classLoader在二者之间可配置
 */
public interface ConfigurableBeanFactory extends AutowireCapableBeanFactory {
	
	ClassLoader getBeanClassLoader();
	
	void setBeanClassLoader(ClassLoader beanClassLoader);

	void addBeanPostProcessor(BeanPostProcessor beanProcessor);
	
	List<BeanPostProcessor> getBeanPostProcessor();
}
