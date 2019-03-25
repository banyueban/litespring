package org.litespring.util;

/**
 * @author MAQUN
 *abstract避免类被实例化
 */
public abstract class Assert {

	public static void notNull(Object object, String message) {
		if (null == object) {
			throw new IllegalArgumentException(message);
		}
		
	}
	
}
