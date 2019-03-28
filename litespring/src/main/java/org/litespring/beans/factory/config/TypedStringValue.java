package org.litespring.beans.factory.config;

/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-03-28 15:44:32
 * 存储property属性中value为String的属性值
 */
public class TypedStringValue {
	
	private String value;
	
	public TypedStringValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
