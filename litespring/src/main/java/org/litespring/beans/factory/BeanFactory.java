package org.litespring.beans.factory;

public interface BeanFactory {

	// 将次方法抽离到BeanDefinitionRegistry接口
//	BeanDefinition getBeanDefinition(String beanID);

	Object getBean(String beanID);

	Class<?> getType(String name) throws NoSuchBeanDefinitionException;
}
