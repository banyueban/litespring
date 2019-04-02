package org.litespring.test.v3;

import static org.junit.Assert.*;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.support.ConstructorResolver;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.dao.v3.AccountDao;
import org.litespring.dao.v3.ItemDao;
import org.litespring.service.v3.PetStoreService;

public class ConstructorResolverTest {
	
	@Test
	public void testAutowireConstructor() {
		DefaultBeanFactory factory = new DefaultBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		reader.loadBeanDefinitions(new ClassPathResource("petstore-v3.xml"));
		
		BeanDefinition bd = factory.getBeanDefinition("petStore");
		ConstructorResolver resolver = new ConstructorResolver(factory);
		PetStoreService ps = (PetStoreService)resolver.autowireConstructor(bd);
		
		assertNotNull(ps.getAccountDao());
		assertTrue(ps.getAccountDao() instanceof AccountDao);
		assertNotNull(ps.getItemDao());
		assertTrue(ps.getItemDao() instanceof ItemDao);
		assertEquals(1, ps.getVersion());
	}
}
