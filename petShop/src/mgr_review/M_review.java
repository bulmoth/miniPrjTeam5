package mgr_review;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import oracleDB.OracleDB;
import util.MyUtil;

public class M_review {
	
	private int selectNum;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Connection conn = null;
	

	
	public void m_review() {
		
		
				System.out.println("─────────────────────────────────");
				System.out.println(); 
				System.out.println("        리뷰 관리 페이지");
				System.out.println("        [0] 리뷰조회");
				System.out.println("        [1] 리뷰삭제");
				System.out.println("        [2] 관리자 메뉴로 돌아가기");
				System.out.println();
				System.out.println("─────────────────────────────────");
				System.out.println("     원하시는 메뉴를 선택해주세요");
				System.out.print("        메뉴 : ");
					
					selectNum = MyUtil.scInt();///
					
					switch(selectNum) {
					case 0 : //리뷰조회
						showReview();
						break;
					case 1 : //리뷰삭제
						deleteReview();
						break;
					case 2 : //관리자 메뉴로 돌아가기
						cancelReview();
						break;
					default : System.out.println("유효한 메뉴가 아닙니다 다시 선택해주세요!"); m_review();
			
				}
	
		
	}
	
	
	
	
	public void showReview() {//리뷰 조회
		
		System.out.println("─────────────────────────────────");
		System.out.println(); 
		System.out.println("     어떤 순서로 조회하시겠습니까?");
		System.out.println("       [0] 별점순으로");
		System.out.println("       [1] 최신순으로");
		System.out.println("       [2] 이전 메뉴로 돌아가기");
		System.out.println();
		System.out.println("─────────────────────────────────");
		
		System.out.print("        선택 : ");
		int selectNum = MyUtil.scInt();//
		
		
		
		switch(selectNum) {
		case 0 : //별점순으로 
			star();
			break;
		case 1 : //게시글번호 순서대로
			revDate();
			break;
		case 2 : //이전 메뉴로 돌아가기
			System.out.println("이전 메뉴로 돌아갑니다");
			m_review();
			break;
		default : System.out.println("유효한 메뉴가 아닙니다 다시 선택해주세요!"); showReview();

	}

		
	}
	
	
	
	private void deleteReview() {//리뷰삭제
		
		conn = OracleDB.getOracleConnection();
		
		
		try {
			
			System.out.println("─────────────────────────────────");
			System.out.println(); 
			System.out.println("          리뷰 삭제 메뉴");
			System.out.println(); 
			System.out.println("─────────────────────────────────");
			
			System.out.println("  삭제할 리뷰 게시글 번호를 입력해주세요");
			System.out.print("        게시글 번호 :");
			
			int rev_no = MyUtil.scInt();///
			
			
			
			//삭제지만 리뷰 삭제란만 N으로 바꿔주는 작업
    			String sqlDelete = "UPDATE REVIEW SET REV_DELETE = 'Y' WHERE REV_NO = ?";
    			pstmt = conn.prepareStatement(sqlDelete);
	        	pstmt.setInt(1, rev_no);
	        	int deleteResult = pstmt.executeUpdate();
	        	
	        	
	        	//삭제 확인
	        	if(deleteResult == 1) {
		           	 System.out.println("정상적으로 리뷰를 삭제하였습니다");
	        	}else {
	        		System.out.println("리뷰를 삭제하지 못했습니다. 리뷰번호를 다시 확인해주세요");
	        		
	        	}

	        	}catch (SQLException e){
	        		System.out.println("삭제 중 문제 발생!");
	        	}catch(InputMismatchException e) {
		    		MyUtil.sc.nextLine();
			    	System.out.println("리뷰 삭제 중 문제 발생! 양식을 확인해주세요");
	        	}finally {
	    			OracleDB.close(conn);
	    			OracleDB.close(pstmt);
	    			OracleDB.close(rs);
	    		}
		
    		System.out.println("리뷰 관리 페이지로 돌아갑니다");
    		m_review();
    		    

		
	}
	
	
	public void cancelReview() {//관리자 메뉴로 돌아가기
		System.out.println("      관리자 메뉴로 돌아갑니다");
		return;
	
}
	
	
	public void star() {//별점순으로 조회
		System.out.println("      별점순으로 조회합니다 ");
		conn = OracleDB.getOracleConnection();
		String sql = "SELECT * \r\n"
				+ "FROM REVIEW R\r\n"
				+ "JOIN PRODUCT P\r\n"
				+ "ON r.prd_no = p.prd_no\r\n"
				+ "WHERE REV_DELETE = 'N' ORDER BY SCORE DESC";
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println();
			System.out.println("     리뷰번호        상품이름             회원번호                        후기                별점          작성일자");
			System.out.println("────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
			System.out.println();
			while(rs.next()) {
			 	 
				
				 
				 int rev_no = rs.getInt("REV_NO");
		    	 String prd_name = rs.getString("PRD_NAME");
		    	 int mem_no = rs.getInt("MEM_NO");
			     String impression = rs.getString("IMPRESSION");
			     int score = rs.getInt("SCORE");
		    	 Date rev_date = rs.getDate("REV_DATE");

		    	 System.out.printf("%10s",rev_no+"\t");
		    	 System.out.printf("%10s",prd_name+"\t");
		         System.out.printf("%10s",mem_no+"\t");
		    	 System.out.printf("%25s",impression+"\t");
		    	 System.out.printf("%10s",score+"\t");
		         System.out.printf("%10s",rev_date+"\t\n");

			}

		} catch (SQLException e) { 
			System.out.println("리뷰 조회 중 문제가 발생하였습니다.");
			
		}finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(rs);
		}
	
		System.out.println();
		System.out.println("─────────────────────────────────────");
		System.out.println(); 
		System.out.println("    초기 메뉴로 : 아무키나 눌러주세요");
		System.out.println(); 
		System.out.println("─────────────────────────────────────");
		MyUtil.sc.nextLine();
		m_review();
		
	
	}
	
	
	public void revDate() {//최신순으로 조회
		System.out.println("      최신순으로 조회합니다 ");
		conn = OracleDB.getOracleConnection();
		String sql = "SELECT * FROM REVIEW WHERE REV_DELETE = 'N' ORDER BY REV_DATE";
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println();
			System.out.println("     리뷰번호          상품번호         회원번호                   후기                  별점             작성일자");
			System.out.println("────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
			System.out.println();
			while(rs.next()) {
			 	 
				
				 
				 int rev_no = rs.getInt("REV_NO");
		    	 int prd_no = rs.getInt("PRD_NO");
		    	 int mem_no = rs.getInt("MEM_NO");
			     String impression = rs.getString("IMPRESSION");
			     int score = rs.getInt("SCORE");
		    	 Date rev_date = rs.getDate("REV_DATE");

		    	 System.out.printf("%10s",rev_no+"\t");
		    	 System.out.printf("%10s",prd_no+"\t");
		         System.out.printf("%10s",mem_no+"\t");
		    	 System.out.printf("%25s",impression+"\t");
		    	 System.out.printf("%10s",score+"\t");
		         System.out.printf("%10s",rev_date+"\t\n");

			}

		} catch (SQLException e) { 
			System.out.println("리뷰 조회 중 문제가 발생하였습니다.");
		}finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
			OracleDB.close(rs);
		}
	
		System.out.println();
		System.out.println("───────────────────────────────");
		System.out.println(); 
		System.out.println("    초기 메뉴로 : 아무키나 눌러주세요");
		System.out.println(); 
		System.out.println("───────────────────────────────");
		MyUtil.sc.nextLine();
		m_review();
		
	
	}
	

}
