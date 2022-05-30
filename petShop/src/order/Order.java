package order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import main.CustomerMain;
import member.Member;
import oracleDB.OracleDB;
import util.MyUtil;

public class Order {

	Scanner scan = new Scanner(System.in);
	Member m = new Member();
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection conn = null;
	public static int totalAmt;
	
	// 주문 메인 메서드
	public void orderMain() {

		System.out.println("─────────────────────────────────");
		System.out.println(); 
		System.out.println("        주문 페이지");
		System.out.println("        [1] 주문 조회");
		System.out.println("        [2] 주문 취소");
		System.out.println("        [3] 메인 메뉴로 돌아가기");
		System.out.println();
		System.out.println("─────────────────────────────────");	
		System.out.print("         선택 : ");
		int selectNum = MyUtil.sc.nextInt();
		MyUtil.sc.nextLine(); //엔터키 처리


		switch(selectNum) {
		case 1 : //주문 조회
			orderSelect();
			break;
		case 2 : //주문 취소
			orderDelete();
			break;
		case 3 : //메인 메뉴로 돌아가기
			System.out.println("메인 메뉴로 돌아갑니다");
			new CustomerMain().CustomMain();
			break;

		default : System.out.println("유효한 메뉴가 아닙니다 다시 선택해주세요!"); orderMain();

		}
	}

	// 주문 조회 메서드
	public void orderSelect(){

		System.out.println("주문 조회현황입니다.");
		System.out.println();
		System.out.println("---------------------");
		System.out.println();

		//db 연결
		conn = OracleDB.getOracleConnection();

		String sql = "SELECT PRD_NO, PRD_NAME, ORD_CNT, ORD_CNT * PRICE AS PRICE FROM ORDER_TBL JOIN PRODUCT USING(PRD_NO) WHERE MEM_NO = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, m.LOGIN_USER_NO);
			rs = pstmt.executeQuery();

			System.out.print("상품 번호");
			System.out.print(" | ");
			System.out.print("상품명");
			System.out.print(" | ");
			System.out.print("수량");
			System.out.print(" | ");
			System.out.print("가격");
			System.out.print("\n-------------------------------\n");

			while(rs.next()) {

				int prdNo = rs.getInt("PRD_NO");		// 상품 번호
				String prdNm = rs.getString("PRD_NAME");// 상품명
				int qty = rs.getInt("ORD_CNT");			// 수량
				int pr = rs.getInt("PRICE");			// 가격

				System.out.print(prdNo);
				System.out.print(" | ");
				System.out.print(prdNm);
				System.out.print(" | ");
				System.out.print(qty);
				System.out.print(" | ");
				System.out.print(pr);
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(rs);
		}
		orderTotal();
	}

	// 주문조회 중복 데이터 일괄처리 메서드
	public void orderTotal() {
		System.out.println("-------------------------------");

		//db 연결
		conn = OracleDB.getOracleConnection();

		String sql = "SELECT ORDER_DATE, WAYBILL_NO, ORD_STATUS, DEL_DATE, SUM(ORD_CNT * PRICE) AS TOTAL FROM ORDER_TBL "
				+ "JOIN PRODUCT USING(PRD_NO) "
				+ "WHERE MEM_NO = ? "
				+ "GROUP BY ORDER_DATE, WAYBILL_NO, ORD_STATUS, DEL_DATE";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, m.LOGIN_USER_NO);
			rs = pstmt.executeQuery();

			System.out.print("주문일자");
			System.out.print(" | ");
			System.out.print("운송장번호");
			System.out.print(" | ");
			System.out.print("주문상태");
			System.out.print(" | ");
			System.out.print("배송예정일");
			System.out.print(" | ");
			System.out.print("총 금액");
			System.out.print("\n-------------------------------\n");

			while(rs.next()) {

				String ordDate = rs.getString("ORDER_DATE");		// 주문일자
				int wbNo = rs.getInt("WAYBILL_NO");					// 운송장번호
				String ordStat = rs.getString("ORD_STATUS");		// 주문상태
				String delDate = rs.getString("DEL_DATE");			// 배송예정일
				int total = rs.getInt("TOTAL");						// 총 금액

				totalAmt = total;
				
				System.out.print(ordDate);
				System.out.print(" | ");
				System.out.print(wbNo);
				System.out.print(" | ");
				System.out.print(ordStat);
				System.out.print(" | ");
				System.out.print(delDate);
				System.out.print(" | ");
				System.out.print(total);
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(rs);
		}
		orderMain();
	}
	
	// 주문 취소 메서드
	public void orderDelete() {
		conn = OracleDB.getOracleConnection();

		System.out.println("─────────────────────────────────");
		System.out.println(); 
		System.out.println("          주문 취소");
		System.out.println(); 
		System.out.println("─────────────────────────────────");

		System.out.print("        주문 취소하시겠습니까? (Y/N):");
		String eq = scan.nextLine();
		
		// y 선택 시 주문 테이블 데이터 삭제
		if (eq.equals("Y") || eq.equals("y")) {
			try {
				String sql = "DELETE FROM ORDER_TBL WHERE MEM_NO = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, m.LOGIN_USER_NO);
				pstmt.executeUpdate();

				System.out.println("주문이 취소되었습니다.");

			} catch (Exception e){
				System.out.println("수정 중 문제 발생");
			}finally {
				OracleDB.close(conn);
				OracleDB.close(pstmt);
				OracleDB.close(rs);
			}

			System.out.println("주문 조회 페이지로 돌아갑니다");
			orderMain();
		} else if(eq.equals("N") || eq.equals("n")) {
			System.out.println("주문 조회 페이지로 돌아갑니다");
			orderMain();
		}
	}
}