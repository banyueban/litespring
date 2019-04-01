package org.litespring.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.litespring.util.ClassUtils;
/**
 * 加载classpath路径下的xml配置文件
 */
public class ClassPathResource implements Resource {

	private final String path;
	
	private ClassLoader classLoader;
	
	public ClassPathResource(String path) {
		this(path, null);
	}

	public ClassPathResource(String path, ClassLoader classLoader) {
		this.path = path;
		this.classLoader = null != classLoader ? classLoader : ClassUtils.getDefaultClassLoader();
	}
	@Override
	public InputStream getInputStream() throws IOException {
		InputStream is = this.classLoader.getResourceAsStream(this.path);
		if (null == is) {
			throw new FileNotFoundException(this.path + "can not be opend");
		}
		return is;
	}
	@Override
	public String getDescription() {
		return this.path;
	}

}
