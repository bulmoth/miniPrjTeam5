package mgr_reply;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracleDB.OracleDB;
import productAddDelete.AdminMain;
import util.MyUtil;


public class M_Reply {
	
	static int QuestionNo;
	static int repNo;
//	======================================================================
	
//	고객센터 답변 게시판
	public void firstPageReply() {
		
		while(true) {
			
			System.out.println("========================");
			System.out.println();
			System.out.println("1. 문의글 확인하기");
			System.out.println("2. 답변 목록 확인하기");
			System.out.println("3. 상위 목록으로 이동");
			System.out.println();
			System.out.println("========================");
			
			int n = MyUtil.scInt();
			
			switch(n) {
			case 1 : showQesList(); break;
			case 2 : showReply(); break;
			case 3 : return;
			
			default : System.out.println("다시 선택하세요");
		
			}
		
		}//while
	}
	
//	==================================================================================
	
	
		
//	    문의 게시판에서 목록가져오기
		public void showQesList() {
			
			System.out.println("===== 답글 목록 조회 =====");
			
						
			//연결얻기
			Connection conn = OracleDB.getOracleConnection();
			
			String sql = "SELECT MEM_NO, QES_TITLE, QES_CONTENTS, QES_DATE FROM QUESTION ORDER BY QES_DATE DESC";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			
			try {
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				System.out.print("질문 번호");
				System.out.print(" | ");
				System.out.print("질문 제목");
				System.out.print(" | ");
				System.out.print("질문 내용");
				System.out.print(" | ");
				System.out.print("질문 날짜");
				System.out.println("\n=======================================");
				
				while(rs.next()) {
				
				int no = rs.getInt("MEM_NO");
				String title = rs.getString("QES_TITLE");
				String comment = rs.getString("QES_CONTENTS");
				Date rdate = rs.getDate("QES_DATE");
				
				//질문 번호 저장
				QuestionNo = no;
				
				System.out.print(no);
				System.out.print(" | ");
				System.out.print(title);
				System.out.print(" | ");
				System.out.print(comment);
				System.out.print(" | ");
				System.out.print(rdate);
				
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				OracleDB.close(conn);
				OracleDB.close(pstmt);
				OracleDB.close(rs);
			}
			
			
			//답변 할 번호 선택 메서드로 이어지도록
			mgrReply();		
		}//showQesList
	
		
		
		//고객 게시글 답변 메서드 - mgrReply()
		public void mgrReply() {
			
			//질문 번호로 답글 달기
			
			//글 작성자는 관리자로 로그인된 사람만 작성 가능
			
						
			//안내 문구 출력 - 답글 입력하기
			System.out.println("===== 답글 달기 ======");
			
			System.out.println("답변 할 질문 번호를 입력해 주세요 : ");
			repNo = MyUtil.scInt();
				
			System.out.println("답변 할 내용을 입력해 주세요");
			String reply = MyUtil.sc.nextLine();
			
			
			//답글 달아서 디비에 저장
			
			Connection conn = OracleDB.getOracleConnection();
			
			String sql = "INSERT INTO MGR_REPLY(REP_NO, QES_NO, REP_COMMENT)"
							+ "VALUES(?, ?, ?)";
			
			
			PreparedStatement pstmt = null;
			
			try {
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, repNo);
				pstmt.setInt(2, QuestionNo);
				pstmt.setString(3, reply);
				
				int result = pstmt.executeUpdate();
				
				if(result==1) {
					System.out.println("게시글 답변 등록 성공 !!!");
				}else {
					System.out.println("게시글 답변 등록 실패 ...");
				}
			
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				OracleDB.close(conn);
				OracleDB.close(pstmt);
			}
			
			
		}//mgrReply
	
		
		
	//답글 목록 확인하기 메서드 - showReply()
	
		public void showReply() {
			
			System.out.println("===== 답글 목록 조회 =====");
			
								
			Connection conn = OracleDB.getOracleConnection();
			
			String sql = "SELECT * FROM MGR_REPLY WHERE REP_DELETE_YN = 'N' ORDER BY REP_DATE DESC";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				pstmt = conn.prepareStatement(sql);
				
				
				rs = pstmt.executeQuery();
				
				System.out.print("답변 번호");
				System.out.print(" | ");
				System.out.print("답변 내용");
				System.out.print(" | ");
				System.out.print("답글 작성일");
				System.out.println("\n-----------------------------");
				
				while(rs.next()) {
				int no = rs.getInt("REP_NO");
				String comment = rs.getString("REP_COMMENT");
				Date rdate = rs.getDate("REP_DATE");
				
				System.out.print(no);
				System.out.print(" | ");
				System.out.print(comment);
				System.out.print(" | ");
				System.out.print(rdate);
				
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				OracleDB.close(conn);
				OracleDB.close(pstmt);
				OracleDB.close(rs);
			}
			
						
		}//showReply
		

		 	
}//class

		

