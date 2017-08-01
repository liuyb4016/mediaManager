package com.eshore.yxt.media.core.util;

import java.util.Properties;

public class PropertiesUtils {
	private static Properties properties = null;
	public static String applicationAbsolutePath="";//应用根目录在磁盘上的绝对路径，该值应该在应用启动时设置
	public static String httpPath="";//应用url地址，该值可能为空

	private PropertiesUtils(){
		
	}
	private synchronized static void loadProperties() {
		if (properties == null) {
			properties = new InitPropLoader().getProperties("config.properties");
		}
    }
	
	public synchronized static void reloadProperties() {
		properties = null;
		properties = new InitPropLoader().getProperties("config.properties");
    }
	
	public static String getProperty(String property){
		loadProperties();
		String value = null;
		if (properties != null) {
			value = properties.getProperty(property);
			if (value != null) {
				value = value.trim();				
			}			
		}
		return value;
	}
}
