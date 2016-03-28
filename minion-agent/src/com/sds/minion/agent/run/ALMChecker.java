package com.sds.minion.agent.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sds.common.FileLogger;
import com.sds.common.LogFactory;
import com.sds.minion.agent.domain.AppStatus;

public class ALMChecker {
	
	public static String check(String appName, String url){
		FileLogger logger=LogFactory.getLogger(appName);
		try{
			String result = checkWAS(url);
			logger.log(appName+"\t WAS Result:"+result);
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        //System.out.println(sdf.format(new Date()) + " " + appName+"\t WAS Result:"+result);
	        // /rest/projectInfo/get/
	        result = sendRequestGetToGoServer(url+"/rest/projectInfo/get/administrator");
	        
	        if(!result.equals(AppStatus.DEAD)){
	        	ObjectMapper mapper = new ObjectMapper();
	        	JsonNode jsonNode = mapper.readValue(result, JsonNode.class).get("projects");
	        	if (!jsonNode.isArray()) {
	        	    throw new Exception("ERROR:"+result);
	        	}
	        }
		}catch(Exception e){
			logger.log(e.getMessage());
			e.printStackTrace();
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//send mail
			String json = "{ \"sender\": \"hjmu.kang@samsung.com\", \"receivers\": [\"hjmu.kang\"], \"title\": \""
		        + "[OpenALM] " + sdf.format(new Date()) + "  " + appName + " 서버 이상 발생! "  
		        + "\","
		        + " \"content\": \""
		        + "<p>" + sdf.format(new Date()) + "</p>"
		        + "<p>"+ appName +"("+url+") 서버에 이상이 감지 되었습니다! 확인해주세요!</p>\""
		        + "   }";
		    sendRequestPostToGoServer("http://70.121.224.24:9090/scout/remote/singlemails?auth=pass", json );
			
			return AppStatus.DEAD;
		}
		return AppStatus.LIVED;
	}
	
	public static String checkWAS(String check){
		HttpURLConnection conn =null;
		InputStream in = null;
		try {
			URL url = new URL(check);
			conn = (HttpURLConnection) url.openConnection();
			in = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String inputLine;
			StringBuffer sb=new StringBuffer();
	        while ((inputLine = br.readLine()) != null){ 
//	            System.out.println(inputLine);
	            sb.append(inputLine);
	        }
			if(sb.toString().equals("")){
				return AppStatus.DEAD;
			}
			return sb.toString();
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
	
	public static void sendRequestPostToGoServer(String url, String input) {
	    try {
	      URL urlObj = new URL(url);
	      HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
	      conn.setDoOutput(true);
	      conn.setRequestMethod("POST");
	      conn.setRequestProperty("Content-Type", "application/json");
	      OutputStream os = conn.getOutputStream();
	      os.write(input.getBytes());
	      os.flush();
	      if (conn.getResponseCode() != 200) {
	        System.out.println("Send mail fail.");
	        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	      }

	      BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	      String output;
	      while ((output = br.readLine()) != null) {
	        System.out.println(output);
	      }
	      conn.disconnect();
	    } catch (Exception e) {
	      System.out.println(e.toString());
	    }
	  }
	
	public static String sendRequestGetToGoServer(String urlStr) throws Exception{
	    StringBuffer result = new StringBuffer();
	    HttpURLConnection conn = null;
	    try {
	      URL url = new URL(urlStr);
	      conn = (HttpURLConnection) url.openConnection();
	      conn.setRequestMethod("GET");
	      conn.setRequestProperty("Accept", "application/json");
	      if (conn.getResponseCode() != 200) {
	          throw new RuntimeException("Failed : HTTP error code : "
	                  + conn.getResponseCode());
	      }
	      BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	      String output = null;
	      while ((output = br.readLine()) != null) {
//	          System.out.println(output);
	          result.append(output);
	      }
	      conn.disconnect();
	    } catch (Exception e) {
	      System.out.println(e.toString());
	      throw e;
	    }finally{
	      if(conn!=null){
	        conn.disconnect();
	      }
	    }
	    return result.toString();
	  }
}
