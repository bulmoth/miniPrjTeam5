package main;

import productAddDelete.AdminMain;
import util.MyUtil;

public class Main {

	public static void main(String[] args) {
		
		
		
		System.out.println("야옹아 멍멍해봐 반려동물 플랫폼입니다.");
		System.out.println("사용자 타입을 선택해주세요");		
		System.out.println();
		System.out.println("---------------------");
		System.out.println();
		System.out.println("1. 구매자");
		System.out.println("2. 관리자");
		System.out.println();
		System.out.println("---------------------");
		
		int n = MyUtil.scInt();
		
		switch(n) {
		case 1 : 
			new CustomerMain().CustomMain(); break; //구매자 --구매자 메인 페이지로 이동
		case 2 : 
			new AdminMain().adminMain(); break;// 관리자
		default : System.out.println("선택하신 계정은 유효하지 않습니다.");
		}
		
		
		
		
		
		
	}

}
