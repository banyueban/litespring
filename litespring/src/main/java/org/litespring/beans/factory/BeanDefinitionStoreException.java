package org.litespring.beans.factory;

import org.litespring.beans.BeansException;

/**
 * @author Charis
 * bean定义时的异常
 */
public class BeanDefinitionStoreException extends BeansException {

	public BeanDefinitionStoreException(String message, Throwable cause) {
		super(message, cause);
	}

	
}
