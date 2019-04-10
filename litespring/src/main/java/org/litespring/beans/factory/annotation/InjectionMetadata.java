package org.litespring.beans.factory.annotation;

import java.util.LinkedList;

/**
 * @author Charis
 * 操作依赖注入
 */
public class InjectionMetadata {

	private final Class<?> targetClass;
	
	private LinkedList<InjectionElement> injectionElements;
	
	public InjectionMetadata(Class<?> targetClass, LinkedList<InjectionElement> injectionElements) {
		this.targetClass = targetClass;
		this.injectionElements = injectionElements;
	}

	public LinkedList<InjectionElement> getInjectionElement() {
		return this.injectionElements;
	}
	
	public void inject(Object target) {
		if (injectionElements == null || injectionElements.isEmpty()) {
			return;
		}
		for (InjectionElement ele : injectionElements) {
			ele.inject(target);
		}
	}

}
