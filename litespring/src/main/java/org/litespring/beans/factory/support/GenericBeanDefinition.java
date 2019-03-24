package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;

public class GenericBeanDefinition implements BeanDefinition {
	
	private String id;
	
	private String beanClassName;
	
	public GenericBeanDefinition(String id, String beanClassName) {
		this.id = id;
		this.beanClassName = beanClassName;
	}
	
	public String getId() {
		return id;
	}

	public String getBeanClassName() {
		return beanClassName;
	}

}
