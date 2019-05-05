package org.litespring.aop.framework;

import org.litespring.util.Assert;

public class CglibProxyFactory implements AopProxyFactory {

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
		// TODO Auto-generated method stub
		return null;
	}

	
}
