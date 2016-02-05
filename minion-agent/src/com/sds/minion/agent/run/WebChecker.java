package com.sds.minion.agent.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.sds.minion.agent.domain.AppStatus;

public class WebChecker {
	public static String check(String check){
		HttpURLConnection conn =null;
		InputStream in = null;
		try {
			URL url = new URL(check);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
			in = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
//			String inputLine;
//	        while ((inputLine = br.readLine()) != null){ 
//	            System.out.println(inputLine);
//	        }
			return AppStatus.LIVED;
		} catch (Exception ex) {
			return AppStatus.DEAD;
		} finally{
			if(in !=null ){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null) conn.disconnect();
		}
	}
	
}
