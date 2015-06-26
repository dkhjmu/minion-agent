package com.sds.common;

import java.util.HashMap;

public class LogFactory {
	private static HashMap<String, FileLogger> loggerMap = new HashMap<String, FileLogger>();
	public static FileLogger getLogger(String appName) {
		if(loggerMap.get(appName)!=null){
			return loggerMap.get(appName);
		}else{
			if(appName.equals("EMPTY")){
				FileLogger empty = new FileLogger("EMPTY", appName);
				return empty;
			}else{
				return null;
			}
		}
	}
	public static void putLogger(String key, FileLogger value){
		loggerMap.put(key, value);
	}
	
}
