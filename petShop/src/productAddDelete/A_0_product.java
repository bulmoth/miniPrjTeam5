package productAddDelete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import oracleDB.OracleDB;
import util.MyUtil;


public class A_0_product {
	
	
	private int selectNum;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Connection conn = null;
	
	
	public void itemMenu() {
		
		
		System.out.println("─────────────────────────────────");
		System.out.println(); 
		System.out.println("        상품등록 및 삭제");
		System.out.println("        [0] 상품현황");
		System.out.println("        [1] 상품등록");
		System.out.println("        [2] 상품삭제");
		System.out.println("        [3] 재고추가");
		System.out.println("        [4] 관리자 메뉴로 돌아가기");
		System.out.println();
		System.out.println("─────────────────────────────────");
		System.out.println("     원하시는 메뉴를 선택해주세요");
		System.out.print("        메뉴 : ");
		
		
		
		selectNum = MyUtil.scInt();
		
		switch(selectNum) {
		case 0 : //상품현황 
			showItem();
			break;
		case 1 : //상품등록
			addItem();
			break;
		case 2 : //상품삭제
			deleteItem();
			break;
		case 3 : //상품 재고 추가
			addStock();
			break;			
		case 4 : //관리자 메뉴로 돌아가기
			cancelItem();
			break;
		default : System.out.println("유효한 메뉴가 아닙니다 다시 선택해주세요!"); itemMenu();

	}

}
	
	
	//상품현황
	public void showItem() {
		
		conn = OracleDB.getOracleConnection();
		String sql = "SELECT CATEGORY, PRD_NO, PRD_NAME, DESCRIPTION, PRICE, STOCK FROM PRODUCT_CATEGORY ORDER BY PRD_NO";
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println();
			System.out.println("     카테고리          상품번호         상품이름                    상품설명                   가격             재고");
			System.out.println("────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
			System.out.println();
			while(rs.next()) {
			 	 
				
				 
				 String category = rs.getString("CATEGORY");
		    	 int prd_no = rs.getInt("PRD_NO");
		    	 String prd_name = rs.getString("PRD_NAME");
			     String description = rs.getString("DESCRIPTION");
			     int price = rs.getInt("PRICE");
		    	 int stock = rs.getInt("STOCK");

		    	 System.out.printf("%10s",category+"\t");
		    	 System.out.printf("%10s",prd_no+"\t");
		         System.out.printf("%10s",prd_name+"\t");
		    	 System.out.printf("%25s",description+"\t");
		    	 System.out.printf("%10s",price+"\t");
		         System.out.printf("%10s",stock+"\t\n");
		         


			}
			
			
			
		
		} catch (SQLException e) { 
			System.out.println("상품 조회 중 문제가 발생하였습니다.");
		}finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(rs);
		}
	
