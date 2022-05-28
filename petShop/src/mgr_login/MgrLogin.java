package mgr_login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracleDB.OracleDB;
import util.MyUtil;

public class MgrLogin {
	
	public static String mgrIdNow;
	
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
				mgrIdNow = id;
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
		while(isCorrect) {
			System.out.println("======== 회원 관리 페이지 ========");
			System.out.println();
			System.out.println("회원 관리 페이지 입니다.");
			System.out.println("원하시는 기능을 선택하십시오.");
			System.out.println();
			System.out.println("1. 회원 조회 및 관리");
			System.out.println("2. 오늘의 회원 변동");
			System.out.println("그 외 : 나가기");
			int n = MyUtil.scInt();
			
			switch(n) {
			case 1: memberList(); break;
			case 2: todayMem(); break;
			default: System.out.println("상위 메뉴로 돌아갑니다."); isCorrect = false;
			}
		}
		//1과 2 이외의 것이 선택되었을 때 while 문 탈출 및 리턴
		return;
	}//memberMng
	
	
	private void memberList() {
		//회원 조회
		System.out.println("===== 회원 목록 조회 =====");
		System.out.println();
		//회원 번호
		int mem_no;
		//연결 얻기
		Connection conn = OracleDB.getOracleConnection();
		//회원 상세 조회
		String sql = "SELECT RPAD(TO_CHAR(MEM_NO), 5, ' ') MEM_NO, LPAD(MEM_ID, 22,' ') MEM_ID, LPAD(MEM_PWD, 20,' ') MEM_PWD,"
				+ " LPAD(NAME, 22,' ') NAME, LPAD(TO_CHAR(BIRTH), 10, ' ') BIRTH, LPAD(PHONE, 15, ' ') PHONE,"
				+ " LPAD(ADDRESS, 23, ' ') ADDRESS, LPAD(TO_CHAR(POINT), 10, ' ') POINT,"
				+ " LPAD(TO_CHAR(ENROLL_DATE), 11, ' ') ENROLL_DATE, LPAD(QUIT_YN, 4, ' ') QUIT_YN "
				+ "FROM MEMBER ORDER BY ENROLL_DATE DESC";
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int select = 0;
			while(select != -1) {
				System.out.println("회원번호 |        아이디        |        비밀번호       |         이름        |  생년월일  |     전화번호    |         주소         |    포인트   |  회원가입일  | 탈퇴여부");
				while(rs.next()) {
					//회원 세부 데이터 출력 메소드
					memLook(rs);
				}
				System.out.print("탈퇴시킬 회원 번호를 선택해 주십시오 (나가기 = -1) : ");
				select = MyUtil.scInt();
				//메뉴 한번 이상 선택 후 -1을 입력으로 받았을 때
				if(select == -1) break;
				//탈퇴하지 않은 사람 중에서 조회
				String sql2 = "SELECT * FROM MEMBER WHERE MEM_NO = ? AND QUIT_YN = 'N'";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setInt(1, select);
				rs2 = pstmt2.executeQuery();
				if(rs2.next()) {
					mem_no = rs2.getInt("MEM_NO");
					//탈퇴 메소드
					memWithdraw(mem_no);
				}else {
					//커서가 가리키는게 없을 때
					System.out.println();
					System.out.println("해당 회원은 이미 탈퇴했거나 존재하지 않습니다.");
					System.out.println("상위 메뉴로 돌아갑니다.");
					return;
				}
			}
			//맨 처음으로 -1을 입력으로 받았을 때
			System.out.println();
			System.out.println("상위 메뉴로 돌아갑니다.");
			return;
			
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
		//회원 정보 출력 메소드
		try {
			String mem_no = rs.getString("MEM_NO");
			String mem_id = rs.getString("MEM_ID");
			String mem_pwd = rs.getString("MEM_PWD");
			String name = rs.getString("NAME");
			String birth = rs.getString("BIRTH");
			String phone = rs.getString("PHONE");
			String address = rs.getString("ADDRESS");
			String point = rs.getString("POINT");
			String enroll_date = rs.getString("ENROLL_DATE");
			String quit_yn = rs.getString("QUIT_YN");
			System.out.println(mem_no + mem_id + mem_pwd + name + birth + phone + address + point + enroll_date + quit_yn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}//memLook
	
	
	private void memWithdraw(int mem_no) {
		//회원 강퇴 메소드
		System.out.println("탈퇴시키겠습니까? (y/n)");
		String answer = MyUtil.sc.nextLine();
		//소문자 y만 탈퇴시키도록 조치
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
		}else {
			//소문자 y 이외의 문자가 들어왔을 경우
			System.out.println("탈퇴 처리에 실패하였습니다.");
			System.out.println("상위 메뉴로 돌아갑니다.");
			return;
		}
		
	}//memWithdraw
	
	
	
	private void todayMem() {
		//오늘의 회원 변동 조회
		System.out.println("===== 오늘의 회원 변동 사항 =====");
		System.out.println();
		int enrollMem = 0;
		int withdrawMem = 0;
		
		//연결 얻기
		Connection conn = OracleDB.getOracleConnection();
		String sql = "SELECT ENROLL_DATE, QUIT_YN FROM MEMBER WHERE TO_CHAR(ENROLL_DATE)=TO_CHAR(SYSDATE) AND QUIT_YN = 'N'";
		String sql2 = "SELECT ENROLL_DATE, QUIT_YN FROM MEMBER WHERE TO_CHAR(ENROLL_DATE)=TO_CHAR(SYSDATE) AND QUIT_YN = 'Y'";
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				enrollMem++;
			}
			pstmt2 = conn.prepareStatement(sql2);
			rs2 = pstmt2.executeQuery();
			while(rs2.next()) {
				withdrawMem++;
			}
			System.out.println("오늘 가입한 회원 : " + enrollMem);
			System.out.println("오늘 탈퇴한 회원 : " + withdrawMem);
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(rs);
			OracleDB.close(pstmt2);
			OracleDB.close(rs2);
		}
		
	}//todayMem

}//class
