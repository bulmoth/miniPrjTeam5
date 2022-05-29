package productAddDelete;


import adopt_reg.AdoptReg;
import mgr_checkOrderList.CheckOrderList;
import mgr_login.MgrLogin;
import mgr_reply.M_Reply;
import mgr_review.M_review;
import util.MyUtil;


public class AdminMain {
	
	private int selectNum; 
	
	
	
	public void adminMain() {
		
		//로그인 화면 불러와서 로그인 여부 가져오기
		boolean mLogin = new MgrLogin().mgrLogin();
		
		//로그인되면 보이는 메뉴
		Loop1 : while(mLogin) {
		
		System.out.println("───────────────────────────────");
		System.out.println();
		System.out.println("       관리자 메인 페이지");
		System.out.println();
		System.out.println("      [1] 회원 관리");
		System.out.println("      [2] 상품 등록 및 삭제");
		System.out.println("      [3] 오늘 매출 확인");
		System.out.println("      [4] 입양 관리");
		System.out.println("      [5] 리뷰 관리");
		System.out.println("      [6] 고객센터 게시글 관리");
		System.out.println("      [7] 프로그램 종료");
		System.out.println();
		System.out.println("───────────────────────────────");
		System.out.println("     원하시는 메뉴를 선택해주세요");
		System.out.print("       메뉴 : ");
		
		selectNum = MyUtil.scInt();
		
		
		switch(selectNum) {
		
		case 1 : //회원 관리
			new MgrLogin().memberMng();
			break;
		case 2 : //상품 등록 및 삭제
			new A_0_product().itemMenu();
			break;
		case 3 : //오늘 매출 확인
			new CheckOrderList().check();
			break;
		case 4 : //입양 관리
			new AdoptReg().adoptReg();
			break;
		case 5 : //리뷰 관리
			new M_review().m_review();
			break;
		case 6 : //고객센터 게시글 관리
			new M_Reply().firstPageReply();
			break;
		case 7 : //프로그램 종료
			System.out.println("       프로그램을 종료합니다!");
			break Loop1;
		default : System.out.println("유효한 메뉴가 아닙니다 다시 선택해주세요!"); adminMain();
		}
		
		}
		//로그인 정보가 없으면 
		if(!mLogin) {
			System.out.println("로그인 정보가 없습니다. 프로그램을 종료합니다.");
		}
		
	}
	
	

}
