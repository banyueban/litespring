package org.litespring.aop.framework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;

public class AopConfigSupport implements AopConfig {
	
	private Object targetObject;
	
	private List<Advice> advices = new ArrayList<Advice>();
	
	private List<Class> interfaces = new ArrayList<Class>();

	@Override
	public void addAdvice(Advice advice) {
		this.advices.add(advice);
	}

	@Override
	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}

	@Override
	public Object getTargetObject() {
		return this.targetObject;
	}

	@Override
	public List<Advice> getAdvices() {
		return this.advices;
	}

	@Override
	public List<Advice> getAdvices(Method method/*, Class<?> targetClass*/) {
		List<Advice> result = new ArrayList<Advice>();
		for (Advice advice : this.advices) {
			Pointcut pc = advice.getPointcut();
			if (pc.getMethodMatcher().matches(method)) {
				result.add(advice);
			}
		}
		return result;
	}

	@Override
	public Class<?> getTargetClass() {
		return this.targetObject.getClass();
	}

}
