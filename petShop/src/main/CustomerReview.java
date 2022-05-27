package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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
			System.out.println("----------------------------");
			
			selectNum = MyUtil.sc.nextInt();
			
			switch(selectNum) {
			case 0 : 
				new CustomerReview().write(); break; // 리뷰 작성
			case 1 : 
				new CustomerReview().showList(); break; // 리뷰 목록 조회
			case 2 : 
				new CustomerReview().showReviewDetail(); break; // 리뷰 상세 조회
			
			default : System.out.println("선택하신 메뉴는 유효하지 않습니다."); ReviewMain();
			}
			
		}
		public void write() {
			//작성자 == 로그인한 유저
			if(Member.loginUserNo == 0) {
				System.out.println("로그인 한 유저만 글을 쓸 수 있습니다.");
				return;
			}
			
			//안내 문구 출력
			//입력 받기 (제목, 내용)
			System.out.println("===== 리뷰 작성 =====");
			System.out.print("제목 : ");
			String title = MyUtil.sc.nextLine();
			System.out.print("내용 : ");
			String content = MyUtil.sc.nextLine();
			

			//연결 얻기
			Connection conn = OracleDB.getOracleConnection();
			//INSERT
			String sql = "INSERT INTO REVIEW(NO, TITLE, CONTENT, WRITER_NO, REGISTER_DATE, DELETE_YN)"
					+ "VALUES(REVIEW_NO_SEQ.NEXTVAL, ?, ?, ?, SYSDATE, 'N')";
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, title);
				pstmt.setString(2, content);
				pstmt.setInt(3, Member.loginUserNo);
				int  result = pstmt.executeUpdate();
				if(result == 1) {
					System.out.println("리뷰 등록 성공 !");
				}else {
					System.out.println("리뷰 등록 실패 ..");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				OracleDB.close(conn);
				OracleDB.close(pstmt);
			}
		}//write

		//리뷰 목록 조회
		public void showList() {
			System.out.println("===== 리뷰 목록 조회 =====");
			//리뷰 목록 조회
			
			Connection conn = OracleDB.getOracleConnection();
			String sql = "SELECT * FROM REVIEW WHERE DELETE_YN = 'N' ORDER BY REGISTER_DATE DESC";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				
				ResultSet rs = pstmt.executeQuery();
				
				
				System.out.print("번호");
				System.out.print(" | ");
				System.out.print("리뷰 제목");
				System.out.print(" | ");
				System.out.print("작성자 번호");
				System.out.print(" | ");
				System.out.print("리뷰 작성일");
				System.out.print("\n-------------------------------");
				
				while(rs.next()) {
					int no = rs.getInt("NO"); //리뷰 번호
					String title = rs.getString("TITLE");//리뷰 제목
					int writerNo = rs.getInt("WRITER_NO"); //작성자 회원 번호
					Timestamp regDate = rs.getTimestamp("REGISTER_DATE");
					
					System.out.print(no);
					System.out.print(" | ");
					System.out.print(title);
					System.out.print(" | ");
					System.out.print(writerNo);
					System.out.print(" | ");
					System.out.print(regDate);
					System.out.println();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				
			}//반납
			
			//리뷰 상세보기 호출
			showReviewDetail();
			
		}//showList
		
		//리뷰 상세 조회
		public void showReviewDetail() {
			
		
			System.out.print("조회할 게시글 번호 입력 : ");
			int no = MyUtil.scInt();
			
			Connection conn = OracleDB.getOracleConnection();
			
			String sql = "SELECT * FROM REVIEW WHERE NO = ? AND DELETE_YN ='N'";
			
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, no);
				
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.next()) {
					String title = rs.getString("TITLE");
					String content = rs.getString("CONTENT");
					
					System.out.println("제목 : " + title);
					System.out.println("내용 : " + content);
					
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				//반납
			}
		}//showReviewDetail

}
