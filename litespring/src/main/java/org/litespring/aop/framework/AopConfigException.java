package org.litespring.aop.framework;

@SuppressWarnings("serial")
public class AopConfigException extends RuntimeException {

	public AopConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public AopConfigException(String message) {
		super(message);
	}
	
}
