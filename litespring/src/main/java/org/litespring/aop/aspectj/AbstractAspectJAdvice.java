package org.litespring.aop.aspectj;

import java.lang.reflect.Method;

import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;

public abstract class AbstractAspectJAdvice implements Advice {
	
	protected Method adviceMethod;
	
	protected Object adviceObject;
	
	protected AspectJExpressionPointcut pointcut;

	public AbstractAspectJAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, Object adviceObject) {
		this.adviceMethod = adviceMethod;
		this.pointcut = pointcut;
		this.adviceObject = adviceObject;
	}
	
	public void invokeAdviceMethod() throws Throwable {
		this.adviceMethod.invoke(adviceObject);
	}
	
	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}

	public Method getAdviceMethod() {
		return this.adviceMethod;
	}
}
