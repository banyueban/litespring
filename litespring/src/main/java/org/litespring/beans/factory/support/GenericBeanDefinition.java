package org.litespring.beans.factory.support;

import java.util.ArrayList;
import java.util.List;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.PropertyValue;

/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-03-26 15:27:50
 * bean定义的类
 */
public class GenericBeanDefinition implements BeanDefinition {
	
	private String id;
	
	private String beanClassName;
	
	private boolean singleton = true;
	
	private boolean prototype = false;
	
	private String scope = SCOPE_DEFAULT;

	private List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
	
	private ConstructorArgument constructorArgument = new ConstructorArgument();
	
	private Class<?> beanClass;
	
	public GenericBeanDefinition(String id, String beanClassName) {
		this.id = id;
		this.beanClassName = beanClassName;
	}
	
	public GenericBeanDefinition () {
		
	}
	
	@Override
	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String className) {
		this.beanClassName = className;
	}
	
	@Override
	public boolean isSingleton() {
		return this.singleton;
	}

	@Override
	public boolean isPrototype() {
		return this.prototype;
	}

	@Override
	public String getScope() {
		return this.scope;
	}

	@Override
	public void setScope(String scope) {
		this.scope = scope;
		this.singleton = SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
		this.prototype = SCOPE_PROTOTYPE.equals(scope);
	}

	@Override
	public List<PropertyValue> getPropertyValues() {
		return this.propertyValues;
	}

	@Override
	public ConstructorArgument getConstructorArgument() {
		return this.constructorArgument;
	}

	@Override
	public String getID() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public boolean hasConstructorArgumentValues() {
		return !this.constructorArgument.isEmpty();
	}

	@Override
	public Class<?> getBeanClass() throws IllegalStateException {
		if (this.beanClass == null) {
			throw new IllegalStateException("BeanClassName [" + this.getBeanClassName() + "] has not been resolved into an actual Class");
		}
		return this.beanClass;
	}

	@Override
	public boolean hasBeanClass() {
		return this.beanClass != null;
	}

	@Override
	public Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException {
		String className = getBeanClassName();
		if (null == className) {
			return null;
		}
		Class<?> resolvedClass = classLoader.loadClass(className);
		this.beanClass = resolvedClass;
		return resolvedClass;
	}

}
