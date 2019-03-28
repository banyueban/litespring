package org.litespring.beans.factory.config;

/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-03-28 15:44:23
 * 存储bean的property属性中ref属性的属性值
 */
public class RuntimeBeanReference {
	
	private String beanName;
	
	public RuntimeBeanReference(String beanName) {
		this.beanName = beanName;
	}
	
	public String getBeanName() {
		return this.beanName;
	}
}
