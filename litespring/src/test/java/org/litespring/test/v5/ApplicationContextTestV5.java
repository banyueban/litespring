package org.litespring.test.v5;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.service.v5.PetStoreService;
import org.litespring.util.MessageTracker;

public class ApplicationContextTestV5 {
	
	@Before
	public void setUp() {
		MessageTracker.clearMsgs();
	}
	
	@Test
	public void testPlaceOrder() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v5.xml");
		PetStoreService petStore = (PetStoreService) ctx.getBean("petStore");
		
		petStore.placeOrder();
		
		assertEquals(3, MessageTracker.getMsgs().size());
		assertEquals("start tx", MessageTracker.getMsgs().get(0));
		assertEquals("place order", MessageTracker.getMsgs().get(1));
		assertEquals("commit tx", MessageTracker.getMsgs().get(2));
	}
}
