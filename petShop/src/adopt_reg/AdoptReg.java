package adopt_reg;

import util.MyUtil;

public class AdoptReg {
	
	public void adoptReg(){
		
		//플래그 세우기
		boolean isQuit = false;
		
		while(!isQuit) {
			
			//입양 등록
			System.out.println("========== 입양 ==========");
			System.out.println();
			System.out.println("입양 관련 페이지 입니다.");
			System.out.println("원하시는 기능을 선택하십시오.");
			System.out.println();
			System.out.println("1. 입양 목록 조회");
			System.out.println("2. 입양 등록");
			System.out.println("그 외 : 나가기");
			
			int select = MyUtil.scInt();
			switch(select) {
			case 1:
				adoptList(); break;
			case 2:
				register(); break;
				default : System.out.println("상위 메뉴로 돌아갑니다."); isQuit = true;
			}
			
		}
		//1과 2 이외의 것이 선택되었을 때 while 문 탈출 및 리턴
		return;
		
	}//adoptReg
	
	private void adoptList() {
		//입양 목록 조회
		
		
	}//adoptList
	
	
	private void register() {
		//입양 등록
		
	}//register

}//class
