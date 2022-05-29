package adopt_reg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import member.Member;
import oracleDB.OracleDB;
import util.MyUtil;

public class AdoptRegMem {
	
	public void adoptRegMem() {
		
		if(Member.LOGIN_USER_NO == 0) {
			System.out.println("로그인한 회원만 이용하실 수 있습니다.");
			return;
		}
		
		//플래그 세우기
		boolean isQuit = false;
		
		while(!isQuit) {
			
			//입양 등록
			System.out.println("========== 입양(회원) ==========");
			System.out.println();
			System.out.println("입양 관련 페이지 입니다.");
			System.out.println("원하시는 기능을 선택하십시오.");
			System.out.println();
			System.out.println("1. 입양 대기 목록 조회 및 입양 신청");
			System.out.println("2. 입양 등록");
			System.out.println("그 외 : 나가기");
			
			int select = MyUtil.scInt();
			switch(select) {
			case 1:
				adoptList(); break;
			case 2: 
				register(); break;
				default : System.out.println("상위 메뉴로 돌아갑니다."); isQuit = true;
			}
			
		}
		//선택지 이외의 것이 선택되었을 때 while 문 탈출 및 리턴
		return;
		
	}
	
	private void adoptList() {
		
		//입양 대기 목록 조회
		System.out.println("===== 현재 등록된 입양 대기 목록 =====");
		Connection conn = OracleDB.getOracleConnection();
		String sql = "SELECT ADT_NO, LPAD(MEM_ID, 25, ' ') MEM_ID, "
				+ "LPAD(TYPE, 5, '  ') TYPE, LPAD(ANI_NAME, 20, ' ') ANI_NAME "
				+ "FROM ADOPT WHERE ADOPT = 'N' ORDER BY ADT_NO ASC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println("입양번호 |        아이디        |  종  |          동물 이름         ");
			while(rs.next()) {
				String adt_no = rs.getString("ADT_NO");
				String mem_id = rs.getString("MEM_ID");
				String type = rs.getString("TYPE");
				String ani_name = rs.getString("ANI_NAME");
				System.out.println(adt_no + mem_id + "    " + type + ani_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			OracleDB.close(conn);
			OracleDB.close(rs);
			OracleDB.close(pstmt);
		}
				
		request();
	}//adoptList

	private void request() {
		
		//회원 입양 신청
		System.out.println("========== 입양 신청 ==========");
		String mem_id = null;
		System.out.print("입양하실 동물의 입양번호를 입력해주세요 (없는 번호 : 나가기) : ");
		int adt_no = MyUtil.scInt();
		Connection conn = OracleDB.getOracleConnection();
		String sql = "UPDATE ADOPT SET ADM_ID = ?, ADOPT = 'Y' WHERE ADT_NO = ?";
		String sql2 = "SELECT MEM_NO, MEM_ID FROM MEMBER WHERE MEM_NO = ?";
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt2 = conn.prepareStatement(sql2);
			pstmt2.setInt(1, Member.LOGIN_USER_NO);
			rs = pstmt2.executeQuery();
			if(rs.next()) {
				mem_id = rs.getString("MEM_ID");
			}else {
				System.out.println("로그인 정보를 불러올 수 없습니다.");
				return;
			}
			pstmt.setString(1, mem_id);
			pstmt.setInt(2, adt_no);
			int result = pstmt.executeUpdate();
			if(result == 1) {
				System.out.println("입양이 성공적으로 이루어졌습니다.");
				System.out.println("상위 메뉴로 돌아갑니다.");
				return;
			}else {
				System.out.println("입양 처리에 실패하였습니다.");
				System.out.println("상위 메뉴로 돌아갑니다.");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(pstmt2);
			OracleDB.close(rs);
		}
		
	}//request

	private void register() {
		
		//입양 등록
		System.out.println("======== 입양 등록(회원) ========");
		String mem_id = null;
		Connection conn = OracleDB.getOracleConnection();
		String sql = "INSERT INTO ADOPT(ADT_NO, MEM_ID, TYPE, ANI_NAME) VALUES(ADOPT_SEQ.NEXTVAL, ?, ?, ?)";
		String sql2 = "SELECT MEM_NO, MEM_ID FROM MEMBER WHERE MEM_NO = ?";
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt2 = conn.prepareStatement(sql2);
			pstmt2.setInt(1, Member.LOGIN_USER_NO);
			rs = pstmt2.executeQuery();
			if(rs.next()) {
				mem_id = rs.getString("MEM_ID");
			}else {
				System.out.println("로그인 정보를 불러올 수 없습니다.");
				return;
			}
			System.out.println("입양 등록을 시작합니다.");
			System.out.print("동물 이름을 입력하세요 : ");
			String ani_name = MyUtil.sc.nextLine();
			pstmt.setString(1, mem_id);
			pstmt.setString(3, ani_name);
			System.out.print("동물 종을 입력하세요(개 : D|고양이 : C|그 외 : 공란) : ");
			String type = MyUtil.sc.nextLine();
			pstmt.setString(2, type);
			int result = pstmt.executeUpdate();
			if(result == 1) {
				System.out.println("등록을 성공적으로 마쳤습니다.");
				System.out.println("상위 메뉴로 돌아갑니다.");
				return;
			}else {
				System.out.println("등록에 실패하였습니다.");
				System.out.println("상위 메뉴로 돌아갑니다.");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(pstmt2);
			OracleDB.close(rs);
		}
		
	}//register


}//class
