package org.litespring.beans.factory.support;

import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;

/**
 * @author Charis
 * property标签属性的解释器,将对应的RuntimeBeanReference或TypedStringValue转化为相应的对象
 */
public class BeanDefinitionValueResolver {
	
	private final BeanFactory beanFactory;
	
	public BeanDefinitionValueResolver(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	
	public Object resolveValueIfNecessary(Object value) {
		if (value instanceof RuntimeBeanReference) {
			RuntimeBeanReference ref = (RuntimeBeanReference) value;
			String beanName = ref.getBeanName();
			return this.beanFactory.getBean(beanName);
		} else if (value instanceof TypedStringValue) {
			return ((TypedStringValue) value).getValue();
		} else {
			// TODO 其它类型支持
			throw new RuntimeException("the value " + value + " has not implemented");
		}
	}
}
