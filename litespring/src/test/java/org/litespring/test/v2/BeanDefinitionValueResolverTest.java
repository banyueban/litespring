package org.litespring.test.v2;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.BeanDefinitionValueResolver;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.dao.v2.AccountDao;

public class BeanDefinitionValueResolverTest {
	
	DefaultBeanFactory factory = null;
	
	XmlBeanDefinitionReader reader = null;
	
	BeanDefinitionValueResolver resolver = null;
			
	@Before
	public void setUp() {
		factory = new DefaultBeanFactory();
		reader = new XmlBeanDefinitionReader(factory);
		reader.loadBeanDefinitions(new ClassPathResource("petstore-v2.xml"));
		resolver = new BeanDefinitionValueResolver(factory);
	}
	
	@Test
	public void testResolveRuntimeBeanReference() {
		RuntimeBeanReference reference = new RuntimeBeanReference("accountDao");
		Object value = resolver.resolveValueIfNecessary(reference);
		assertNotNull(value);
		assertTrue(value instanceof AccountDao);
	}
	
	@Test
	public void testResolveTypedStringValue() {
		TypedStringValue stringValue = new TypedStringValue("test");
		Object value = resolver.resolveValueIfNecessary(stringValue);
		assertNotNull(value);
		assertEquals("test",value);
	}
}
