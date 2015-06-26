package com.sds.minion.agent.run;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sds.minion.agent.domain.AppStatus;

public class DbChecker {
	public static String check(String check, String userId, String password){
		Connection conn=null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			Class.forName("com.tmax.tibero.jdbc.TbDriver");
			conn = DriverManager.getConnection(check, userId, password);
			// select count(*) cnt from VT_SESSION
//			ps = conn.prepareStatement("select count(*) cnt from VT_SESSION");
//			rs = ps.executeQuery();
//			if(rs!=null){
//				while(rs.next()){
//					String cnt = rs.getString(1);
//					System.out.println("Number of Sessions : "+cnt);
//				}
//			}
			conn.getAutoCommit();
			return AppStatus.LIVED;
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
	}
	
//	public static void main(String[] args) {
//		System.out.println(DbChecker.check("jdbc:tibero:thin:@70.121.224.24:8629:openpms", "sys", "sds000"));
//	}
	
}	
