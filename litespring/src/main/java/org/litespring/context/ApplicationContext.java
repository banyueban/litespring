package org.litespring.context;

import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;

/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-03-25 17:41:00
 *
 */
// 由于AutowireCapableBeanFactory不需要Application来实现,修改
//public interface ApplicationContext extends ConfigurableBeanFactory {
public interface ApplicationContext extends BeanFactory {
	
}
