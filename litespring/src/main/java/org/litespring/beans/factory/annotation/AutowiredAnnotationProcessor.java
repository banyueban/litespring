package org.litespring.beans.factory.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.litespring.beans.BeansException;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.AutowireCapableBeanFactory;
import org.litespring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.litespring.core.annotation.AnnotationUtils;
import org.litespring.util.ReflectionUtils;

/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-04-11 16:10:04
 * 处理@Autowired注解,生成InjectionMetadata,实现对依赖的注入
 */
public class AutowiredAnnotationProcessor implements InstantiationAwareBeanPostProcessor {
	
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
			// 处理依赖链(父类..)上的@Autowired注解
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
	 * 找到被目标注解标注的Field,如果该Field上不存在,则判断组合注解上是否存在
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

	@Override
	public Object beforeInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object afterInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object beforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean afterInstantiation(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void postProcessPropertyValues(Object bean, String beanName) throws BeansException {
		InjectionMetadata metadata = buildAutowiringMetadata(bean.getClass());
		try {
			metadata.inject(bean);
		} catch(Throwable ex) {
			throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
		}
	}

}
