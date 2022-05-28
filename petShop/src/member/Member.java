package member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracleDB.OracleDB;
import util.MyUtil;	

public class Member {
	public static int LOGIN_USER_NO;	
	
	public boolean login() {
		System.out.println("========로그인 하기==========");
		System.out.print("아이디 : ");
		String id = MyUtil.sc.nextLine();
		System.out.print("비밀번호 : ");
		String pwd = MyUtil.sc.nextLine();
		
		Connection conn =  OracleDB.getOracleConnection();
		//해당 아이디에 맞는 패스워드 조회하기(DB)
		String sql = "SELECT MEM_NO, MEM_PWD FROM MEMBER WHERE MEM_ID = ?"; //패스워드를 조회하는데 id가 ? 인 친구의 pwd를 조회
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String dbPwd = rs.getString("MEM_PWD"); // 첫번째 칼럼의 행을 조회 
				int no = rs.getInt("MEM_NO");
				if(dbPwd.equalsIgnoreCase(pwd)) {
					//로그인 성공
					LOGIN_USER_NO = no;
					System.out.println("로그인 성공!!!");
					return true;
				}
			}
		
		
		} catch (SQLException e) {
			System.out.println("SQL 오류 발생");
		}finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(rs);
		}
		System.out.println("로그인 실패...");
		return false;
		//DB에서 조회한 패스워드가 일치하는지 확인
	}
	
	
	public void join() {
		
		System.out.println("======회원가입 하기======");
		System.out.print("아이디 : ");
		String id = MyUtil.sc.nextLine();
		System.out.print("비밀번호 : ");
		String pwd = MyUtil.sc.nextLine();
		System.out.print("이름 : ");
		String name = MyUtil.sc.nextLine();
		System.out.print("생년월일6자리 : ");
		String birth = MyUtil.sc.nextLine();
		System.out.print("전화번호 : ");
		String phone = MyUtil.sc.nextLine();
		System.out.print("주소 : ");
		String address = MyUtil.sc.nextLine();
		
		//커넥션 연결
		Connection conn = OracleDB.getOracleConnection();
		//아이디 중복검사
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
					String sql = "SELECT * FROM MEMBER WHERE MEM_ID = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, id);
					rs = pstmt.executeQuery();
					if(rs.next()) {
						System.out.println("아이디 중복!!!");
						return ;
					}
					String sqlInsert = "INSERT INTO MEMBER(MEM_NO,MEM_ID,MEM_PWD, NAME, BIRTH, PHONE, ADDRESS) "
							+ "VALUES(MEMBER_NO_SEQ.NEXTVAL,?,?,?,TO_DATE(?),?,?)";
					pstmt = conn.prepareStatement(sqlInsert);
					pstmt.setString(1, id);
					pstmt.setString(2, pwd);
					pstmt.setString(3, name);
					pstmt.setString(4, birth);
					pstmt.setString(5, phone);
					pstmt.setString(6, address);
					
					int result = pstmt.executeUpdate();
					if(result == 1) {
						System.out.println("회원가입 성공!!!");
					}else {
						System.out.println("회원가입 실패!!!");
					}
					
		} catch (SQLException e) {
					e.printStackTrace();
		} finally {
					OracleDB.close(conn);
					OracleDB.close(pstmt);
					OracleDB.close(rs);
		}
		
	}
	
	
	public void joinUpdate() {
		System.out.println("=====회원정보 수정하기=====");
		//로그인 아닌 경우 회원정보가 수정 불가
		if(Member.LOGIN_USER_NO == 0) {
			System.out.println("로그인 한 유저만 회원정보를 수정할 수 있습니다.");
			return;
		} 
		System.out.print("수정 할 비밀번호 : ");
		String rePwd = MyUtil.sc.nextLine();
		System.out.print("수정 할 전화번호 : ");
		int rePhone = MyUtil.scInt();
		System.out.print("수정 할 주소 : ");
		String reAddress = MyUtil.sc.nextLine();
		//접속 얻기
		Connection conn = OracleDB.getOracleConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt2 = null;
		
		try {
			String sql = "SELECT * FROM MEMBER WHERE MEM_NO = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, LOGIN_USER_NO);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String pwd = rs.getString("MEM_PWD");
				int phone = rs.getInt("PHONE");
				String address = rs.getString("ADDRESS");			
			}
			
			String sqlUpdate = "UPDATE MEMBER SET MEM_PWD = ?, PHONE = ?, ADDRESS = ?"
					+ "WHERE MEM_NO = ?";
			pstmt2 = conn.prepareStatement(sqlUpdate);
			pstmt2.setString(1, rePwd);
			pstmt2.setInt(2, rePhone);
			pstmt2.setString(3, reAddress);
			pstmt2.setInt(4, LOGIN_USER_NO);
			
			
			int result = pstmt2.executeUpdate();
			if(result == 1) {
				System.out.println("회원정보 수정 완료!!!");
			}else {
				System.out.println("회원정보 수정 실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(pstmt2);
			OracleDB.close(rs);
		}
	}
	public void joinDelete() {
		System.out.println("=====회원정보 삭제하기=====");
		//로그인 아닌 경우 회원정보가 수정 불가
		if(Member.LOGIN_USER_NO == 0) {
			System.out.println("로그인 한 유저만 회원정보를 수정할 수 있습니다.");
			return;
		} 
		
		System.out.print("정말 삭제하시겠습니까? 1 : 네. 2 : 아니요.");
		int x = MyUtil.scInt();
		if(x == 2) {
			return;
		}
		else if(x == 1){
			System.out.println("삭제를 시작하겠습니다.");
			System.out.print("비밀번호를 입력하세요. : ");
			String pwd = MyUtil.sc.nextLine();
			//접속 얻기
			Connection conn = OracleDB.getOracleConnection();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			PreparedStatement pstmt2 = null;
			try {
				String sql = "SELECT * FROM MEMBER WHERE MEM_NO = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, LOGIN_USER_NO);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					String selectPwd = rs.getString("MEM_PWD");
					if(selectPwd.equals(pwd)) {
						String sqlUpdate = "UPDATE MEMBER SET QUIT_YN = ?";
						pstmt2 = conn.prepareStatement(sqlUpdate);
						pstmt2.setString(1, "Y");
						int result = pstmt2.executeUpdate();
						if(result == 1) {
							System.out.println("회원삭제 완료!!!");
						}else {
							System.out.println("회운삭제 실패!!!");
						}
					
					}
				}	
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				OracleDB.close(conn);
				OracleDB.close(pstmt);
				OracleDB.close(pstmt2);
				OracleDB.close(rs);
			}
		}else {
			System.out.println("잘못 입력했습니다.");	
		} 
		
	}
}