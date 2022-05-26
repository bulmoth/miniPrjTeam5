package main;

public class CustomerMain {
	
	private int selectNum;

	public void CustomMain() {
		
		//메인페이지
		
		System.out.println("야옹아멍멍해 반려동물 관련 서비스 플랫폼입니다.");
		System.out.println();
		System.out.println("원하시는 서비스를 선택해주세요");
		System.out.println();
		System.out.println("---------------------");
		System.out.println();
		System.out.println("1. 회원가입");
		System.out.println("2. 로그인");
		System.out.println("3. 상품 구매");
		System.out.println("4. 배송 조회");
		System.out.println("5. 장바구니");
		System.out.println("6. 리뷰 작성");
		System.out.println("7. 입양");
		System.out.println("8. 호텔 신청");
		System.out.println("9 고객 센터 문의");
		System.out.println();
		System.out.println("---------------------");
		
		selectNum = MyUtil.sc.nextInt();
		
		switch(selectNum) {
		case 0 : 
			break; //회원가입
		case 1 : 
			break; // 로그인
		case 2 : 
			break; //상품구매
		case 3 : 
			break; //배송 조회
		case 4 : 
			break; //장바구니
		case 5 : 
			break; // 리뷰 작성
		case 6 : 
			break; //입양
		case 7 : 
			break; //호텔 신청
		case 8 : 
			break; //고객센터 문의
			
		default : System.out.println(); CustomMain();
		}
		

	}

}
