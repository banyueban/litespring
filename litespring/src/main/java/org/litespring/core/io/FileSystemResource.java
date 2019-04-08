package org.litespring.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.litespring.util.Assert;

public class FileSystemResource implements Resource {
	
	// TODO 这里path为什么是final？
	private final String path;
	
	private final File file;
	
	public FileSystemResource(String path) {
		Assert.notNull(path, "path must be not null");
		this.path = path;
		this.file = new File(path);
	}
	
	public FileSystemResource(File file) {
		this.path = file.getPath();
		this.file = file;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(this.file);
	}
	
	@Override
	public String getDescription() {
		return "file [" + this.file.getAbsolutePath() + "]";
	}

}
