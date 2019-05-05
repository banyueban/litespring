package org.litespring.aop.framework;

import java.lang.reflect.Method;
import java.util.List;

import org.litespring.aop.Advice;

public interface AopConfig {
	
	void addAdvice(Advice advice);

	void setTargetObject(Object obj);
	
	Object getTargetObject();
	
	List<Advice> getAdvices();
	
	List<Advice> getAdvices(Method method/*, Class<?> targetClass*/);
	
	Class<?> getTargetClass();
}
