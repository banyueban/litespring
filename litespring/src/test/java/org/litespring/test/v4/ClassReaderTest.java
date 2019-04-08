package org.litespring.test.v4;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.litespring.core.annotation.AnnotationAttributes;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.type.classreading.AnnotationMetadataReadingVisitor;
import org.litespring.core.type.classreading.ClassMetadataReadingVisitor;
import org.springframework.asm.ClassReader;

public class ClassReaderTest {
	
	@Test
	public void testGetClassMetadata() throws IOException {
		
		ClassPathResource resource = new ClassPathResource("org/litespring/service/v4/PetStoreService.class");
		ClassReader reader = new ClassReader(resource.getInputStream());
		
		ClassMetadataReadingVisitor visitor = new ClassMetadataReadingVisitor();
		
		reader.accept(visitor, ClassReader.SKIP_DEBUG);
		
		assertFalse(visitor.isAbstract());
		assertFalse(visitor.isInterface());
		assertFalse(visitor.isFinal());
		assertEquals("org.litespring.service.v4.PetStoreService",visitor.getClassName());
		assertEquals("java.lang.Object",visitor.getSuperClassName());
		assertEquals(0, visitor.getInterfaceNames().length);
	}
	
	@Test
	public void testGetAnnotation() throws IOException {
		ClassPathResource resource = new ClassPathResource("org/litespring/service/v4/PetStoreService.class");
		ClassReader reader = new ClassReader(resource.getInputStream());
		
		AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
		
		reader.accept(visitor, ClassReader.SKIP_DEBUG);
		String annotation = "org.litespring.stereotype.Component";
		assertTrue(visitor.hasAnnotation(annotation));
		
		AnnotationAttributes attributes = visitor.getAnnotationAttributes(annotation);
		
		assertEquals("petStore", attributes.get("value"));
	}
}
