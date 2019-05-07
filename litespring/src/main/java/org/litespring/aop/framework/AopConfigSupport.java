package org.litespring.aop.framework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;
import org.litespring.util.Assert;

public class AopConfigSupport implements AopConfig {
	
	private boolean proxyTargetClass = false;
	
	private Object targetObject;
	
	private List<Advice> advices = new ArrayList<Advice>();
	
	private List<Class> interfaces = new ArrayList<Class>();
	
	public AopConfigSupport() {
		
	}
	
	@Override
	public void addAdvice(Advice advice) {
		this.advices.add(advice);
	}
	
	public void addInterface(Class<?> intf) {
		Assert.notNull(intf, "Interface must not be null");
		if (!intf.isInterface()) {
			throw new IllegalArgumentException("[" + intf.getName() + "] is not an interface");
		}
		if (!this.interfaces.contains(intf)) {
			this.interfaces.add(intf);
			
		}
	}
	
	/**
	 * Remove a proxied interface.
	 * <p>Does nothing if the given interface isn't proxied.
	 * @param intf the interface to remove from the proxy
	 * @return {@code true} if the interface was removed; {@code false}
	 * if the interface was not found and hence could not be removed
	 */
	/*public boolean removeInterface(Class<?> intf) {
		return this.interfaces.remove(intf);
	}*/
	
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

	@Override
	public boolean isProxyTargetClass() {
		return proxyTargetClass;
	}

	@Override
	public Class<?>[] getProxiedInterfaces() {
		return this.interfaces.toArray(new Class[this.interfaces.size()]);
	}

	@Override
	public boolean isInterfaceProxied(Class<?> intf) {
		for (Class proxyIntf : this.interfaces) {
			if (intf.isAssignableFrom(proxyIntf)) {
				return true;
			}
		}
		return false;
	}
	
	public void setProxyTargetClass(boolean proxyTargetClass) {
		this.proxyTargetClass = proxyTargetClass;
	}
	
}
