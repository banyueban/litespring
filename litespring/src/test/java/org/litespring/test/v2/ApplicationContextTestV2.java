package org.litespring.test.v2;

import static org.junit.Assert.*;

import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.dao.v2.AccountDao;
import org.litespring.dao.v2.ItemDao;
import org.litespring.service.v2.PetStoreService;

public class ApplicationContextTestV2 {
	
	@Test
	public void testGetBeanProperty() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v2.xml");
		PetStoreService ps = (PetStoreService) ctx.getBean("petStore");
		assertNotNull(ps.getAccountDao());
		assertNotNull(ps.getItemDao());
		assertTrue(ps.getAccountDao() instanceof AccountDao);
		assertTrue(ps.getItemDao() instanceof ItemDao);
		assertEquals("maqun", ps.getOwer());
	}
}
