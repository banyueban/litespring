package org.litespring.beans.factory.config;

import org.litespring.beans.BeansException;

/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-04-12 16:01:18
 * hook(钩子)函数,在bean实例化过程中操作bean
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {
	
	Object beforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;
	
	boolean afterInstantiation(Object bean, String beanName) throws BeansException;
	
	/*
	 * 属性操作
	 */
	void postProcessPropertyValues(Object bean, String beanName) throws BeansException;
}
