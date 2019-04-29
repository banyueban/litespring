package org.litespring.test.v5;

import java.lang.reflect.Method;

import org.junit.Test;
import org.litespring.service.v5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.cglib.proxy.NoOp;

public class CGLibTest {

	@Test
	public void testCallback() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(PetStoreService.class);
		enhancer.setCallback(new TransactionInterceptor());
		PetStoreService ps = (PetStoreService) enhancer.create();
		ps.placeOrder();
	}
	
	public static class TransactionInterceptor implements MethodInterceptor {
		
		public TransactionInterceptor() {}
		
		TransactionManager tx = new TransactionManager();
		@Override
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			tx.start();
			Object result = proxy.invokeSuper(obj, args);
			tx.commit();
			return result;
		}
		
	}
	
	@Test
	public void testFilter() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(PetStoreService.class);
		enhancer.setInterceptDuringConstruction(false);
		
		Callback[] callbacks = new Callback[] {new TransactionInterceptor(), NoOp.INSTANCE};
		Class<?>[] types = new Class<?>[callbacks.length];
		for (int i = 0; i < callbacks.length; i++) {
			types[i] = callbacks[i].getClass();
		}
		
		enhancer.setCallbackFilter(new ProxyCallbackFilter());
		enhancer.setCallbacks(callbacks);
		enhancer.setCallbackTypes(types);
		
		PetStoreService ps = (PetStoreService) enhancer.create();
		ps.placeOrder();
	}
	
	public static class ProxyCallbackFilter implements CallbackFilter {
		
		public ProxyCallbackFilter() {}
		
		// 返回值int表示Callback[]数组中拦截器(MethodInterceptor)的index
		@Override
		public int accept(Method method) {
			if (method.getName().startsWith("place")) {
				return 0;
			} 
			return 1;
		}
		
	}
}
