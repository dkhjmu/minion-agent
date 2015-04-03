package com.sds.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class FileLogger {
	File samFile = null;
	FileWriter fw = null;
	BufferedWriter bw = null;
	FileOutputStream fos = null;
	
	public FileLogger(String filePath, String appName, boolean dateParam){
		Date now = new Date();
		Calendar cal=Calendar.getInstance();
		cal.setTime(now);
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}
		if(dateParam==true){
			filePath = filePath + File.separator + cal.get(Calendar.YEAR) + "_" + (cal.get(Calendar.MONTH)+1) + File.separator ;
		}
		String fileName = appName+".log";
		fileName = appName+"_"+cal.get(Calendar.DATE)+".log";

		File samDir = new File(filePath);
		System.out.println(filePath);
		if (!samDir.exists()) {
			samDir.mkdirs();
			System.out.println("mkdirs");
		}
		samFile = new File(filePath + fileName);
		try{
			if (samFile.exists()) {
				fw = new FileWriter(samFile);
				bw = new BufferedWriter(fw);
				fos = new FileOutputStream(samFile);
			} else {
				samFile.createNewFile();
				fw = new FileWriter(samFile);
				bw = new BufferedWriter(fw);
				fos = new FileOutputStream(samFile);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void log(String row) throws IOException {
		try {
			if (row == null)
				row = "";
			fos.write(row.getBytes("UTF-8"));
			fos.write("\n".getBytes());
		} catch (IOException ie) {
			throw ie;
		}
	}
	
	
//	public static void main(String[] args) throws IOException {
//		FileLogger logger = new FileLogger("D:/log/", "scout", true);
//		logger.log("test2");
//		System.out.println("end~!");
//	}
	
}
