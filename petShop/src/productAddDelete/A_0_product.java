package productAddDelete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import oracleDB.OracleDB;
import util.MyUtil;


public class A_0_product {
	
	
	private int selectNum;
	PreparedStatement pstmt = null;
    ResultSet rs = null;
	
	
	public void item0() {
		System.out.println("─────────────────────────────────");
		System.out.println(); 
		System.out.println("        상품등록 및 삭제");
		System.out.println("        [0] 상품현황");
		System.out.println("        [1] 상품등록");
		System.out.println("        [2] 상품삭제");
		System.out.println("        [3] 관리자 메뉴로 돌아가기");
		System.out.println();
		System.out.println("─────────────────────────────────");
		System.out.println("     원하시는 메뉴를 선택해주세요");
		System.out.print("        메뉴 : ");
		
		
		
		selectNum = MyUtil.sc.nextInt();
		
		switch(selectNum) {
		case 0 : //상품현황 
			item0_0();
			break;
		case 1 : //상품등록
			item0_1();
			break;
		case 2 : //상품삭제
			item0_2();
			break;
		case 3 : //관리자 메뉴로 돌아가기
			///
		default : System.out.println("유효한 메뉴가 아닙니다 다시 선택해주세요!"); item0();

	}

}
	
	
	//상품현황
	public void item0_0() {
		
		Connection conn = OracleDB.getOracleConnection();
		String sql = "SELECT * FROM PRODUCT_CATEGORY";
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println();
			System.out.println("      카테고리       상품번호          상품이름           상품설명           가격        재고");
			System.out.println("─────────────────────────────────────────────────────────────────────────────────────────");
			System.out.println();
			while(rs.next()) {
			 	 
				 String category = rs.getString("CATEGORY");
		    	 int prd_no = rs.getInt("PRD_NO");
		    	 String prd_name = rs.getString("PRD_NAME");
			     String description = rs.getString("DESCRIPTION");
			     int price = rs.getInt("PRICE");
		    	 int stock = rs.getInt("STOCK");
		    	  
		    	  
		    	
		         System.out.print("      "+category+"         ");
		         System.out.print(prd_no+"          ");
		         System.out.print(prd_name+"         ");
		         System.out.print(description+"        ");
		         System.out.print(price+"         ");
		         System.out.printf(stock+"        \n");

			}
			
			
			
			MyUtil.sc.nextLine();
		} catch (SQLException e) {
			System.out.println("상품 내역이 없습니다.");
		}
	
		System.out.println();
		System.out.println("───────────────────────────────");
		System.out.println(); 
		System.out.println("    이전메뉴 : 아무키나 눌러주세요");
		System.out.println(); 
		System.out.println("───────────────────────────────");
		MyUtil.sc.nextLine();
		item0();
		
	}

	
	//상품 등록
	    public void item0_1() {
	    	
	    	
	    	while(true) {
	    	System.out.printf("카테고리번호\n(1. 개사료, 2.고양이사료 3.개간식, 4.고양이간식 5. 장난감, 6.기타 숫자만 입력) : ");
	    	int cat_no = MyUtil.sc.nextInt();
	    	System.out.print("상품번호 : ");
	    	int prd_no = MyUtil.sc.nextInt();
	    	MyUtil.sc.nextLine();
	    	System.out.print("상품이름 : ");
	    	String prd_name = MyUtil.sc.nextLine();
	    	System.out.print("상품설명 : ");
	    	String description = MyUtil.sc.nextLine();
	    	System.out.print("가격 : ");
	    	int price = MyUtil.sc.nextInt();
	    	System.out.print("재고 : ");
	    	int stock = MyUtil.sc.nextInt();
	    	
	    	//상품번호 중복 검사
	    	Connection conn = OracleDB.getOracleConnection();
	    	
	    	try {
	    		String sql = "SELECT * FROM PRODUCT WHERE PRD_NO = ?";
	    		PreparedStatement pstmt = conn.prepareStatement(sql);
	        	pstmt.setInt(1, prd_no);
	        	ResultSet rs = pstmt.executeQuery();
	        	if(rs.next()) {System.out.println("상품번호는 중복되면 안됩니다. 다시 시도해 주세요");
	        	continue;//중복되면 등록 초기화면으로 돌아감
	        	}
	        	
	        	//중복없으면 등록함
	        	String sqlInsert 
	            = "INSERT INTO PRODUCT(CAT_NO, PRD_NO, PRD_NAME, DESCIRIPTION, PRICE, STOCK) " 
	            		+ "VALUES(?,?,?,?,?,?)";
	        	PreparedStatement pstmt2 = conn.prepareStatement(sqlInsert);
	        	pstmt2.setInt(1, cat_no);
	            pstmt2.setInt(2, prd_no);
	            pstmt2.setString(3, prd_name);
	            pstmt2.setString(4, description);
	            pstmt2.setInt(5, price);
	            pstmt2.setInt(6, stock);
	            int itemResult = pstmt2.executeUpdate();
	            
	            if(itemResult == 1) {
	           	 System.out.println("정상적으로 상품을 등록하였습니다");
	           	 break;
	            }

	    	}catch(SQLException e) {
	    		System.out.println("상품 등록 중 문제 발생!");
	    	}

          }
	    	
	    	System.out.println("상품 등록 및 삭제 메뉴로 돌아갑니다");
	    	item0();
	    	
	    }
	//상품 삭제
		public void item0_2() {
			
		
		
		
		
		
	}
	
		
		
		//테스트용 메서드
		 public static void main(String[] args) {
				new A_0_product().item0();
				

			}
}
