package org.litespring.beans;

import java.util.List;

/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-03-25 10:02:54
 */
public interface BeanDefinition {
	// case1增加对scope的设定
	public static final String SCOPE_DEFAULT = "";
	public static final String SCOPE_SINGLETON = "singleton";
	public static final String SCOPE_PROTOTYPE = "prototype";

	public boolean isSingleton();

	public boolean isPrototype();

	String getScope();
	
	void setScope(String scope);
	
	// case0只有getBeanClassName一个方法
	public String getBeanClassName();
	
	List<PropertyValue> getPropertyValues();

	public ConstructorArgument getConstructorArgument();
}
