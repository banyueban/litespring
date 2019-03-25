package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-03-25 14:58:16
 * 为了接口单一原则以及不把获取bean定义和注册bean的方法暴露给使用者,因此将两个方法单独抽取出一个接口,DefaultBeanFactory类来实现
 */
public interface BeanDefinitionRegistry {
	
	BeanDefinition getBeanDefinition(String beanID);
	
	void registerBeanDefinition(String beanID, BeanDefinition beanDefinition);
	
}
