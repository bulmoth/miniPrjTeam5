package mgr_review;

import util.MyUtil;

public class M_review {
	
	private int selectNum;
	
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
					
					selectNum = MyUtil.sc.nextInt();
					
					switch(selectNum) {
					case 0 : //리뷰조회
						
						
						
						
						//
						break;
					case 1 : //리뷰삭제
						//
						break;
					case 2 : //관리자 메뉴로 돌아가기
						//
						break;
					default : System.out.println("유효한 메뉴가 아닙니다 다시 선택해주세요!"); m_review();
			
				}
	
		
	}
	
	
	
	
	public void showReview() {//리뷰 조회
		
		System.out.println("─────────────────────────────────");
		System.out.println(); 
		System.out.println("     어떤 순서로 조회하시겠습니까?");
		System.out.println("       [0] 별점순으로");
		System.out.println("       [1] 게시글번호 순서대로");
		System.out.println("       [2] 이전 메뉴로 돌아가기");
		System.out.println();
		System.out.println("─────────────────────────────────");
		
		
		
		
		
		
	}
	
	
	
	public void deleteReview() {//리뷰삭제
		
		
		
		
		
		
	}
	
	
	public void cancleReview() {//관리자 메뉴로 돌아가기
		System.out.println("      관리자 메뉴로 돌아갑니다");
		return;
	
}
	
	

}
