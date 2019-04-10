package org.litespring.beans.factory.annotation;

import java.lang.reflect.Member;

import org.litespring.beans.factory.config.AutowireCapableBeanFactory;

/**
 * @author Charis
 * 需要注入的属性抽象类
 */
public abstract class InjectionElement {
	
	protected Member member;
	
	protected AutowireCapableBeanFactory factory;

	/**
	 * @param member
	 * @param factory
	 */
	public InjectionElement(Member member, AutowireCapableBeanFactory factory) {
		this.member = member;
		this.factory = factory;
	}

	public abstract void inject(Object target);
}
