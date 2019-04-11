package org.litespring.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public abstract class AnnotationUtils {

	public static <T extends Annotation> T getAnnotation(AnnotatedElement ae, Class<T> annotationType) {
		T ann = ae.getAnnotation(annotationType);
		if (ann == null) {
			// 处理组合注解,找到需要的元注解
			for (Annotation metaAnn : ae.getAnnotations()) {
				ann = metaAnn.annotationType().getAnnotation(annotationType);
				if (null != ann) {
					break;
				}
			}
		}
		return ann;
	}

}
