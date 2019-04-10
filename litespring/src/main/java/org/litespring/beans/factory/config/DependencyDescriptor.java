package org.litespring.beans.factory.config;

import java.lang.reflect.Field;

/**
 * @author Charis
 * 属性描述符
 */
public class DependencyDescriptor {

	private Field field;
	
	private boolean required;
	
	public DependencyDescriptor(Field field, boolean required) {
		this.field = field;
		this.required = required;
	}

	public Class<?> getDependencyType() {
		if (null != this.field) {
			return this.field.getType();
		}
		throw new RuntimeException("only support field dependency");
	}
	
	public boolean isRequired() {
		return this.required;
	}
}