		System.out.println();
		System.out.println("───────────────────────────────");
		System.out.println(); 
		System.out.println("    이전메뉴 : 아무키나 눌러주세요");
		System.out.println(); 
		System.out.println("───────────────────────────────");
		MyUtil.sc.nextLine();
		itemMenu();
		
	}

	
	//상품 등록
	    private void addItem() {

	    	conn = OracleDB.getOracleConnection();
	    	
	    	
	    	
	    	   try {
	    		   
	    		   while(true) {
	       	    	System.out.printf("카테고리번호\n(1. 개사료, 2.고양이사료 3.개간식, 4.고양이간식 5. 장난감, 6.기타 숫자만 입력) : ");
	       	    	int cat_no = MyUtil.scInt();
	       	    	System.out.print("상품번호 : ");
	       	    	int prd_no = MyUtil.scInt();
	       	    	System.out.print("상품이름 : ");
	       	    	String prd_name = MyUtil.sc.nextLine();
	       	    	System.out.print("상품설명 : ");
	       	    	String description = MyUtil.sc.nextLine();
	       	    	System.out.print("가격 : ");
	       	    	int price = MyUtil.scInt();
	       	    	System.out.print("재고 : ");
	       	    	int stock = MyUtil.scInt();
	       	    	
	       	    	
	       	    	
		    		String sql = "SELECT * FROM PRODUCT WHERE PRD_NO = ?";
		    		pstmt = conn.prepareStatement(sql);
		        	pstmt.setInt(1, prd_no);
		        	ResultSet rs = pstmt.executeQuery();
		        	
		        	//상품번호 중복 검사
		        	if(rs.next()) {System.out.println("상품번호는 중복되면 안됩니다. 다시 시도해 주세요");
		        	continue;//중복되면 등록 초기화면으로 돌아감
		        	}
		        	
	        	//중복없으면 등록함
	        	String sqlInsert 
	            = "INSERT INTO PRODUCT(CAT_NO, PRD_NO, PRD_NAME, DESCRIPTION, PRICE, STOCK) " 
	            		+ "VALUES(?,?,?,?,?,?)";
	        	pstmt = conn.prepareStatement(sqlInsert);
	        	pstmt.setInt(1, cat_no);
	            pstmt.setInt(2, prd_no);
	            pstmt.setString(3, prd_name);
	            pstmt.setString(4, description);
	            pstmt.setInt(5, price);
	            pstmt.setInt(6, stock);
	            int itemResult = pstmt.executeUpdate();
	            
	            if(itemResult == 1) {
	           	 System.out.println("정상적으로 상품을 등록하였습니다");
	           	 break;
	            }
	    		   }

	    	}catch(SQLException e) {
	    		System.out.println("상품 등록 중 문제 발생!");
	    	}catch(InputMismatchException e) {
	    		MyUtil.sc.nextLine();
		    	System.out.println("상품 등록 중 문제 발생! 양식을 확인해주세요");
	    		
	    	}finally {
				OracleDB.close(conn);
				OracleDB.close(pstmt);
				OracleDB.close(rs); 
				}

	    	
	    	
	    	System.out.println("상품 등록 및 삭제 메뉴로 돌아갑니다");
	    	itemMenu();
	    	
	    
	    }
	    
	//상품 삭제
	    private void deleteItem() {
			conn = OracleDB.getOracleConnection();
			
    		
    		try {
    			System.out.println("    삭제할 상품 번호를 입력하세요");
    			System.out.print("        상품 번호 : ");
    			int prd_no = MyUtil.scInt();
    			
    			
    			String sqlDelete = "DELETE FROM PRODUCT WHERE PRD_NO = ?";
    			pstmt = conn.prepareStatement(sqlDelete);
	        	pstmt.setInt(1, prd_no);
	        	
	        	int deleteResult = pstmt.executeUpdate();
	        	//삭제 확인
	        	if(deleteResult == 1) {
		           	 System.out.println("정상적으로 상품을 삭제하였습니다");
	        	}else {
	        		System.out.println("상품을 삭제하지 못했습니다. 상품번호를 다시 확인해주세요");
	        		
	        	}
    		

	        	}catch (SQLException e){
	        		System.out.println("상품 삭제 중 문제 발생!");
	        	}catch(InputMismatchException e) {
		    		MyUtil.sc.nextLine();
			    	System.out.println("상품 삭제 중 문제 발생! 양식을 확인해주세요");
	        	}finally {
	    			OracleDB.close(conn);
	    			OracleDB.close(pstmt);
	    			OracleDB.close(rs);
	    		}
		
    		System.out.println("상품 등록 및 삭제 메뉴로 돌아갑니다");
    		itemMenu();
		
		
	}
		//상품 재고 추가
	    private void addStock() {
			conn = OracleDB.getOracleConnection();
			
			try {
				while(true) {
				System.out.println("    재고 추가할 상품 번호를 입력하세요");
    			System.out.print("        상품 번호 : ");
    			int prd_no = MyUtil.scInt();
    			
			
    			//상품번호 있는지 확인
				String sql = "SELECT * FROM PRODUCT WHERE PRD_NO = ?";
	    		pstmt = conn.prepareStatement(sql);
	        	pstmt.setInt(1, prd_no);
	        	ResultSet rs = pstmt.executeQuery();
	        	if(rs.next()) {
	        		//해당하는 상품번호 있을 시
    				System.out.println("    몇 개 추가하시겠습니까?");
        			System.out.print("        추가 수 : ");
        			int stock = MyUtil.scInt();
        		    
	        		
        			String sqlUpdate = "UPDATE PRODUCT SET STOCK = STOCK + ? WHERE PRD_NO = ?";
        		    PreparedStatement pstmt2 = conn.prepareStatement(sqlUpdate);
        			pstmt2.setInt(1, stock);
    	        	pstmt2.setInt(2, prd_no);
    	        	
    	        	int updateResult = pstmt2.executeUpdate();
	        		
    	        	if(updateResult == 1) {
    		           	 System.out.println("정상적으로 처리되었습니다");
    		           	 break;
    		            }else {
    		            	System.out.println("처리하는 중 문제가 발생했습니다.");
    		            	break;
    		            }

	        	}else { //상품번호가 없는 경우
	            	System.out.println("해당하는 상품 번호가 없습니다. 상품번호를 다시 확인해주세요");
	            
	         	}
	        	
				}
	        	}catch (SQLException e){
	        		System.out.println("재고 추가 중 문제 발생!");
	        		
	        	}catch(InputMismatchException e) {
		    		MyUtil.sc.nextLine();
			    	System.out.println("재고 추가 중 문제 발생! 양식을 확인해주세요");
	        	
	        	}finally {
	    			OracleDB.close(conn);
	    			OracleDB.close(pstmt);
	    			OracleDB.close(rs);
	    		}
			
			
			System.out.println("상품 등록 및 삭제 메뉴로 돌아갑니다");
			itemMenu();

		}
		
		
		public void cancelItem() {
			System.out.println("      관리자 메뉴로 돌아갑니다");
			return;
		}
	
		
		

}
