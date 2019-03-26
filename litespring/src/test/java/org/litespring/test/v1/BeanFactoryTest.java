package org.litespring.test.v1;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.service.v1.PetStoreService;
import org.litespring.service.v1.PetStoreService1;

public class BeanFactoryTest {
	
	DefaultBeanFactory factory = null;
	XmlBeanDefinitionReader reader = null;
	
	@Before
	public void setUp() {
		factory = new DefaultBeanFactory();
		reader = new XmlBeanDefinitionReader(factory);
	}
	
	@Test
	public void testGetBean() {
		reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));
		BeanDefinition bd = factory.getBeanDefinition("petStore");
		assertTrue(bd.isSingleton());
		assertFalse(bd.isPrototype());
		assertEquals(BeanDefinition.SCOPE_DEFAULT, bd.getScope());
		assertEquals("org.litespring.service.v1.PetStoreService", bd.getBeanClassName());
		PetStoreService petStore = (PetStoreService)factory.getBean("petStore");
		assertNotNull(petStore);
		PetStoreService petStore1 = (PetStoreService)factory.getBean("petStore");
		assertEquals(petStore, petStore1);
	}
	
	@Test
	public void testGetBeanPrototype() {
		reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));
		BeanDefinition bd = factory.getBeanDefinition("petStore1");
		assertFalse(bd.isSingleton());
		assertTrue(bd.isPrototype());
		assertEquals(BeanDefinition.SCOPE_PROTOTYPE, bd.getScope());
		assertEquals("org.litespring.service.v1.PetStoreService1", bd.getBeanClassName());
		PetStoreService1 petStore = (PetStoreService1)factory.getBean("petStore1");
		assertNotNull(petStore);
		PetStoreService1 petStore1 = (PetStoreService1)factory.getBean("petStore1");
		assertNotEquals(petStore, petStore1);
	}
	
	@Test
	public void testInvalidBean() {
//		BeanFactory factory = new DefaultBeanFactory("petstore-v1.xml");
		try {
			factory.getBean("invalidBean");
		} catch(BeanCreationException e) {
			return;
		}
		Assert.fail("expect BeanCreationException");		
	}
	
	@Test
	public void testInvalidXML() {
		try {
			reader.loadBeanDefinitions(new ClassPathResource("xxx.xml"));
		} catch(BeanDefinitionStoreException e) {
			return;
		}
		Assert.fail("exception BeanDefinitionStoreException");
	}
}
