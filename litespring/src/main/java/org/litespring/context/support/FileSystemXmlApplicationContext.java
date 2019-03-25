package org.litespring.context.support;

import org.litespring.core.io.FileSystemResource;
import org.litespring.core.io.Resource;

/**
 * @author MAQUN
 *绝对路径加载配置文件,定义bean
 *通过模版方法重构
 */
public class FileSystemXmlApplicationContext extends AbstractApplicationContext {
	
//	private DefaultBeanFactory factory = null;
//	
//	public FileSystemXmlApplicationContext(String path) {
//		factory = new DefaultBeanFactory();
//		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
//		Resource resource = new FileSystemResource(path);
//		reader.loadBeanDefinitions(resource);
//	}
//
//	public Object getBean(String beanID) {
//		return this.factory.getBean(beanID);
//	}
	public FileSystemXmlApplicationContext(String configFile) {
		super(configFile);
	}

	@Override
	protected Resource getResourceByPath(String path) {
		return new FileSystemResource(path);
	}

}
