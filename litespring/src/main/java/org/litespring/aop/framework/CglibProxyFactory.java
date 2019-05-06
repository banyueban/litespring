package org.litespring.aop.framework;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.util.Assert;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.cglib.proxy.Enhancer;

public class CglibProxyFactory implements AopProxyFactory {

	protected static final Log logger = LogFactory.getLog(CglibProxyFactory.class);
	
	private static final int AOP_PROXY = 0;
	
	protected final AopConfig config;
	
	public CglibProxyFactory(AopConfig config) {
		Assert.notNull(config, "AdvisedSupport must not be null");
		if (config.getAdvices().size() == 0) {
			throw new AopConfigException("No advisors and no targetSource specified");
		}
		this.config = config;
	}

	@Override
	public Object getProxy() {
		return getProxy(null);
	}

	@Override
	public Object getProxy(ClassLoader classLoader) {
		if (logger.isDebugEnabled()) {
			logger.debug("Creating CGLIB proxy: target source is " + this.config.getTargetClass());
		}
		
		Enhancer enhancer = new Enhancer();
		Class<?> rootClass = this.config.getTargetClass();
		if (classLoader != null) {
			enhancer.setClassLoader(classLoader);
		}
		enhancer.setSuperclass(rootClass);
		enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE); // enhance BySpringCGLIB
		enhancer.setInterceptDuringConstruction(false);
		
		Callback[] callbacks = getCallbacks(rootClass);
		Class<?>[] types = new Class<?>[callbacks.length];
		for (int x = 0; x < types.length; x++) {
			types[x] = callbacks[x].getClass();
		}
		enhancer.setCallbackFilter(new ProxyCallbackFilter(this.config));
		enhancer.setCallbackTypes(types);
		enhancer.setCallbacks(callbacks);
		
		Object proxy = enhancer.create();
		
		return proxy;
	}

	private Callback[] getCallbacks(Class<?> rootClass) {
		
		return null;
	}
	
	/**
	 * CallbackFilter to assign Callbacks to methods.
	 */
	public static class ProxyCallbackFilter implements CallbackFilter {
		
		private final AopConfig config;
		
		public ProxyCallbackFilter(AopConfig advised) {
			this.config = advised;
		}
		
		@Override
		public int accept(Method method) {
			return AOP_PROXY;
		}

	}
}
