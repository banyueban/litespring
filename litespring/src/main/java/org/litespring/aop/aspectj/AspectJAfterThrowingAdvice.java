package org.litespring.aop.aspectj;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;

/**
 * @author Charis
 * 模版方法优化
 */
public class AspectJAfterThrowingAdvice /*implements Advice*/extends AbstractAspectJAdvice {
	
	/*private Method adviceMethod;
	
	private AspectJExpressionPointcut pointcut;
	
	private Object adviceObject;*/

	public AspectJAfterThrowingAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, Object adviceObject) {
		/*this.adviceMethod = adviceMethod;
		this.pointcut = pointcut;
		this.adviceObject = adviceObject;*/
		super(adviceMethod, pointcut, adviceObject);
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {
			return invocation.proceed();
		} catch(Throwable t) {
			/*this.adviceMethod.invoke(this.adviceObject);*/
			this.invokeAdviceMethod();
			throw t;
		}
	}
/*
	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}
*/
}
