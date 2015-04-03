package com.sds.minion.agent.service.impl;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;

public class HealInfo {
	
	public static String getMemoryUsage(){
	    Runtime instance = Runtime.getRuntime();
	    StringBuffer sb = new StringBuffer();
	    long totalMemory = instance.totalMemory();
	    long freeMemory = instance.freeMemory();
	    
	    float usage = round(((totalMemory - freeMemory) * 1.0f / totalMemory) * 100.0f, 2);
	    sb.append("Memory Usage:" + usage + "%");
		return sb.toString();
	}
	
	private static float round(float d, int decimalPlace) {
	    BigDecimal bd = new BigDecimal(Float.toString(d));
	    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
	    return bd.floatValue();
	}
	
	public static String getCpuUsage(){
		double cpu = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
		return String.valueOf(cpu);
	}
	
	public static String getDiskUsage(){
		File file = new File("/");
	       long disk = file.getTotalSpace() - file.getFreeSpace();
	       String diskUsage = (disk / 1024 / 1024) + " mb";
	       return diskUsage;
	}
}
