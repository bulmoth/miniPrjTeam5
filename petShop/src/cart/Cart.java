package cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import main.CustomerMain;
import member.Member;
import oracleDB.OracleDB;
import util.MyUtil;

public class Cart {

	Scanner scan = new Scanner(System.in);
	Member m = new Member();
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection conn = null;
	int arrPrd[]; // 장바구니 조회 시 prdNo 값들을 arrPrd 배열에 담아야 함.
	int arrCnt[]; // 장바구니 조회 시 각각 상품별 수량 값들을 arrCnt 배열에 담아야 함.
	int cnt;	  // 장바구니 데이터 개수 담을 변수
	
	// 장바구니 메인 메서드
	public void cartMain() {

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
			// 장바구니 데이터만큼 주문테이블에 데이터 insert를 위한 메서드
			cartCnt();	
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
		default : System.out.println("유효한 메뉴가 아닙니다 다시 선택해주세요!"); cartMain();

		}
	}
	
	// 장바구니 조회 메서드
	public void cartSelect(){

		System.out.println("장바구니 리스트입니다.");
		System.out.println();
		System.out.println("---------------------");
		System.out.println();

		//db 연결
		conn = OracleDB.getOracleConnection();

		String sql = "SELECT CART_NO, PRD_NO, COUNT, PRD_NAME, PRICE * COUNT as PRICE FROM CART INNER JOIN PRODUCT USING (PRD_NO) WHERE MEM_NO = ?";
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
			
			// 배열 객체 생성
			arrPrd = new int[cnt];
			arrCnt = new int[cnt];
			int i = 0;
			
			while(rs.next()) {
				
				int cartNo = rs.getInt("CART_NO");		// 장바구니 번호
				int prdNo = rs.getInt("PRD_NO");		// 상품 번호
				String prdNm = rs.getString("PRD_NAME");// 상품명
				int qty = rs.getInt("COUNT");			// 수량
				int pr = rs.getInt("PRICE");			// 가격
				
				// 배열에 각각 상품 데이터를 담는다.
				arrPrd[i] = prdNo; 
				arrCnt[i] = qty; 
				i++;
				 
				System.out.print(cartNo);
				System.out.print(" | ");
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
		// 조회 후 장바구니 메인으로 이동
		cartMain();
	}

	// 장바구니 데이터 개수 파악을 위한 메서드
	public void cartCnt() {
		//db 연결
		conn = OracleDB.getOracleConnection();
		String query = "SELECT COUNT(1) as cnt FROM CART WHERE MEM_NO = ?";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, m.LOGIN_USER_NO);
			rs = pstmt.executeQuery();

			while(rs.next()) {
				cnt = rs.getInt("cnt");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(rs);
		}
	}
	
	// 장바구니 데이터 수정 메서드
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
			cartMain();
			break;
		default : System.out.println("유효한 메뉴가 아닙니다 다시 선택해주세요!"); cartUpdate();


		}
	}

	// 장바구니 상품 수량 수정 메서드
	private void cntUpdate() {

		conn = OracleDB.getOracleConnection();

		try {

			System.out.println("─────────────────────────────────");
			System.out.println(); 
			System.out.println("          수량 수정 메뉴");
			System.out.println(); 
			System.out.println("─────────────────────────────────");

			System.out.println("    수량을 수정할 상품을 선택해주세요");
			System.out.print("           장바구니 번호 :");

			int cartNo = MyUtil.sc.nextInt();
			MyUtil.sc.nextLine(); //엔터키 처리

			System.out.println("    수량을 입력해주세요");
			System.out.print("			    수량:  ");

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
		cartMain();
	}

	
	// 상품 단건 제거 메서드
	private void prdDelete() {

		conn = OracleDB.getOracleConnection();

		try {

			System.out.println("─────────────────────────────────");
			System.out.println(); 
			System.out.println("          상품 빼기 ");
			System.out.println(); 
			System.out.println("─────────────────────────────────");

			System.out.println("  장바구니에서 뺄 상품을 선택해주세요");
			System.out.print("          장바구니 번호 :");

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
		cartMain();
	}

	// 장바구니 전체 초기화
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
			pstmt.executeUpdate();
			
			System.out.println("정상적으로 초기화가 되었습니다");

		}catch (Exception e){
			System.out.println("수정 중 문제 발생");
		}finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(rs);
		}

		System.out.println("장바구니 페이지로 돌아갑니다");
		cartMain();
	}
	
	
	// 주문테이블 데이터 INSERT 메서드
	private void orderInsert() {
		
		conn = OracleDB.getOracleConnection();
		
		System.out.println("─────────────────────────────────");
		System.out.println(); 
		System.out.println("            주문하기 ");
		System.out.println(); 
		System.out.println("─────────────────────────────────");


		System.out.print("        주문하시겠습니까? (Y/N):");
		String eq = scan.nextLine();
		
		if (eq.equals("Y") || eq.equals("y")) {

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
			
			// 장바구니 데이터만큼 INSERT구문 반복
			for(int i = 0; i < cnt; i++) {
				
				String sql = "INSERT INTO ORDER_TBL(ORD_NO, MEM_NO, PRD_NO, ORD_CNT, ORDER_DATE, WAYBILL_NO, ORD_STATUS, DEL_DATE)"
						+ "VALUES(ORD_NO_SEQ.NEXTVAL, ?, ?, ?, TO_CHAR(SYSDATE, 'yyyyMMdd'), ?, '주문완료', TO_CHAR(SYSDATE+3, 'yyyyMMdd'))";
				PreparedStatement pstmt = null;

				try {
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, m.LOGIN_USER_NO);
					pstmt.setInt(2, arrPrd[i]);  // prdNo 데이터 하나씩 INSERT 처리하기 위해 배열사용
					pstmt.setInt(3, arrCnt[i]);  // 각각 상품마다 정해진 수량 INSERT 처리하기 위해 배열사용
					pstmt.setInt(4, Integer.parseInt(waybNo));  // 운송장번호 String -> int 형변환
					pstmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			System.out.println("주문 성공 !");
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(rs);
		} else if(eq.equals("N") || eq.equals("n")) {
			System.out.println("장바구니 페이지로 돌아갑니다");
			cartMain();
		} else {
			System.out.println("Y/N만 입력해주세요");
			return;
		}
		cartRemove();
	}
	
	
	// 주문 후 장바구니 테이블 초기화 시켜주기 위한 메서드
	// cartDelete를 사용하지 않고 따로 생성한 이유는 sysout 출력문이 지저분해서 따로 처리했음
	public void cartRemove() {
		conn = OracleDB.getOracleConnection();

		try {
			String sql = "DELETE FROM CART WHERE MEM_NO = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, m.LOGIN_USER_NO);
			pstmt.executeUpdate();
		}catch (Exception e){
			System.out.println("장바구니 초기화 중 문제 발생");
		}finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(rs);
		}
	}
}


