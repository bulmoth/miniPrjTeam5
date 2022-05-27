package adopt_reg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracleDB.OracleDB;
import util.MyUtil;

public class AdoptReg {
	
	public void adoptReg(){
		
		//플래그 세우기
		boolean isQuit = false;
		
		while(!isQuit) {
			
			//입양 등록
			System.out.println("========== 입양 ==========");
			System.out.println();
			System.out.println("입양 관련 페이지 입니다.");
			System.out.println("원하시는 기능을 선택하십시오.");
			System.out.println();
			System.out.println("1. 입양 대기 목록 조회");
			System.out.println("2. 입양 완료 목록 조회");
			System.out.println("3. 입양 등록");
			System.out.println("그 외 : 나가기");
			
			int select = MyUtil.scInt();
			switch(select) {
			case 1:
				adoptList(); break;
			case 2:
				complete(); break;
			case 3: 
				register(); break;
				default : System.out.println("상위 메뉴로 돌아갑니다."); isQuit = true;
			}
			
		}
		//선택지 이외의 것이 선택되었을 때 while 문 탈출 및 리턴
		return;
		
	}//adoptReg
	
	private void adoptList() {
		//입양 대기 목록 조회
		System.out.println("===== 현재 등록된 입양 대기 목록 =====");
		Connection conn = OracleDB.getOracleConnection();
		String sql = "SELECT RPAD(TO_CHAR(ADT_NO), 6, ' ') ADT_NO, LPAD(MEM_ID, 21, ' ') MEM_ID, "
				+ "LPAD(TYPE, 5, ' ') TYPE, LPAD(ANI_NAME, 25, ' ') ANI_NAME "
				+ "FROM ADOPT WHERE ADOPT = 'N' ORDER BY ADT_NO ASC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println("입양번호 |        아이디        |  종  |          동물 이름         ");
			while(rs.next()) {
				String adt_no = rs.getString("ADT_NO");
				String mem_no = rs.getString("MEM_ID");
				String type = rs.getString("TYPE");
				String ani_name = rs.getString("ANI_NAME");
				System.out.println(adt_no + mem_no + type + ani_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			OracleDB.close(conn);
			OracleDB.close(rs);
			OracleDB.close(pstmt);
		}
		
	}//adoptList
	
	private void complete() {
		//입양 완료 목록 조회
		System.out.println("========== 입양 완료 목록 ==========");
		Connection conn = OracleDB.getOracleConnection();
		String sql = "SELECT RPAD(TO_CHAR(ADT_NO), 6, ' ') ADT_NO, LPAD(ADM_ID, 21, ' ') ADM_ID, "
				+ "LPAD(TYPE, 5, ' ') TYPE, LPAD(ANI_NAME, 25, ' ') ANI_NAME "
				+ "FROM ADOPT WHERE ADOPT = 'Y' ORDER BY ADT_NO ASC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println("입양번호 |        입양자        |  종  |          동물 이름         ");
			while(rs.next()) {
				String adt_no = rs.getString("ADT_NO");
				String adm_id = rs.getString("ADM_ID");
				String type = rs.getString("TYPE");
				String ani_name = rs.getString("ANI_NAME");
				System.out.println(adt_no + adm_id + type + ani_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			OracleDB.close(conn);
			OracleDB.close(rs);
			OracleDB.close(pstmt);
		}
	}//complete
	
	
	private void register() {
		//입양 등록
		
	}//register
	

}//class
