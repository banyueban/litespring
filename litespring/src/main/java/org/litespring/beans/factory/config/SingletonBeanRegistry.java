package org.litespring.beans.factory.config;

/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-03-26 15:46:46
 *  单例注册接口,目的是把单例对象单独放到一个map里
 */
public interface SingletonBeanRegistry {
	
	void registerSingleton(String beanName, Object singletonObject);
	
	Object getSingleton(String beanName);
	
}
