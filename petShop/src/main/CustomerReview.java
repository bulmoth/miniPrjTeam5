package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

import member.Member;
import oracleDB.OracleDB;
import util.MyUtil;

public class CustomerReview {
	
	
	private int selectNum;
	
	
		//리뷰작성
		//게시글 작성
		public void ReviewMain() {
			
			
			System.out.println("리뷰 페이지 입니다");
			System.out.println();
			System.out.println("----------------------------");
			System.out.println("1. 리뷰 작성");
			System.out.println("2. 리뷰 목록 조회");
			System.out.println("3. 리뷰 상세 조회");
			System.out.println("4. 리뷰 삭제");
			System.out.println("5. 메인 페이지로 돌아가기");
			System.out.println("----------------------------");
			
			selectNum = MyUtil.sc.nextInt();
			
			switch(selectNum) {
			case 1 : 
				new CustomerReview().write(); break; // 리뷰 작성
			case 2 : 
				new CustomerReview().showList(); break; // 리뷰 목록 조회
			case 3 : 
				new CustomerReview().showReviewDetail(); break; // 리뷰 상세 조회
			case 4 : 
				new CustomerReview().revDelete(); break; //리뷰삭제
			case 5 : 
				new CustomerMain().CustomMain(); break;
			
			default : System.out.println("선택하신 메뉴는 유효하지 않습니다."); ReviewMain();
			}
			
		}
		
		public void write() {
			
			
			//작성자 == 로그인한 유저
			if(Member.LOGIN_USER_NO == 0) {
				System.out.println("로그인 한 유저만 글을 쓸 수 있습니다.");
				return;
			}
			
			//안내 문구 출력
			//입력 받기 (제목, 내용)
			System.out.println("===== 리뷰 작성 =====");
			System.out.print("상품 번호 : ");
			int prdNo = MyUtil.scInt(); 
			System.out.print("내용 : ");
			String content = MyUtil.sc.nextLine();
			System.out.print("별점 : ");
			int score = MyUtil.scInt();
			
			

			//연결 얻기
			Connection conn = OracleDB.getOracleConnection();
			//INSERT
			String sql = "INSERT INTO REVIEW(REV_NO, PRD_NO, MEM_NO, IMPRESSION, SCORE, REV_DATE, REV_DELETE)"
					+ "VALUES(REVIEW_NO_SEQ.NEXTVAL, ?, ?, ?, ?, SYSDATE, 'N')";
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, prdNo);
				pstmt.setInt(2, Member.LOGIN_USER_NO);
				pstmt.setString(3, content);
				pstmt.setInt(4, score);
				int  result = pstmt.executeUpdate();
				if(result == 1) {
					System.out.println("리뷰 등록 성공 !");
					System.out.println();
					System.out.println("===============");
				}else {
					System.out.println("리뷰 등록 실패 ..");
					System.out.println();
					System.out.println("===============");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				OracleDB.close(conn);
				OracleDB.close(pstmt);
			}
			
			ReviewMain();
			
		}//write

		//리뷰 목록 조회
		public void showList() {
			System.out.println("===== 리뷰 목록 조회 =====");
			//리뷰 목록 조회
			
			Connection conn = OracleDB.getOracleConnection();
			String sql = "SELECT * FROM REVIEW WHERE REV_DELETE = 'N' ORDER BY REV_DATE DESC";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				
				ResultSet rs = pstmt.executeQuery();
				
				
				System.out.print("번호");
				System.out.print(" | ");
				System.out.print("상품 번호");
				System.out.print(" | ");
				System.out.print("리뷰");
				System.out.print(" | ");
				System.out.print("작성자 번호");
				System.out.print(" | ");
				System.out.print("리뷰 작성일");
				System.out.print("\n-------------------------------");
				
				while(rs.next()) {
					int no = rs.getInt("REV_NO"); //리뷰 번호
					int prdNo = rs.getInt("PRD_NO");
					String score = rs.getString("SCORE");//리뷰 내용
					int memNo = rs.getInt("MEM_NO"); //작성자 회원 번호
					Timestamp revDate = rs.getTimestamp("REV_DATE");
					
					System.out.print(no);
					System.out.print(" | ");
					System.out.print(prdNo);
					System.out.print(" | ");
					System.out.print(score);
					System.out.print(" | ");
					System.out.print(memNo);
					System.out.print(" | ");
					System.out.print(revDate);
					System.out.println();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				OracleDB.close(conn);
				
				
			}//반납
			
			//리뷰 상세보기 호출
			ReviewMain();
			
		}//showList
		
		//리뷰 상세 조회
		public void showReviewDetail() {
			
		
			System.out.print("조회할 게시글 번호 입력 : ");
			int no = MyUtil.scInt();
			
			Connection conn = OracleDB.getOracleConnection();
			
			String sql = "SELECT * FROM REVIEW WHERE REV_NO = ? AND REV_DELETE ='N'";
			
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, no);
				
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.next()) {
					int revno = rs.getInt("REV_NO");
					int prdNo = rs.getInt("PRD_NO");
					String inpress = rs.getString("IMPRESSION");
					
					System.out.println("리뷰 번호 : " + revno);
					System.out.println("상품 번호 : " + prdNo);
					System.out.println("리뷰 : " + inpress);
					
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				OracleDB.close(conn);
				
				//반납
			}
			
			ReviewMain();
			
		}//showReviewDetail
		
		
		//리뷰 삭제
		PreparedStatement pstmt = null;
		
		public void revDelete() {
			
			Connection conn = OracleDB.getOracleConnection();
			
			System.out.println("리뷰 삭제 페이지 입니다.");
			System.out.println("삭제할 리뷰 넘버를 입력해 주세요");
			
			int revNo = MyUtil.scInt();
			
			String sql = "DELETE FROM REVIEW WHERE REV_NO = ?";

			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, revNo);
				int  result = pstmt.executeUpdate();
				if(result == 1) {
					System.out.println("리뷰 삭제 성공 !");
					System.out.println();
					System.out.println("===============");
				}else {
					System.out.println("리뷰 삭제 실패 ..");
					System.out.println();
					System.out.println("===============");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				
				OracleDB.close(pstmt);
			}
			
			ReviewMain();
	
		}
			

			
		

			
			
			
			
			

//			
//			
//		}
}
