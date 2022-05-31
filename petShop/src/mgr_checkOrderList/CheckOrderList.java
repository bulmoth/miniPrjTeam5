package mgr_checkOrderList;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracleDB.OracleDB;
import order.Order;
import util.MyUtil;



public class CheckOrderList {

	
	public void check() {
		while (true) {
			System.out.println("============================");
			System.out.println();
			System.out.println("1. 오늘의 주문서 확인하기");
			System.out.println("2. 오늘의 판매 수량 확인");
			System.out.println("3. 회원별 구매 수량 확인");
			System.out.println("4. 관리자 메뉴로 돌아가기");
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
				return;
			
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
			
//			2. SELECT 쿼리 날리기
			String sql = "SELECT mem_no, ord_no, ord_cnt, order_date FROM ORDER_TBL";
			String sql2 = "SELECT mem_no, ord_no, ord_cnt, order_date, PRICE * ord_cnt AS PRICE FROM ORDER_TBL JOIN PRODUCT USING(PRD_NO)";
			
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			
			
				try {
					pstmt = conn.prepareStatement(sql2);
					
					rs = pstmt.executeQuery();
					
					System.out.print("회원 번호");
					System.out.print(" | ");
					System.out.print("주문 번호");
					System.out.print(" | ");
					System.out.print("주문 품목");
					System.out.print(" | ");
					System.out.print("주문 총액");
					System.out.print(" | ");
					System.out.print("주문 날짜");
					System.out.println("\n-----------------------------");
					
					
					while(rs.next()) {
						
						int no = rs.getInt("mem_no"); //회원 번호
						int no2 = rs.getInt("ord_no"); //주문 번호
						int no3 = rs.getInt("ord_cnt"); //주문 수량
						int no4 = rs.getInt("PRICE");
						Date rs2 = rs.getDate("order_date"); //주문 날짜


						
						System.out.print(no);
						System.out.print(" | ");
						System.out.print(no2);
						System.out.print(" | ");
						System.out.print(no3);
						System.out.print(" | ");
						System.out.print(no4);
						System.out.print(" | ");
						System.out.print(rs2);
						System.out.println();		
						
						
						
					}
					System.out.println("오늘의 매출을 확인하시겠습니까? (Y/N)");
					
					String answer = MyUtil.sc.nextLine();
					if("Y".equalsIgnoreCase(answer)) {
					checkSale2();
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
					OracleDB.close(conn);
					OracleDB.close(pstmt);
					OracleDB.close(rs);
					
					
				}
						
				
		}//checkSale
	
//	
	
	//오늘의 총 매출액
	public void checkSale2() {
		
				
		
		System.out.println("======== 오늘의 매출액을 확인합니다.  ========");
		
		Connection conn = OracleDB.getOracleConnection();
				
		
		String sql = "SELECT SUM((ORDER_TBL.ORD_CNT)*(PRODUCT.PRICE)) AS TOTALPRICE FROM ORDER_TBL, PRODUCT WHERE ORDER_TBL.PRD_NO = PRODUCT.PRD_NO";
				
		
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		
			try {
				pstmt = conn.prepareStatement(sql);
				
				rs = pstmt.executeQuery();
				
				System.out.print("오늘의 총 매출액은 ::  ");

				
				
				while(rs.next()) {
					

					int no = rs.getInt("TOTALPRICE"); //회원 번호

					
					System.out.print(no + "원 입니다.");
				
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
		
		String sql = "SELECT SUM(ORD_CNT) AS COUNT FROM ORDER_TBL WHERE TO_DATE(ORDER_DATE) = ?";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, inputDate);
			rs = pstmt.executeQuery();
			
			
			if(rs.next()) {
				int totalCount = rs.getInt("COUNT");
				
				
				System.out.println(inputDate + " 일의 총 주문 수량은" + totalCount + " 입니다");
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
		String sql = "SELECT ord_cnt FROM ORDER_TBL WHERE MEM_NO = ?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, inputMember);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int totalCount = rs.getInt("ORD_CNT");
				
				System.out.println(inputMember + " 번 회원의 총 주문 수량은 " + totalCount + " 입니다.");
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
