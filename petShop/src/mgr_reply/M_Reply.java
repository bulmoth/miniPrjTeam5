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
	public static int MemberLoginNo = 9876;
	public static int qes_no = 12;
	
	
	//답글 목록 확인하기 메서드 - showReply()
	
		public void showReply() {
			
			System.out.println("===== 답글 목록 조회 =====");
			
			//관리자만 답글 목록 확인가능
			if(ManagerLoginNo == 0 ) {
					System.out.println("관리자만 답글 목록을 볼 수 있습니다.");
					return;
			}		
			
					
			Connection conn = OracleDB.getOracleConnection();
			
			String sql = "SELECT * FROM MGR_REPLY WHERE RTPLY_DELETE_YN = 'N' ORDER BY REPLY_DATE DESC";
			
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
				
				int no = rs.getInt("REPLY_NO");
				String comment = rs.getString("REPLY_COMMENT");
				Date rdate = rs.getDate("REPLY_DATE");
				
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
			
			//목록을 보여주고 - 답변한 글 선택할 수 있도록 - mgrReply() 메서드 호출
			mgrReply();
			
		}//showReply
		
		
		
//		=================================================
		
		
	
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
		int titleNum = MyUtil.sc.nextInt();//scInt로 처리필요
		
			
		System.out.println("답변 할 내용을 입력해 주세요");
		String reply = MyUtil.sc.nextLine();
		
		
		//답글 달아서 디비에 저장
		
		Connection conn = OracleDB.getOracleConnection();
		
		String sql = "INSERT INTO MGR_REPLY(REPLY_NO, Q_NO, REPLY_COMMENT, REPLY_DATE, RTPLY_DELETE_YN )"
						+ "VALUES(?, ?, ?, SYSDATE, 'N')";
		
		
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, qes_no);
			pstmt.setInt(2, qes_no);
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
		
	
		
// 5.26 : 내가 답글 작성하고, 답글 작성한 목록 보여주기 까지 진행	
	
//	변경할거
//	문의 게시판 글 - 질문번호, 글 제목 따와서 목록 보여주고
//	그 목록에서 답글 달 번호 선택하는 작업으로 변경해주기 
	
//	추가할거
//	답글 달기 완료한 문의글에 답글 달았다고 체크해주기. 
//	답글 확인할때  - 문의한 사람만 답글 확인하게 - 내가 문의한 글만 보이도록
//	내 답글에 사용자가 댓글 달 수 있도록?
//	자주묻는 질문 게시판 만들기
	
	
	
}//class
