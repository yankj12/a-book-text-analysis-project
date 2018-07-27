package com.yan.common.util;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesIOUtil {
	
	public static Properties loadProperties(String fileName){
		Properties pro = new Properties();
		try {
			FileInputStream in = new FileInputStream(PropertiesIOUtil.class.getResource(fileName).getPath());
			pro.load(in);
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pro;
	}
}
