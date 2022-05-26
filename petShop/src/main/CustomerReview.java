package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import mgr_login.Mgr_login;
import oracleDB.OracleDB;
import util.MyUtil;

public class CustomerReview {
	
	//리뷰작성
	//게시글 작성
		public void write() {
			//작성자 == 로그인한 유저
			if(Mgr_login.로그인회원고유번호 == 0) {
				System.out.println("로그인 한 유저만 글을 쓸 수 있습니다.");
				return;
			}
			
			//안내 문구 출력
			//입력 받기 (제목, 내용)
			System.out.println("===== 게시글 작성 =====");
			System.out.print("제목 : ");
			String title = MyUtil.sc.nextLine();
			System.out.print("내용 : ");
			String content = MyUtil.sc.nextLine();
			
			
			//디비에 저장
			//연결 얻기
			Connection conn = OracleDB.getOracleConnection();
			//INSERT 쿼리 날리기
			String sql = "INSERT INTO REVIEW(NO, TITLE, CONTENT, WRITER_NO, REGISTER_DATE, DELETE_YN)"
					+ "VALUES(REVIEW_NO_SEQ.NEXTVAL, ?, ?, ?, SYSDATE, 'N')";
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, title);
				pstmt.setString(2, content);
				pstmt.setInt(3, Mgr_login.로그인회원고유번호);
				int  result = pstmt.executeUpdate();
				if(result == 1) {
					System.out.println("게시글 등록 성공 !");
				}else {
					System.out.println("게시글 등록 실패 ..");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				OracleDB.close(conn);
				OracleDB.close(pstmt);
			}
		}

		//게시글 목록 조회
		public void showList() {
			System.out.println("===== 게시글 목록 조회 =====");
			//게시글 조회해서 보여주기
			
			Connection conn = OracleDB.getOracleConnection();
			String sql = "SELECT * FROM REVIEW WHERE DELETE_YN = 'N' ORDER BY REGISTER_DATE DESC";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				
				ResultSet rs = pstmt.executeQuery();
				
				
				System.out.print("글번호");
				System.out.print(" | ");
				System.out.print("게시글 제목");
				System.out.print(" | ");
				System.out.print("작성자 번호");
				System.out.print(" | ");
				System.out.print("게시글 작성일");
				System.out.println("\n-------------------------------");
				
				while(rs.next()) {
					int no = rs.getInt("NO"); //게시글 번호
					String title = rs.getString("TITLE");//게시글 제목
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
			}
			//파이널리 블럭에서 자원 반납
			
			
			//게시판 상세보기 메소드 호출
			showBoardDetail();
			
		}//showList
		
		//게시글 상세 조회
		public void showBoardDetail() {
			
			// 게시글 번호 (no) 필요함
			// 출력문 추가
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
				//자원 반납
			}
		}

}
