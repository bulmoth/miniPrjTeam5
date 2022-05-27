package productAddDelete;


import mgr_review.M_review;
import util.MyUtil;


public class AdminMain {
	
	private int selectNum; 
	
	
	public void adminMain() {
		
		System.out.println("───────────────────────────────");
		System.out.println();
		System.out.println("       관리자 메인 페이지");
		System.out.println();
		System.out.println("      [0] 관리자 로그인");
		System.out.println("      [1] 회원 관리");
		System.out.println("      [2] 상품 등록 및 삭제");
		System.out.println("      [3] 주문서 조회");
		System.out.println("      [4] 호텔, 입양 관리");
		System.out.println("      [5] 리뷰 관리");
		System.out.println("      [6] 고객센터 게시글 조회");
		System.out.println("      [7] 프로그램 종료");
		System.out.println();
		System.out.println("───────────────────────────────");
		System.out.println("     원하시는 메뉴를 선택해주세요");
		System.out.print("       메뉴 : ");
		
		selectNum = MyUtil.sc.nextInt();
		MyUtil.sc.nextLine(); //엔터키 처리
		
		switch(selectNum) {
		case 0 : //관리자 로그인
		
			break;
		case 1 : //회원 관리
			
			break;
		case 2 : //상품 등록 및 삭제
			new A_0_product().itemMenu();
			break;
		case 3 : //주문서 조회
			
			break;
		case 4 : //호텔, 입양 관리
			
			break;
		case 5 : //리뷰 관리
			new M_review().m_review();
			break;
		case 6 : //고객센터 게시글 조회
		
			break;
		case 7 : //프로그램 종료
				System.out.println("       프로그램을 종료합니다!");
				
				break;
		default : System.out.println("유효한 메뉴가 아닙니다 다시 선택해주세요!"); adminMain();
		}
	}
	//테스트용으로 만든 메인메서드
      public static void main(String[] args) {
		new AdminMain().adminMain();

	}
	

}
