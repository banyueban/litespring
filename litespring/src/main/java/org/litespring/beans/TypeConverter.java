package org.litespring.beans;

/**
 * @author Charis
 * property中的类型转换实现接口
 */
public interface TypeConverter {
	<T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException;
}
