package org.litespring.beans.factory.config;

import org.litespring.beans.BeansException;

/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-04-12 15:57:12
 * hook(钩子)函数,在bean初始化前后操作bean
 */
public interface BeanPostProcessor {
	
	Object beforeInitialization(Object bean, String beanName) throws BeansException;
	
	Object afterInitialization(Object bean, String beanName) throws BeansException;
}
