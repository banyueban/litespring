package org.litespring.test.v1;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.FileSystemResource;
import org.litespring.core.io.Resource;

public class ResourceTest {

	@Test
	public void testClassPathResource() {
		Resource r = new ClassPathResource("petstore-v1.xml");
		InputStream is = null;
		try {
			is = r.getInputStream();
			// 测试不充分，应该打印出来比较
			Assert.assertNotNull(is);
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void testFileSystemResource() {
		Resource r = new FileSystemResource("D:\\git\\litespring\\litespring\\src\\test\\resources\\petstore-v1.xml");
		InputStream is = null;
		try {
			is = r.getInputStream();
			Assert.assertNotNull(is);
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
