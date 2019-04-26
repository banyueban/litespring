package org.litespring.aop.aspectj;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;

public class AspectJBeforeAdvice implements Advice{
	
	private Method adviceMethod;
	
	private AspectJExpressionPointcut pointcut;
	
	private Object adviceObject;

	public AspectJBeforeAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, Object adviceObject) {
		this.adviceMethod = adviceMethod;
		this.pointcut = pointcut;
		this.adviceObject = adviceObject;
	}
	
	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		this.adviceMethod.invoke(this.adviceObject);
		return invocation.proceed();
	}


}
