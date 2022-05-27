package mgr_reply;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracleDB.OracleDB;
import util.MyUtil;


public class M_Reply {

	public static int ManagerLoginNo = 1234;
	public static int Question_no;
	public static int rep_no;
//	loginUserNo
	
	
//	======================================================================
	
//	고객센터 답변 게시판
	public void firstPageReply() {
		while(true) {
			
			System.out.println("========================");
			System.out.println("1. 문의글 확인");
			System.out.println("2. 답변 하기");
			System.out.println("3. 답변 목록 확인하기");
			System.out.println("4. 상위 목록으로 이동");
			System.out.println("========================");
			
			int n = MyUtil.scInt();
			
			switch(n) {
			case 1 : showQesList(); break;
			case 2 : mgrReply(); break;
			case 3 : showReply(); break;
			case 4 : return;
			
			default : System.out.println("다시 선택하세요");
		
			}
		
		}//while
	}
	
//	==================================================================================
	
	
		
//	    문의 게시판에서 목록가져오기
		public void showQesList() {
			
			System.out.println("===== 답글 목록 조회 =====");
			
			//관리자만 답글 목록 확인가능
			if(ManagerLoginNo == 0 ) {
					System.out.println("답글 목록 확인을 위해서 관리자로 로그인해 주세요.");
					return;
			}
			
			//연결얻기
			Connection conn = OracleDB.getOracleConnection();
			
			String sql = "SELECT 질문번호, 질문제목, 질문내용, 질문날짜 FROM 질문테이블 WHERE 질문의_DELETE_YN = 'N' ORDER BY 질문_DATE DESC";
			
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
				
				int no = rs.getInt("질문번호");
				String title = rs.getString("질문제목");
				String comment = rs.getString("질문내용");
				Date rdate = rs.getDate("질문날짜");
				
				//질문 번호 저장
				Question_no = no;
				
				System.out.print(no);
				System.out.print(" | ");
				System.out.print(title);
				System.out.print(" | ");
				System.out.print(comment);
				System.out.print(" | ");
				System.out.print(rdate);
				
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				OracleDB.close(conn);
				OracleDB.close(pstmt);
				OracleDB.close(rs);
			}
			
								
		}//showQesList
	
		
		
		//고객 게시글 답변 메서드 - mgrReply()
		public void mgrReply() {
			
			//질문 번호로 답글 달기
			
			//글 작성자는 관리자로 로그인된 사람만 작성 가능
			
			//매니저 로그인 번호로 매니저인지 확인 - loginManagerNo 저장하는 static 변수 필요
			if(ManagerLoginNo == 0) {
				System.out.println("관리자만 답글을 쓸 수 있습니다.");
				return;
			}
			
			
			
			//안내 문구 출력 - 답글 입력하기
			System.out.println("===== 답글 달기 ======");
			
			System.out.println("답변 할 질문 번호를 입력해 주세요 : ");
			rep_no = MyUtil.scInt();
				
			System.out.println("답변 할 내용을 입력해 주세요");
			String reply = MyUtil.sc.nextLine();
			
			
			//답글 달아서 디비에 저장
			
			Connection conn = OracleDB.getOracleConnection();
			
			String sql = "INSERT INTO MGR_REPLY(REP_NO, 질문번호(), REP_COMMENT, REP_DATE, RTP_DELETE_YN )"
							+ "VALUES(?, ?, ?, SYSDATE, 'N')";
			
			
			PreparedStatement pstmt = null;
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, rep_no);
				pstmt.setInt(2, Question_no);
				pstmt.setString(3, reply);
				
				int result = pstmt.executeUpdate();
				
				if(rep_no != Question_no){
					System.out.println("게시글 번호가 다릅니다. ...");
				}else if(result==1) {
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
			int rep_no = -9999;
			System.out.println("===== 답글 목록 조회 =====");
			
			//관리자만 답글 목록 확인가능
			if(ManagerLoginNo == 0 ) {
					System.out.println("관리자만 답글 목록을 볼 수 있습니다.");
					return;
			}	
			
					
			Connection conn = OracleDB.getOracleConnection();
			
			String sql = "SELECT * FROM M_REPLY WHERE REP_DELETE_YN = 'N' ORDER BY REP_DATE DESC";
			
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
				System.out.println("\n=======================================");
				
				int no = rs.getInt("REP_NO");
				String comment = rs.getString("REP_COMMENT");
				Date rdate = rs.getDate("REP_DATE");
				
				System.out.print(no);
				System.out.print(" | ");
				System.out.print(comment);
				System.out.print(" | ");
				System.out.print(rdate);
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				OracleDB.close(conn);
				OracleDB.close(pstmt);
				OracleDB.close(rs);
			}
			
						
		}//showReply
		
		
}//class

		//목록상세조회하기
		
		
//입력받은 번호랑 / 질문번호랑 일치하는지
//pstmt.setInt(1, no);
//pstmt.setInt(1, rep_no);

//		=================================================
		
		
	


	
		

//  고객문의답변 첫 페이지 오면 선택 / 1.문의글 확인(답글달아야할 글) 2.답글달아서 db저장 3.답글달아준 내용확인


//	변경할거
//	문의 게시판 글 - 질문번호, 글 제목 따와서 목록 보여주고
//	그 목록에서 답글 달 번호 선택하는 작업으로 변경해주기 
	
//	추가할거
//	답글 달기 완료한 문의글에 답글 달았다고 체크해주기. 
//	답글 확인할때  - 문의한 사람만 답글 확인하게 - 내가 문의한 글만 보이도록
//	내 답글에 사용자가 댓글 달 수 있도록?
//	자주묻는 질문 게시판 만들기
	
	
	

