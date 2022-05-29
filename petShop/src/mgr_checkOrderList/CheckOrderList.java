package mgr_checkOrderList;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracleDB.OracleDB;
import util.MyUtil;




public class CheckOrderList {

	
	public void check() {
		while (true) {
			System.out.println("============================");
			System.out.println();
			System.out.println("1. 오늘의 주문서 추가하기");
			System.out.println("2. 오늘의 매출액 확인");
			System.out.println("3. 회원별 구매액 확인");
			System.out.println();
			System.out.println("============================");
			
			
			int n = MyUtil.scInt();
			

			switch (n) {
			case 1:
				checkSale(); break;
			case 2:
				checkAmount();	break;
			case 3:
				checkMemberAmount();	break;
			case 4:
				
				break;
			
			default:
				System.out.println("다시 선택하세요");

			}
		} // while
	}

	
	
	
	//오늘의 주문서 가져오기 
	public void checkSale() {
		
				
		//안내 문구 출력
		//입력 받기 ( 제목, 내용)
		System.out.println("======== 오늘의 주문을 확인합니다.  ========");
		
		Connection conn = OracleDB.getOracleConnection();
		
//		2. SELECT 쿼리 날리기
		String sql = "INSERT INTO PETSHOP_SALES(PS_NO, O_NO, O_PRICE)"
				+ "SELECT 주문서테이블.넘버, 주문서테이블.주문번호, 주문서테이블.주문금액 FROM 주문서테이블";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		
		
			try {
				pstmt = conn.prepareStatement(sql);
				
				rs = pstmt.executeQuery();
				
				System.out.print("정산 번호");
				System.out.print(" | ");
				System.out.print("주문 번호");
				System.out.print(" | ");
				System.out.print("주문 금액");
				System.out.print(" | ");
				System.out.print("주문 날짜");
				System.out.println("\n-----------------------------");
				
				
				while(rs.next()) {
					
					int no = rs.getInt("PS_NO"); //게시글 번호
					int no2 = rs.getInt("O_NO"); //주문 번호
					int no3 = rs.getInt("O_PRICE"); //주문 금액
					Date rs2 = rs.getDate("O_DATE"); //주문 날짜
					
					
					System.out.print(no);
					System.out.print(" | ");
					System.out.print(no2);
					System.out.print(" | ");
					System.out.print(no3);
					System.out.print(" | ");
					System.out.print(rs2);
					System.out.println();		
				
				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				OracleDB.close(conn);
				OracleDB.close(pstmt);
				OracleDB.close(rs);
				
				
			}
					
			
	}//checkSale
	
	
	
	//오늘의 총 매출액 확인
	public void checkAmount() {
		
		System.out.print("조회할 날짜 입력 yy/mm/dd (예시:22/05/27) : ");
		String inputDate = MyUtil.sc.nextLine();
		
		Connection conn = OracleDB.getOracleConnection();
		
		String sql = "SELECT SUM(O_PRICE) 오늘총매출 FROM PETSHOP_SALES WHERE TO_CHAR(O_DATE,'YY/MM/DD')= ? ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, inputDate);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String totalPrice = rs.getString("오늘총매출");
				
				System.out.println(inputDate + " 일의 ");
				System.out.println("총 매출액은 : " + totalPrice + " 원 입니다.");
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(rs);
			
			
		}
		
		
		
	}//checkAmount
	
	
	
	//회원번호별 판매 금액 체크
	public void checkMemberAmount() {
		
		System.out.print("조회할 회원 번호 입력 : ");
		int inputMember = MyUtil.scInt();
		
		Connection conn = OracleDB.getOracleConnection();
		//넘버에 봐서 회원번호로할지 물건번호로 할지 체크
		String sql = "SELECT PS_DATE, SUM(O_PRICE) 회원총구매액 FROM PETSHOP_SALES WHERE O_NO = ?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, inputMember);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int totalPrice = rs.getInt("회원총구매액");
				
				System.out.println(inputMember + " 번 회원의 ");
				System.out.println("총 구매액은 : " + totalPrice + " 원 입니다.");
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(rs);
		}		
		
		
	}//checkMemberAmount
	
	
	
	
}	
