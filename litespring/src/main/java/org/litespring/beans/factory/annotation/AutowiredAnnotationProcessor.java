package org.litespring.beans.factory.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.litespring.beans.factory.config.AutowireCapableBeanFactory;
import org.litespring.core.annotation.AnnotationUtils;
import org.litespring.util.ReflectionUtils;

/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-04-11 16:10:04
 * 处理@Autowired注解,生成InjectionMetadata,实现对依赖的注入
 */
public class AutowiredAnnotationProcessor {
	
	private AutowireCapableBeanFactory beanFactory;
	
	private Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<Class<? extends Annotation>>();

	private String requiredParameterName = "required";

	private Object requiredParameterValue = true;
	
	public AutowiredAnnotationProcessor() {
		this.autowiredAnnotationTypes.add(Autowired.class);
	}
	
	public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	/*
	 * 获取@Autowired标注的Field属性,生成InjectionMetadata
	 */
	public InjectionMetadata buildAutowiringMetadata(Class<?> clazz) {
		LinkedList<InjectionElement> elements = new LinkedList<InjectionElement>();
		Class<?> targetClass = clazz;
		do {
			LinkedList<InjectionElement> currElements = new LinkedList<InjectionElement>();
			for (Field field : targetClass.getDeclaredFields()) {
				Annotation ann = findAutowiredAnnotation(field);
				if (ann != null) {
					if (Modifier.isStatic(field.getModifiers())) {
						continue;
					}
					boolean required = determineRequiredStatus(ann);
					currElements.add(new AutowiredFieldElement(field, required, beanFactory));
				}
			}
			
			// 处理方法上的@Autowired注解
			for (Method method : targetClass.getDeclaredMethods()) {
				// TODO
			}
			
			elements.addAll(0, currElements);
			// TODO 处理依赖链(父类..)上的@Autowired注解???
			targetClass = targetClass.getSuperclass();
		} while(targetClass != null && targetClass != Object.class);
		
		return new InjectionMetadata(clazz, elements);
	}

	/*
	 * 
	 */
	protected boolean determineRequiredStatus(Annotation ann) {
		try {
			Method method = ReflectionUtils.findMethod(ann.annotationType(), this.requiredParameterName);
			if (method == null) {
				return true;
			}
			return this.requiredParameterValue  == (Boolean) ReflectionUtils.invokeMethod(method, ann);
		} catch(Exception ex) {
			return true;
		}
	}

	/*
	 * 
	 */
	private Annotation findAutowiredAnnotation(AccessibleObject ao) {
		for(Class<? extends Annotation> type : this.autowiredAnnotationTypes) {
			Annotation ann = AnnotationUtils.getAnnotation(ao, type);
			if (null != ann) {
				return ann;
			}
		}
		return null;
	}

}
