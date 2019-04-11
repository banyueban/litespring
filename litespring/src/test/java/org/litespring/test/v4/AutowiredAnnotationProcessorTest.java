package org.litespring.test.v4;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;
import org.litespring.beans.factory.annotation.AutowiredAnnotationProcessor;
import org.litespring.beans.factory.annotation.AutowiredFieldElement;
import org.litespring.beans.factory.annotation.InjectionElement;
import org.litespring.beans.factory.annotation.InjectionMetadata;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.dao.v4.AccountDao;
import org.litespring.dao.v4.ItemDao;
import org.litespring.service.v4.PetStoreService;
public class AutowiredAnnotationProcessorTest {

	AccountDao accountDao = new AccountDao();
	ItemDao itemDao = new ItemDao();
	// mock一个AutowireCapableBeanFactory,重写了resolveDependency()方法,返回定义好的itemDao和accountDao
	DefaultBeanFactory factory = new DefaultBeanFactory() {

		@Override
		public Object resolveDependency(DependencyDescriptor descriptor) {
			if (descriptor.getDependencyType().equals(AccountDao.class)) {
				return accountDao;
			}
			if (descriptor.getDependencyType().equals(ItemDao.class)) {
				return itemDao;
			}
			throw new RuntimeException("Could not resolve type except AccountDao and ItemDao");
		}
		
	};
	
	@Test
	public void testGetInjectionMetadata() {
		AutowiredAnnotationProcessor processor = new AutowiredAnnotationProcessor();
		processor.setBeanFactory(factory);
		InjectionMetadata injectionMetadata = processor.buildAutowiringMetadata(PetStoreService.class);
		LinkedList<InjectionElement> injectionElements = injectionMetadata.getInjectionElement();
		
		assertEquals(2, injectionElements.size());
		assertExistField(injectionElements, "accountDao");
		assertExistField(injectionElements, "itemDao");
		
		PetStoreService petStore = new PetStoreService();
		injectionMetadata.inject(petStore);
		
		assertTrue(petStore.getAccountDao() instanceof AccountDao);
		assertTrue(petStore.getItemDao() instanceof ItemDao);
		
	}

	private void assertExistField(LinkedList<InjectionElement> elements, String fieldName) {
		for (InjectionElement ele : elements) {
			AutowiredFieldElement fieldEle = (AutowiredFieldElement) ele;
			if (fieldName.equals(fieldEle.getField().getName())) {
				return;
			}
		}
		fail(fieldName + "doesn't exist");
	}
}
