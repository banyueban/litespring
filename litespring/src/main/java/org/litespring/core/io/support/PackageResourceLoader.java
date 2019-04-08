package org.litespring.core.io.support;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.core.io.FileSystemResource;
import org.litespring.core.io.Resource;
import org.litespring.util.Assert;
import org.litespring.util.ClassUtils;

/**
 * @author Charis
 * 根据包名读取class文件为resurce,后续通过IO获取
 */
public class PackageResourceLoader {
	
	private static final Log logger = LogFactory.getLog(PackageResourceLoader.class);

	private final ClassLoader classLoader;
	
	public PackageResourceLoader() {
		this.classLoader = ClassUtils.getDefaultClassLoader();
	}
	public PackageResourceLoader(ClassLoader classLoader) {
		Assert.notNull(classLoader, "ClassLoader must be not null");
		this.classLoader = classLoader;
	}

	public ClassLoader getClassLoader() {
		return this.classLoader;
	}
	/*
	 * 读取package目录下的class文件
	 */
	public Resource[] getResources(String basePackage) throws IOException {
		Assert.notNull(basePackage, "basePackage must be not null");
		String location = ClassUtils.convertClassNameToResourcePath(basePackage);
		ClassLoader cl = getClassLoader();
		URL url = cl.getResource(location);
		File rootDir = new File(url.getFile());
		
		Set<File> matchingFiles = retrieveMatchingFiles(rootDir);
		Resource[] result = new Resource[matchingFiles.size()];
		int i = 0;
		for (File file : matchingFiles) {
			result[i++] = new FileSystemResource(file);
		}
		return result;
	}
	
	protected Set<File> retrieveMatchingFiles(File rootDir) throws IOException {
		if (!rootDir.exists()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Skipping [" + rootDir.getAbsolutePath() + "] beause it does not exists");
			}
			return Collections.emptySet();
		}
		if (!rootDir.isDirectory()) {
			if (logger.isWarnEnabled()) {
				logger.warn("Skipping [" + rootDir.getAbsolutePath() + "] because it does not denote a directory");
			}
			return Collections.emptySet();
		}
		if (!rootDir.canRead()) {
			if (logger.isWarnEnabled()) {
				logger.warn("Can not search for matching files underneath directory [" + rootDir.getAbsolutePath() + 
						"] because the application is not allowed to read the directory");
			}
			return Collections.emptySet();
		}
		Set<File> result = new LinkedHashSet<File>(8);
		doRetrieveMatchingFiles(rootDir, result);
		return result;
	}
	
	/*
	 * 递归目录下的所有文件
	 */
	protected void doRetrieveMatchingFiles(File dir, Set<File> result) throws IOException {
		File[] dirContents = dir.listFiles();
		
		if (null == dirContents) {
			if (logger.isWarnEnabled()) {
				logger.warn("Can not retrieve contents of directory [" + dir.getAbsolutePath() + "]");
			}
			return;
		}
		for (File content : dirContents) {
			if (content.isDirectory()) {
				if (!content.canRead()) {
					if (logger.isDebugEnabled()) {
						logger.debug("Skipping subdirectory[" + content.getAbsolutePath() + 
								"] because the application is not allowed to read the directory");
					}
				} else {
					doRetrieveMatchingFiles(content, result);
				}
			} else {
				result.add(content);
			}
		}
	}

}
