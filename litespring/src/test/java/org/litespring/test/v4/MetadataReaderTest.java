package org.litespring.test.v4;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.litespring.core.annotation.AnnotationAttributes;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.type.AnnotationMetadata;
import org.litespring.core.type.ClassMetadata;
import org.litespring.core.type.classreading.MetadataReader;
import org.litespring.core.type.classreading.SimpleMetadataReader;
import org.litespring.stereotype.Component;

public class MetadataReaderTest {

	@Test
	public void testGetMetadata() throws IOException {
		
		ClassPathResource resource = new ClassPathResource("org/litespring/service/v4/PetStoreService.class");
		MetadataReader reader = new SimpleMetadataReader(resource);
		
		ClassMetadata cm = reader.getClassMetadata();
		AnnotationMetadata amd = reader.getAnnotationMetadata();
		
		String annotation = Component.class.getName();
		
		assertTrue(amd.hasAnnotation(annotation));
		AnnotationAttributes attributes = amd.getAnnotationAttributes(annotation);
		assertEquals("petStore", attributes.get("value"));
		
		assertFalse(amd.isAbstract());
		assertFalse(amd.isFinal());
		assertFalse(amd.isInterface());
		assertEquals("org.litespring.service.v4.PetStoreService", amd.getClassName());
	}
}
