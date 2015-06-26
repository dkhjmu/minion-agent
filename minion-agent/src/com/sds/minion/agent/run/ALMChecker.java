package com.sds.minion.agent.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sds.common.FileLogger;
import com.sds.common.LogFactory;
import com.sds.minion.agent.domain.AppStatus;

public class ALMChecker {
	
//	public static void main(String[] args) {
//		check("EMPTY");
//	}
	
	public static String check(String appName){
		FileLogger logger=LogFactory.getLogger(appName);
		try{
			String sessionCnt = checkDB("jdbc:tibero:thin:@70.121.244.212:8629:openalm", "openalm", "sds000");
			String result = checkWAS("http://70.121.244.211:9090/scout/mini");
			logger.log(appName+"\t WAS Result:"+result);
			logger.log(appName+"\t DB Session:"+sessionCnt);
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        System.out.println(sdf.format(new Date()) + " " + appName+"\t WAS Result:"+result);
	        System.out.println(sdf.format(new Date()) + " " + appName+"\t DB Session:"+sessionCnt);
		}catch(Exception e){
			logger.log(e.getMessage());
			e.printStackTrace();
			return AppStatus.DEAD;
		}
		return AppStatus.LIVED;
	}
	
	@SuppressWarnings("resource")
	public static String checkDB(String check, String userId, String password){
		Connection conn=null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			Class.forName("com.tmax.tibero.jdbc.TbDriver");
			conn = DriverManager.getConnection(check, userId, password);
			// select count(*) cnt from VT_SESSION
			String cnt;
			String total=AppStatus.DEAD;
			ps = conn.prepareStatement("select count(*) cnt from VT_SESSION where user_name = 'OPENALM' and status = 'RUNNING'");
			rs = ps.executeQuery();
			if(rs!=null){
				while(rs.next()){
					cnt = rs.getString(1);
					System.out.println("Number of Running Sessions : "+cnt);
				}
			}
			ps = conn.prepareStatement("select count(*) cnt from VT_SESSION where user_name = 'OPENALM'");
			rs = ps.executeQuery();
			if(rs!=null){
				while(rs.next()){
					total = rs.getString(1);
					System.out.println("Number of Total Sessions : "+total);
				}
			}
			return total;
		}catch(Exception e){
			e.printStackTrace();
			return AppStatus.DEAD;
		}finally{
			try {
				if (rs != null){
					rs.close();
					rs=null;
				}
			} catch (Exception ex) {
			} // rs close
			try {
				if (ps != null){
					ps.close();
					ps=null;
				}
			} catch (Exception ex) {
			} // ps close
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}//checkDb
	
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
	            System.out.println(inputLine);
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
	
}
