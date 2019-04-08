package org.litespring.test.v4;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.litespring.core.io.Resource;
import org.litespring.core.io.support.PackageResourceLoader;

public class PackageResourceLoaderTest {
	
	@Test
	public void testGetResoutces() throws IOException {
		PackageResourceLoader loader = new PackageResourceLoader();
		Resource[] resources = loader.getResources("org.litespring.dao.v4");
		
		for (Resource r : resources) {
			System.out.println(r.getDescription());
		}
		assertEquals(2, resources.length);
	}
}
