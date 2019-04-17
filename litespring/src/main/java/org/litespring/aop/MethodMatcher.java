package org.litespring.aop;

import java.lang.reflect.Method;

public interface MethodMatcher {

	boolean matches(Method method1/*, Class<?> targetClass*/);
	
}
