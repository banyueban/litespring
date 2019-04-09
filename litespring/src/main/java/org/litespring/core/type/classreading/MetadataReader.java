package org.litespring.core.type.classreading;

import org.litespring.core.io.Resource;
import org.litespring.core.type.AnnotationMetadata;
import org.litespring.core.type.ClassMetadata;

/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-04-09 14:47:03
 * 封装asm的操作
 */
public interface MetadataReader {

	Resource getResource();
	
	ClassMetadata getClassMetadata();

	AnnotationMetadata getAnnotationMetadata();

}
