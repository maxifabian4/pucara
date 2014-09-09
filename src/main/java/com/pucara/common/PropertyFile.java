package com.pucara.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertyFile {
	private Properties properties;
	private OutputStream output;

	public PropertyFile(String path) throws IOException {
		properties = new Properties();
		InputStream input = new FileInputStream(path);
		properties.load(input);
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public void saveProperty(String path, String key, String value)
			throws IOException {
		output = new FileOutputStream(path);
		properties.setProperty(key, value);
		properties.store(output, null);
	}
}
