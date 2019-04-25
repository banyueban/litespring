package org.litespring.aop.aspectj;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;

public class AspectJAfterThrowingAdvice implements Advice{
	
	private Method adviceMethod;
	
	private AspectJExpressionPointcut pointcut;
	
	private Object adviceObject;

	public AspectJAfterThrowingAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, Object adviceObject) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}

}
