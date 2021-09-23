/**
 * 
 */
package com.cioxhealth.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ReadPropertiesFile {

	private Properties properties;

	public ReadPropertiesFile() {
		properties = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = getClass().getClassLoader().getResourceAsStream("environment.properties");
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getPropertyValue(String key) {
		return properties.getProperty(key);
	}
}
