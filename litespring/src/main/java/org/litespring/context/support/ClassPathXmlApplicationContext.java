package org.litespring.context.support;

import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.Resource;

/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-03-25 18:00:17
 * 对BeanFactory和XmlBeanDefinitionReader做了包装
 * 通过模版方法对FileSystemXmlApplicationContext的公共方法进行重构
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

//	private DefaultBeanFactory factory = null;
//	
//	public ClassPathXmlApplicationContext(String configFile) {
//		this.factory = new DefaultBeanFactory();
//		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
//		Resource resource = new ClassPathResource(configFile);
//		reader.loadBeanDefinitions(resource);
//	}
//
//	public Object getBean(String beanID) {
//		return factory.getBean(beanID);
//	}
	public ClassPathXmlApplicationContext(String configFile) {
		super(configFile);
	}
	
	@Override
	protected Resource getResourceByPath(String path) {
		return new ClassPathResource(path);
	}

}
