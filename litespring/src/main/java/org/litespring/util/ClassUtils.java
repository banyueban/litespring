package org.litespring.util;

/**
 * @author Charis
 * this util copied from spring
 */
public class ClassUtils {

	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			//
			cl = Thread.currentThread().getContextClassLoader();
		} catch(Throwable t) {
			
		}
		if (null == cl) {
			cl = ClassLoader.class.getClassLoader();
			if (null == cl) {
				try {
					cl = ClassLoader.getSystemClassLoader();
				} catch(Throwable t) {
					
				}
			}
		}
		return cl;
	}

}
