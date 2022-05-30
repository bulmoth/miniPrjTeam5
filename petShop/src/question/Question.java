package question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import main.CustomerMain;
import main.CustomerReview;
import member.Member;
import oracleDB.OracleDB;
import util.MyUtil;

public class Question{
	
	private int selectNum;
	
	public void qMain() {
		
		while(true) {
		System.out.println("문의 페이지 입니다");
		System.out.println();
		System.out.println("----------------------------");
		System.out.println("1. 문의 작성");
		System.out.println("2. 문의 목록 조회");
		System.out.println("3. 문의 수정");
		System.out.println("4. 메인 페이지로 돌아가기");
		System.out.println("----------------------------");
		
		selectNum = MyUtil.scInt();
		
		switch(selectNum) {
		case 1 : 
			write(); break; // 리뷰 작성
		case 2 : 
			showList(); break; // 리뷰 목록 조회
		case 3 : 
			writeUpdate(); break; // 리뷰 상세 조회
		case 4 : 
			return; //메인페이지로 돌아가기
		
		default : System.out.println("선택하신 메뉴는 유효하지 않습니다."); new CustomerMain().CustomMain();
		}
		}
	}
	
	public void write() {
		if(Member.LOGIN_USER_NO == 0) {
			System.out.println("로그인 한 유저만 글을 쓸 수 있습니다.");
			return;
		} 
		System.out.println("====문의 게시글 작성====");
		System.out.print("제목 : ");
		String title = MyUtil.sc.nextLine();
		System.out.print("내용 : ");
		String content = MyUtil.sc.nextLine();
		
		Connection conn = OracleDB.getOracleConnection();
		
		String sql = "INSERT INTO QUESTION(QES_NO, MEM_NO, QES_TITLE, QES_CONTENTS)"
				+ "VALUES(QUESTION_NO_SEQ.NEXTVAL,?, ?, ?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Member.LOGIN_USER_NO);
			pstmt.setString(2, title);
			pstmt.setString(3, content);
			int result = pstmt.executeUpdate();
			if(result == 1) {
				System.out.println("문의 작성 성공!");	
			}else {
				System.out.println("문의 작성 실패!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			OracleDB.close(conn);
			OracleDB.close(pstmt);
		}	
	}

	
	public void showList() {
		System.out.println("====게시글 목록조회====");
		Connection conn = OracleDB.getOracleConnection();
		String sql = "SELECT * FROM QUESTION ORDER BY QES_DATE ASC";
		ResultSet rs = null;
	
		System.out.print("글번호");
		System.out.print(" | ");
		System.out.print("게시글 제목");
		System.out.print(" | ");
		System.out.print("작성자 번호");
		System.out.print(" | ");
		System.out.print("게시글 작성일");
		System.out.println("\n-----------------------");
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				
				int no = rs.getInt("QES_NO");
				String title = rs.getString("QES_TITLE");
				int writeNo = rs.getInt("MEM_NO");
				Timestamp regDate = rs.getTimestamp("QES_DATE");
				System.out.print(no);
				System.out.print(" | ");
				System.out.print(title);
				System.out.print(" | ");
				System.out.print(writeNo);
				System.out.print(" | ");
				System.out.print(regDate);
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			OracleDB.close(conn);
			OracleDB.close(rs);
		}
		showListDetail(); 
	}
	
	
	public void showListDetail() {
		//게시글 번호 (no) 필요함
		//출력문 추가
		System.out.print("조회할 문의글 번호 입력 : ");
		int no = MyUtil.sc.nextInt();
		//커넥션
		Connection conn = OracleDB.getOracleConnection();
		//쿼리
		String sql = "SELECT * FROM QUESTION WHERE QES_NO = ?";
		ResultSet rs = null;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, no);
		
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String title = rs.getString("QES_TITLE");
				String content = rs.getString("QES_CONTENTS");
				
				System.out.println("제목 : "+title );
				System.out.println("내용 : "+content );
			
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			OracleDB.close(conn);
			OracleDB.close(rs);
		}
	
	}
	
	
	
	public void writeUpdate() {
		//수정자 == 로그인한 유저
		if(Member.LOGIN_USER_NO == 0) {
			System.out.println("로그인 한 유저만 글을 수정할 수 있습니다.");
			return;
		} 
		//안내 문구 출력
		System.out.println("내가 문의한 글 리스트 ");
		//연결 얻기
		System.out.println("====게시글 목록조회====");
		//모든 게시글 조회해서 보여주기
		//1. 커넥션
		Connection conn = OracleDB.getOracleConnection();
		//2.쿼리 작성
		
		System.out.print("글번호");
		System.out.print(" | ");
		System.out.print("게시글 제목");
		System.out.print(" | ");
		System.out.print("작성자 번호");
		System.out.print(" | ");
		System.out.print("게시글 작성일");
		System.out.println("\n-----------------------");
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		try {
			String sql = "SELECT * FROM QUESTION "
					+ "WHERE MEM_NO = ? "
					+ "ORDER BY QES_DATE ASC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Member.LOGIN_USER_NO);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				int no = rs.getInt("QES_NO");
				String title = rs.getString("QES_TITLE");
				int writeNo = rs.getInt("MEM_NO");
				Timestamp regDate = rs.getTimestamp("QES_DATE");
				System.out.print(no);
				System.out.print(" | ");
				System.out.print(title);
				System.out.print(" | ");
				System.out.print(writeNo);
				System.out.print(" | ");
				System.out.print(regDate);
				System.out.println();
			}
				System.out.print("수정할 문의글 번호 선택 : ");	
				int selectNo = MyUtil.scInt();
				
				System.out.println("수정할 제목 : ");
				String reTitle = MyUtil.sc.nextLine();
				System.out.println("수정할 내용 : ");
				String reContents = MyUtil.sc.nextLine();
				
				String sqlUpdate = "UPDATE QUESTION "
						+ "SET QES_TITLE = ?"
						+ ", QES_CONTENTS = ?"
						+ "WHERE QES_NO = ?";
				pstmt2 = conn.prepareStatement(sqlUpdate);
				pstmt2.setString(1, reTitle);
				pstmt2.setString(2, reContents);
				pstmt2.setInt(3, selectNo);
				int result = pstmt2.executeUpdate();
				if(result == 1) {
					System.out.println("문의글 수정 성공");
				}else {
					System.out.println("문의 작성 실패!");
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			OracleDB.close(conn);
			OracleDB.close(rs);
			OracleDB.close(pstmt);
			OracleDB.close(pstmt2);
		}
	
}
}
