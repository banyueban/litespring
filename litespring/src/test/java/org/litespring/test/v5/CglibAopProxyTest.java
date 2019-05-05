package org.litespring.test.v5;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.litespring.aop.aspectj.AspectJAfterReturningAdvice;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;
import org.litespring.aop.aspectj.AspectJExpressionPointcut;
import org.litespring.aop.framework.AopConfig;
import org.litespring.aop.framework.AopConfigSupport;
import org.litespring.aop.framework.CglibProxyFactory;
import org.litespring.service.v5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.litespring.util.MessageTracker;

public class CglibAopProxyTest {

	private static AspectJBeforeAdvice beforeAdvice;
	private static AspectJAfterReturningAdvice afterReturningAdvice;
	private static AspectJExpressionPointcut pc;
	
	private TransactionManager tx;
	
	@Before
	public void setUp() throws Exception {
		MessageTracker.clearMsgs();
		
		String expression = "execution (* org.litespring.service.v5.*.placeOrder(..))";
		tx = new TransactionManager();
		pc = new AspectJExpressionPointcut();
		pc.setExpression(expression);
		
		beforeAdvice = new AspectJBeforeAdvice(TransactionManager.class.getMethod("start"), pc, tx);
		afterReturningAdvice = new AspectJAfterReturningAdvice(TransactionManager.class.getMethod("commit"), pc, tx);
	}
	
	@Test
	public void testGetProxy() {
		AopConfig config = new AopConfigSupport();
		config.addAdvice(beforeAdvice);
		config.addAdvice(afterReturningAdvice);
		config.setTargetObject(new PetStoreService());
		
		CglibProxyFactory proxy = new CglibProxyFactory(config);
		
		PetStoreService ps = (PetStoreService) proxy.getProxy();
		ps.placeOrder();
		
		List<String> msgs = MessageTracker.getMsgs();
		assertEquals(3, msgs.size());
		assertEquals("start tx", msgs.get(0));
		assertEquals("place order", msgs.get(1));
		assertEquals("commit tx", msgs.get(2));
	}
}
