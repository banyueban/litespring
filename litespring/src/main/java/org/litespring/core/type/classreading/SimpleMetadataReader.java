package org.litespring.core.type.classreading;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.litespring.core.io.Resource;
import org.litespring.core.type.AnnotationMetadata;
import org.litespring.core.type.ClassMetadata;
import org.springframework.asm.ClassReader;

/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-04-09 14:47:41
 * 封装asm的操作,仅暴露接口
 */
public class SimpleMetadataReader implements MetadataReader {

	private final Resource resource;
	
	private final ClassMetadata classMetadata;
	
	private final AnnotationMetadata annotationMetadata;
	
	public SimpleMetadataReader(Resource resource) throws IOException {
		InputStream is = new BufferedInputStream(resource.getInputStream());
		ClassReader classReader = null;
		try {
			classReader = new ClassReader(is);
		} 
		finally {
			is.close();
		}
		AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
		classReader.accept(visitor, ClassReader.SKIP_DEBUG);
		this.resource = resource;
		this.classMetadata = visitor;
		this.annotationMetadata = visitor;
		
	}

	@Override
	public ClassMetadata getClassMetadata() {
		return this.classMetadata;
	}

	@Override
	public AnnotationMetadata getAnnotationMetadata() {
		return this.annotationMetadata;
	}

	@Override
	public Resource getResource() {
		return this.resource;
	}

}
