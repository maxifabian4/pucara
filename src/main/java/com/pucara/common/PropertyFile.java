package com.pucara.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFile {
	private Properties properties;

	public PropertyFile(String path) throws IOException {
		properties = new Properties();
		InputStream input = new FileInputStream(path);
		properties.load(input);
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}
}
