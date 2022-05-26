package mgr_login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracleDB.OracleDB;
import util.MyUtil;

public class MgrLogin {
	
	public boolean mgrLogin() {
		
		System.out.println("===== 관리자 로그인 =====");
		// 아이디, 비번 받기
		System.out.print("아이디 : ");
		String id = MyUtil.sc.nextLine();
		System.out.print("비밀번호 : ");
		String pwd = MyUtil.sc.nextLine();
		
		// 디비와 연결, 관리자 아이디/비번 일치여부
		Connection conn = OracleDB.getOracleConnection();
		String sql = "SELECT * FROM ADMIN WHERE ADM_ID = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			rs.next();
			String dbPwd = rs.getString("ADM_PWD");
			if(pwd.equals(dbPwd)) {
				System.out.println("관리자 ID " + id + " 로그인 되었습니다.");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			OracleDB.close(conn);
			OracleDB.close(rs);
			OracleDB.close(pstmt);
		}
		System.out.println("아이디와 비밀번호를 다시 확인하십시오.");
		return false;
		
	}//mgrLogin
	
	
	public void memberMng() {
		//회원 관리
		
		
	}

}//class
