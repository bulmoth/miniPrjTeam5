package cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Random;

import main.CustomerMain;
import member.Member;
import oracleDB.OracleDB;
import util.MyUtil;

public class Cart {

	Member m = new Member();
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection conn = null;
	int prdNo[];

	public void cartShow() {

		System.out.println("─────────────────────────────────");
		System.out.println(); 
		System.out.println("        장바구니 페이지");
		System.out.println("        [1] 장바구니 조회");
		System.out.println("        [2] 장바구니 수정");
		System.out.println("        [3] 주문하기");
		System.out.println("        [4] 메인 메뉴로 돌아가기");
		System.out.println();
		System.out.println("─────────────────────────────────");	
		System.out.print("        선택 : ");
		int selectNum = MyUtil.sc.nextInt();
		MyUtil.sc.nextLine(); //엔터키 처리


		switch(selectNum) {
		case 1 : //장바구니 조회 
			cartSelect();
			break;
		case 2 : //장바구니 수정
			cartUpdate();
			break;
		case 3 : //주문하기
			orderInsert();
			break;
		case 4 : //메인 메뉴로 돌아가기
			System.out.println("메인 메뉴로 돌아갑니다");
			new CustomerMain().CustomMain();
			break;
		default : System.out.println("유효한 메뉴가 아닙니다 다시 선택해주세요!"); cartShow();

		}
	}





	public void cartSelect(){

		System.out.println("장바구니 리스트입니다.");
		System.out.println();
		System.out.println("---------------------");
		System.out.println();

		//db 연결
		conn = OracleDB.getOracleConnection();

		String sql = "SELECT CART_NO, PRD_NO, COUNT, PRD_NAME, PRICE FROM CART INNER JOIN PRODUCT USING (PRD_NO) WHERE MEM_NO = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, m.LOGIN_USER_NO);
			rs = pstmt.executeQuery();

			System.out.print("장바구니 번호");
			System.out.print(" | ");
			System.out.print("상품 번호");
			System.out.print(" | ");
			System.out.print("상품명");
			System.out.print(" | ");
			System.out.print("수량");
			System.out.print(" | ");
			System.out.print("가격");
			System.out.print("\n-------------------------------\n");

			while(rs.next()) {
				int cartNo = rs.getInt("CART_NO");
				prdNo[1] = rs.getInt("PRD_NO");			// 상품 번호
				String prdNm = rs.getString("PRD_NAME");	// 상품명
				int cnt = rs.getInt("COUNT");				// 수량
				int pr = rs.getInt("PRICE");				// 가격

				System.out.print(cartNo);
				System.out.print(" | ");
				System.out.print(prdNo);
				System.out.print(" | ");
				System.out.print(prdNm);
				System.out.print(" | ");
				System.out.print(cnt);
				System.out.print(" | ");
				System.out.print(pr);
				System.out.println();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {

		}//반납


	}

	public void cartUpdate() {

		System.out.println("─────────────────────────────────");
		System.out.println(); 
		System.out.println("       장바구니 수정");
		System.out.println("       [1] 수량 수정");
		System.out.println("       [2] 상품 삭제");
		System.out.println("       [3] 장바구니 초기화");
		System.out.println("       [4] 장바구니로 돌아가기");
		System.out.println();
		System.out.println("─────────────────────────────────");

		System.out.print("        선택 : ");
		int selectNum = MyUtil.sc.nextInt();
		MyUtil.sc.nextLine(); 


		switch(selectNum) {
		case 1 : //수량 수정
			cntUpdate();
			break;
		case 2 : //상품 삭제
			prdDelete();
			break;
		case 3 : //장바구니 초기화
			cartDelete();
			break;
		case 4 : //장바구니로 돌아가기
			System.out.println("장바구니로 돌아갑니다");
			cartShow();
			break;
		default : System.out.println("유효한 메뉴가 아닙니다 다시 선택해주세요!"); cartUpdate();


		}
	}


	private void cntUpdate() {

		conn = OracleDB.getOracleConnection();

		try {

			System.out.println("─────────────────────────────────");
			System.out.println(); 
			System.out.println("          수량 수정 메뉴");
			System.out.println(); 
			System.out.println("─────────────────────────────────");

			System.out.println("  수량을 수정할 상품을 선택해주세요");
			System.out.print("        장바구니 번호 :");

			int cartNo = MyUtil.sc.nextInt();
			MyUtil.sc.nextLine(); //엔터키 처리

			System.out.println("  수량을 입력해주세요");
			System.out.print("			 수량:  ");

			int cnt = MyUtil.sc.nextInt();
			MyUtil.sc.nextLine(); //엔터키 처리

			String sql = "UPDATE CART SET COUNT = ? WHERE CART_NO = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cnt );
			pstmt.setInt(2, cartNo);
			int update = pstmt.executeUpdate();


			//삭제 확인
			if(update == 1) {
				System.out.println("정상적으로 상품을 수정하였습니다");
			}else {
				System.out.println("상품을 수정하지 못했습니다. 장바구니 번호를 다시 확인해주세요");

			}

		}catch (SQLException e){
			System.out.println("수정 중 문제 발생");
		}catch(InputMismatchException e) {
			MyUtil.sc.nextLine();
			System.out.println("수정 중 문제 발생 오류 양식을 확인해주세요");
		}finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(rs);
		}

		System.out.println("장바구니 페이지로 돌아갑니다");




	}


