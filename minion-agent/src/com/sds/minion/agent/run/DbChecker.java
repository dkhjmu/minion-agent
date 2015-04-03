package com.sds.minion.agent.run;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.sds.minion.agent.domain.AppStatus;

public class DbChecker {
	public static String check(String check, String userId, String password){
		Connection conn=null;
		try{
			Class.forName("com.tmax.tibero.jdbc.TbDriver");
			conn = DriverManager.getConnection(check, userId, password);
			conn.getAutoCommit();
			return AppStatus.LIVED;
		}catch(Exception e){
			return AppStatus.DEAD;
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
//	public static void main(String[] args) {
//		System.out.println(DbChecker.check("jdbc:tibero:thin:@192.168.1.2:8629:openpms", "sys", "sds000"));
//	}
	
}	
