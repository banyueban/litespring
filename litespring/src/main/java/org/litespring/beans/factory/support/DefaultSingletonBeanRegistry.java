package org.litespring.beans.factory.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.litespring.beans.factory.config.SingletonBeanRegistry;
import org.litespring.util.Assert;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
	
	private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>();
	
	@Override
	public void registerSingleton(String beanName, Object singletonObject) {

		Assert.notNull(beanName, "'beanName' must be not null");
		
		Object oldObject = this.singletonObjects.get(beanName);
		if (null != oldObject) {
			throw new IllegalStateException("Could not regiser object [" + singletonObject
					+ "] under beanName '" + beanName + "' : this is already object [" + oldObject + "] bound");
		}
		this.singletonObjects.put(beanName, singletonObject);
	}

	@Override
	public Object getSingleton(String beanName) {
		return this.singletonObjects.get(beanName);
	}

}
