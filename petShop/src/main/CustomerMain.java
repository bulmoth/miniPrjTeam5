package main;


import adopt_reg.AdoptRegMem;
import cart.Cart;
import member.Member;
import order.Order;
import question.Question;
import util.MyUtil;

public class CustomerMain {
	
	private int selectNum;

	public void CustomMain() {
		
		//메인페이지
		while(true) {
		System.out.println("야옹아멍멍해 반려동물 관련 서비스 플랫폼입니다.");
		System.out.println();
		System.out.println("원하시는 서비스를 선택해주세요");
		System.out.println();
		System.out.println("---------------------");
		System.out.println();
		System.out.println("1. 회원가입");
		System.out.println("2. 로그인");
		System.out.println("3. 회원 정보 수정");
		System.out.println("4. 상품 구매");
		System.out.println("5. 장바구니");
		System.out.println("6. 주문 조회");
		System.out.println("7. 리뷰 작성");
		System.out.println("8. 입양");
		System.out.println("9. 고객 센터 문의");
		System.out.println();
		System.out.println("---------------------");
		
		selectNum = MyUtil.scInt();
		
		switch(selectNum) {
		case 1 : 
			new Member().create(); break; //회원가입
		case 2 : 
			new Member().login();break; // 로그인
		case 3 : 
			new Member().update(); break; //회원정보 수정
		case 4 : 
			new CustomerShop().Shop(); break; //상품구매
		case 5 : 
			new Cart().cartMain(); break; //장바구니
		case 6 : 
			new Order().orderMain();  break; //주문 조회
		case 7 : 
			new CustomerReview().ReviewMain(); break;
		case 8 : 
			new AdoptRegMem().adoptRegMem(); break; //입양
		case 9 : 
			new Question().qMain(); break; //고객센터 문의
			
		default : System.out.println("선택하신 메뉴는 유효하지 않습니다."); CustomMain();
		}
	}
		
		
		

	}

}
