package main;

import util.MyUtil;

public class CustomerShop {
	
	private int selectNum;
	
	public void Shop() {
		
		//상품 구매
		System.out.println("원하시는 상품을 선택해 주세요.");
		System.out.println("----------------------");
		System.out.println("1. 개 사료");
		System.out.println("2. 고양이 사료");
		System.out.println("3. 개 간식");
		System.out.println("4. 고양이 간식");
		System.out.println("5. 장난감");
		System.out.println("6. 기타");
		System.out.println("----------------------");
		
		selectNum = MyUtil.sc.nextInt();
		
		switch(selectNum) {
		case 0 : 
			break; //개 사료
		case 1 : 
			break; // 고양이 사료
		case 2 : 
			break; //개 간식
		case 3 : 
			break; //고양이 간식
		case 4 : 
			break; //장난감
		case 5 : 
			break; // 기타
			
		default : System.out.println(); Shop();
		}
	}

}
