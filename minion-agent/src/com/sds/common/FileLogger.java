package com.sds.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FileLogger {
	
	private static final String newLine = System.getProperty("line.separator");
	
	String fullpath;
	SimpleDateFormat sdf; 
	SimpleDateFormat yyyy;
	SimpleDateFormat ddd;
	String dateValue;
	private String filePath;
	private String appName;
	
	public FileLogger(String filePath, String appName){
		this.filePath=filePath;
		this.appName=appName;
		setFileFullPath(filePath, appName);
	}

	private void setFileFullPath(String filePath, String appName) {
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}
		sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		yyyy=new SimpleDateFormat("yyyy-MM");
		ddd=new SimpleDateFormat("dd");
		
		Date date = new Date();
		filePath = filePath + File.separator + yyyy.format(date) + File.separator ;
		dateValue = ddd.format(date);
		String fileName = appName+"_"+dateValue+".log";

		File samDir = new File(filePath);
		System.out.println(filePath);
		if (!samDir.exists()) {
			samDir.mkdirs();
		}
		fullpath=filePath+File.separator+fileName;
		System.out.println("fullpath:"+fullpath);
	}
	
	public void log(String row){
		Date date = new Date();
		if(dateValue != ddd.format(date)){
			setFileFullPath(filePath, appName);
		}
	    PrintWriter printWriter = null;
	    try {
	    	File file = new File(fullpath);
	        if (!file.exists()) file.createNewFile();
	        printWriter = new PrintWriter(new FileOutputStream(fullpath, true));
	        printWriter.write(sdf.format(new Date())+" "+row+newLine);
	    } catch (IOException ioex) {
	        ioex.printStackTrace();
	    } finally {
	        if (printWriter != null) {
	            printWriter.flush();
	            printWriter.close();
	        }
	    }
	}
	
}
