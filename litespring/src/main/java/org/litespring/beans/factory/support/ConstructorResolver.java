package org.litespring.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;

/**
 * @author Charis
 * 构造器注入的解析器
 */
public class ConstructorResolver {
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private final ConfigurableBeanFactory beanFactory;
	
	public ConstructorResolver(ConfigurableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public Object autowireConstructor(final BeanDefinition bd) {
		Constructor<?> constructorToUse = null;
		Object[] argsToUse = null;
		Class<?> beanClass = null;
		try {
			beanClass = this.beanFactory.getBeanClassLoader().loadClass(bd.getBeanClassName());
		} catch (ClassNotFoundException e) {
			throw new BeanCreationException(bd.getID(), "Instantiation of bean failed, can't resolve class", e);
		}
		
		Constructor<?>[] candidates = beanClass.getConstructors();
		
		BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this.beanFactory);
		ConstructorArgument cargs = bd.getConstructorArgument();
		SimpleTypeConverter typeConverter = new SimpleTypeConverter();
		
		for (int i = 0; i < candidates.length; i++) {
			Class<?>[] parameterTypes = candidates[i].getParameterTypes();
			// 判断构造器的参数长度
			if (parameterTypes.length != cargs.getArgumentCount()) {
				continue;
			}
			argsToUse = new Object[parameterTypes.length];
			// 判断构造器的参数类型是否和标签中一致
			boolean result = this.valuesMatchTypes(parameterTypes, cargs.getArgumentValues(), argsToUse, valueResolver, typeConverter);
			if (result) {
				constructorToUse = candidates[i];
				break;
			}
		}
		
		// 找不到合适的构造器
		if (null == constructorToUse) {
			throw new BeanCreationException(bd.getID(), "can't find a appropriate constructor");
		}
		try {
			return constructorToUse.newInstance(argsToUse);
		} catch (Exception e) {
			throw new BeanCreationException(bd.getID(), "can't find a create instance using" + constructorToUse);
		}
	}

	/*
	 * 判断构造器的参数类型和标签中的是否一致
	 */
	private boolean valuesMatchTypes(Class<?>[] parameterTypes, List<ConstructorArgument.ValueHolder> valueHolders, Object[] argsToUse,
			BeanDefinitionValueResolver valueResolver, SimpleTypeConverter typeConverter) {
		for (int i = 0; i < parameterTypes.length; i++) {
			ConstructorArgument.ValueHolder valueHolder = valueHolders.get(i);
			//<constructor-arg>标签中ref的原始类型,可能是RuntimeBeanReference/TypedStringValue
			Object originalValue = valueHolder.getValue();
			
			try {
				// 转换成真正的bean实例/String类型
				Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);
				// 类型转换,String转int或boolean,如果转型失败,抛出异常,说明构造器中的类型和<constructor-arg>标签中的不一致
				Object convertedValue = typeConverter.convertIfNecessary(resolvedValue, parameterTypes[i]);
				argsToUse[i] = convertedValue;
			} catch(Exception e) {
				logger.error(e);
				return false;
			}
		}
		return true;
	}

}
