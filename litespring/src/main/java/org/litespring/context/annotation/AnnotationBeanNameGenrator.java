package org.litespring.context.annotation;

import java.beans.Introspector;
import java.util.Set;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.annotation.AnnotatedBeanDefinition;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.BeanNameGenerator;
import org.litespring.core.annotation.AnnotationAttributes;
import org.litespring.core.type.AnnotationMetadata;
import org.litespring.util.ClassUtils;
import org.litespring.util.StringUtils;

/**
 * @author Charis
 * 生成BeanName
 */
public class AnnotationBeanNameGenrator implements BeanNameGenerator {

	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		if (definition instanceof AnnotatedBeanDefinition) {
			String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
			if (StringUtils.hasText(beanName)) {
				return beanName;
			}
		}
		return buildDefaultBeanName(definition, registry);
	}
	
	/*
	 * 取注解的value属性
	 */
	private String determineBeanNameFromAnnotation(AnnotatedBeanDefinition definition) {
		AnnotationMetadata amd = definition.getMetadata();
		Set<String> types = amd.getAnnotationTypes();
		String beanName = null;
		for (String type : types) {
			AnnotationAttributes attributes = amd.getAnnotationAttributes(type);
			if (null != attributes.get("value")) {
				Object value = attributes.get("value");
				if (value instanceof String) {
					String strVal = (String) value;
					if (StringUtils.hasLength(strVal)) {
						// 多个value属性处理
						if (beanName != null && !strVal.equals(beanName)) {
							throw new IllegalStateException("Stereotype annotations suggest inconsistent " +
									"component names: '" + beanName + "' versus '" + strVal + "'");
						}
						beanName = strVal;
					}
				}
			}
		}
		return beanName;
	}

	/*
	 * 类名首字母小写
	 */
	private String buildDefaultBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		return buildDefaultBeanName(definition);
	}

	private String buildDefaultBeanName(BeanDefinition definition) {
		String shortName = ClassUtils.getShortName(definition.getBeanClassName());
		return Introspector.decapitalize(shortName);
	}
}
