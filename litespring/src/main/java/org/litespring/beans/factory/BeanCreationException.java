package org.litespring.beans.factory;

import org.litespring.beans.BeansException;

/**
 * @author MAQUN
 * 创建bean的异常
 */
public class BeanCreationException extends BeansException {
	
	private String beanName;
	
	public BeanCreationException(String msg) {
		super(msg);
	}
	
	public BeanCreationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public BeanCreationException(String beanName, String msg) {
		super("Error creating bean with name '" + beanName + "': " + msg);
		this.beanName = beanName;
	}
	
	public BeanCreationException(String beanName, String msg, Throwable cause) {
		this(beanName, msg);
		initCause(cause);
	}

	public String getBeanName() {
		return beanName;
	}

}
