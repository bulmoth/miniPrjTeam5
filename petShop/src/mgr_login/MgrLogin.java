package mgr_login;

import java.sql.Connection;
import java.sql.Date;
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
		boolean isCorrect = true; 
		//회원 관리
		System.out.println("===== 회원 관리 페이지 =====");
		System.out.println("1. 회원 조회 및 관리");
		System.out.println("2. 오늘의 회원 변동");
		
		while(isCorrect) {
			int n = MyUtil.scInt();
			
			switch(n) {
			case 1: memberList(); break;
			case 2: todayMem(); break;
			default: System.out.println("상위 메뉴로 돌아갑니다."); isCorrect = false;
			}
		}
		return;
	}//memberMng
	
	
	private void memberList() {
		//회원 조회
		System.out.println("===== 회원 목록 조회 =====");
		//회원 번호
		int mem_no;
		//연결 얻기
		Connection conn = OracleDB.getOracleConnection();
		String sql = "SELECT RPAD(MEM_NO, 6,' ') MEM_NO, RPAD(MEM_ID, 20,' ') MEM_ID, RPAD(MEM_PWD, 20,' ') MEM_PWD,"
				+ " RPAD(NAME, 20,' ') NAME, ENROLL_DATE, QUIT_YN "
				+ "FROM MEMBER ORDER BY ENROLL_DATE DESC";
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println("회원번호 |        아이디        |        비밀번호       |         이름        |  회원가입일  | 탈퇴여부");
			while(rs.next()) {
				memLook(rs);
			}
			int select = 0;
			while(select != -1) {
				System.out.print("탈퇴시킬 회원 번호를 선택해 주십시오 (나가기 = -1) : ");
				select = MyUtil.scInt();
				String sql2 = "SELECT * FROM MEMBER WHERE MEM_NO = ?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setInt(1, select);
				rs2 = pstmt2.executeQuery();
				rs2.next();
				mem_no = rs2.getInt("MEM_NO");
				if(!rs2.next()) {
					System.out.println("해당 값은 존재하지 않습니다.");
					System.out.println("상위 메뉴로 돌아갑니다.");
					return;
				}
				memWithdraw(mem_no);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			OracleDB.close(conn);
			OracleDB.close(rs);
			OracleDB.close(pstmt);
			OracleDB.close(rs2);
		}
	}//memberList

	
	
	private void memLook(ResultSet rs) {
		try {
			int mem_no = rs.getInt("MEM_NO");
			String mem_id = rs.getString("MEM_ID");
			String mem_pwd = rs.getString("MEM_PWD");
			String name = rs.getString("NAME");
			Date enroll_date = rs.getDate("ENROLL_DATE");
			String quit_yn = rs.getString("QUIT_YN");
			System.out.println(mem_no + mem_id + mem_pwd + name + enroll_date + quit_yn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}//memLook
	
	
	private void memWithdraw(int mem_no) {
		//회원 강퇴
		System.out.println("탈퇴시키겠습니까? (y/n)");
		String answer = MyUtil.sc.nextLine();
		if("y".equals(answer)) {
			Connection conn = OracleDB.getOracleConnection();
			String sql = "UPDATE MEMBER SET QUIT_YN = 'Y' WHERE MEM_NO = ?";
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, mem_no);
				int result = pstmt.executeUpdate();
				if(result == 1) {
					System.out.println("탈퇴가 성공적으로 이루어졌습니다.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				OracleDB.close(conn);
				OracleDB.close(pstmt);
			}
			System.out.println("탈퇴 처리에 실패하였습니다.");
		}else {
			System.out.println("상위 메뉴로 돌아갑니다.");
			return;
		}
		
	}//memWithdraw
	
	
	
	private void todayMem() {
		//오늘의 회원 변동 조회
		
	}

}//class
