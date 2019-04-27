package org.litespring.test.v5;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Before;
import org.junit.Test;
import org.litespring.aop.aspectj.AspectJAfterReturningAdvice;
import org.litespring.aop.aspectj.AspectJAfterThrowingAdvice;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;
import org.litespring.aop.framework.ReflectiveMethodInvocation;
import org.litespring.service.v5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.litespring.util.MessageTracker;

public class ReflectiveMethodInvocationTest {

	private AspectJBeforeAdvice beforeAdvice;
	
	private AspectJAfterReturningAdvice afterReturningAdvice;
	
	private AspectJAfterThrowingAdvice afterThrowingAdvice;
	
	private PetStoreService petStore;
	
	private TransactionManager tx;
	
	@Before
	public void setUp() throws Exception {
		petStore = new PetStoreService();
		tx = new TransactionManager();
		MessageTracker.clearMsgs();
		beforeAdvice = new AspectJBeforeAdvice(TransactionManager.class.getMethod("start"), null, tx);
		afterReturningAdvice = new AspectJAfterReturningAdvice(TransactionManager.class.getMethod("commit"), null, tx);
		afterThrowingAdvice = new AspectJAfterThrowingAdvice(TransactionManager.class.getMethod("rollback"), null, tx);
	}
	
	@Test
	public void testMethodInvocation() throws Throwable {
		Method targetMethod = PetStoreService.class.getMethod("placeOrder");
		List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
		interceptors.add(beforeAdvice);
		interceptors.add(afterReturningAdvice);
		
		ReflectiveMethodInvocation methodInvocation = new ReflectiveMethodInvocation(petStore, targetMethod, new Object[0], interceptors);
		
		methodInvocation.proceed();
		
		List<String> msgs = MessageTracker.getMsgs();
		assertEquals(3, msgs.size());
		assertEquals("start tx", msgs.get(0));
		assertEquals("place order", msgs.get(1));
		assertEquals("commit tx", msgs.get(2));
	}
	
	@Test
	public void testMethodInvocation2() throws Throwable {
		Method targetMethod = PetStoreService.class.getMethod("placeOrder");
		List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
		interceptors.add(afterReturningAdvice);
		interceptors.add(beforeAdvice);
		
		ReflectiveMethodInvocation methodInvocation = new ReflectiveMethodInvocation(petStore, targetMethod, new Object[0], interceptors);
		
		methodInvocation.proceed();
		
		List<String> msgs = MessageTracker.getMsgs();
		assertEquals(3, msgs.size());
		assertEquals("start tx", msgs.get(0));
		assertEquals("place order", msgs.get(1));
		assertEquals("commit tx", msgs.get(2));
	}
	
	@Test
	public void testMethodInvocation3() throws Throwable {
		Method targetMethod = PetStoreService.class.getMethod("placeOrderWithException");
		List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
		interceptors.add(beforeAdvice);
		interceptors.add(afterThrowingAdvice);
		interceptors.add(afterReturningAdvice);
		
		ReflectiveMethodInvocation methodInvocation = new ReflectiveMethodInvocation(petStore, targetMethod, new Object[0], interceptors);
		try {
			methodInvocation.proceed();
		} catch(Throwable ex) {
			List<String> msgs = MessageTracker.getMsgs();
			assertEquals(2, msgs.size());
			assertEquals("start tx", msgs.get(0));
			assertEquals("rollback tx", msgs.get(1));
		}
		
	}
}