	private void prdDelete() {

		conn = OracleDB.getOracleConnection();

		try {

			System.out.println("─────────────────────────────────");
			System.out.println(); 
			System.out.println("          상품 빼기 ");
			System.out.println(); 
			System.out.println("─────────────────────────────────");

			System.out.println("  장바구니에서 뺄 상품을 선택해주세요");
			System.out.print("        장바구니 번호 :");

			int cartNo = MyUtil.sc.nextInt();
			MyUtil.sc.nextLine(); //엔터키 처리

			String sql = "DELETE FROM CART WHERE CART_NO = ? AND MEM_NO = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cartNo);
			pstmt.setInt(2, m.LOGIN_USER_NO);
			int delPrd = pstmt.executeUpdate();


			//상품제거 
			if(delPrd == 1) {
				System.out.println("정상적으로 상품을 뺐습니다");
			}else {
				System.out.println("상품을 빼지 못했습니다. 장바구니 번호를 다시 확인해주세요");

			}

		}catch (SQLException e){
			System.out.println("수정 중 문제 발생");
		}catch(InputMismatchException e) {
			MyUtil.sc.nextLine();
			System.out.println("수정 중 문제 발생 오류 양식을 확인해주세요");
		}finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(rs);
		}

		System.out.println("장바구니 페이지로 돌아갑니다");
	}

	private void cartDelete() {

		conn = OracleDB.getOracleConnection();

		try {

			System.out.println("─────────────────────────────────");
			System.out.println(); 
			System.out.println("          장바구니 초기화 ");
			System.out.println(); 
			System.out.println("─────────────────────────────────");


			String sql = "DELETE FROM CART WHERE MEM_NO = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, m.LOGIN_USER_NO);
			int delPrd = pstmt.executeUpdate();

			System.out.println("delPrd : " + delPrd);
			//상품제거 
			if(delPrd == 1) {
				System.out.println("정상적으로 상품을 뺐습니다");
			}else {
				System.out.println("상품을 빼지 못했습니다. 장바구니 번호를 다시 확인해주세요");

			}

		}catch (SQLException e){
			System.out.println("수정 중 문제 발생");
		}catch(InputMismatchException e) {
			MyUtil.sc.nextLine();
			System.out.println("수정 중 문제 발생 오류 양식을 확인해주세요");
		}finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(rs);
		}

		System.out.println("장바구니 페이지로 돌아갑니다");
	}

	private void orderInsert() {


		conn = OracleDB.getOracleConnection();


		System.out.println("─────────────────────────────────");
		System.out.println(); 
		System.out.println("         주문하기 ");
		System.out.println(); 
		System.out.println("─────────────────────────────────");


		System.out.print("       주문하시겠습니까? (Y/N):");


		if (equals('y') || equals('Y')) {

			// 운송장번호 생성
			Random random = new Random();
			int createNum = 0;
			String ranNum = "";
			int letter 	  = 9;
			String waybNo = "";

			for (int i = 0; i<letter; i++) {


				createNum = random.nextInt(9);
				ranNum = Integer.toString(createNum);
				waybNo += ranNum;
			}

			int ordCnt = 1;
			
			for(int i = 0; i < prdNo.length; i++) {
				String sql = "INSERT INTO ORDER_TBL(ORD_NO, MEM_NO, PRD_NO, ORD_CNT, ORDER_DATE, WAYBILL_NO, ORD_STATUS, DEL_DATE)"
						+ "VALUES(ORD_NO_SEQ.NEXTVAL, ?, ?, ?, SYSDATE, ?, '주문완료', SYSDATE+3)";
				PreparedStatement pstmt = null;
				
				try {
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, m.LOGIN_USER_NO);
					pstmt.setInt(2, prdNo[i]);
					pstmt.setInt(3, ordCnt);
					pstmt.setInt(4, Integer.parseInt(waybNo));
					int  result = pstmt.executeUpdate();
					if(result == 1) {
						System.out.println("주문 성공 !");
					}else {
						System.out.println("주문 실패 ..");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					OracleDB.close(conn);
					OracleDB.close(pstmt);
					OracleDB.close(rs);
				}
			}
		}
	}
}


