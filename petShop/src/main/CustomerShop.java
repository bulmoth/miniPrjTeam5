package main;

import java.sql.Connection;

import oracleDB.OracleDB;
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
			new CustomerShop().Dogfood(); break; //개 사료
		case 1 : 
			new CustomerShop().Catfood(); break; // 고양이 사료
		case 2 : 
			new CustomerShop().DogTreat(); break; //개 간식
		case 3 : 
			new CustomerShop().CatTreat(); break; //고양이 간식
		case 4 : 
			new CustomerShop().Toys(); break; //장난감
		case 5 : 
			new CustomerShop().Extra(); break; // 기타
			
		default : System.out.println("선택하신 메뉴는 유효하지 않습니다."); Shop();
		}
	}//Shop
	
	
	//상품 카테고리 메소드
	public void Dogfood() {
		
		System.out.println("=========개 사료 페이지입니다.=========");
		
		Connection conn = OracleDB.getOracleConnection();
		String sql = "SELECT * FROM PRODUCT";
		
	}//Dogfood
	
	public void Catfood() {
		System.out.println("=========고양이 사료 페이지입니다.=========");
		
	}//Catfood
	
	public void DogTreat() {
		System.out.println("=========개 간식 페이지입니다.=========");
		
	}//DogTreat
	
	public void CatTreat() {
		System.out.println("=========고양이 간식 페이지입니다.=========");
	}//CatTreat
	
	public void Toys() {
		System.out.println("=========장난감 페이지입니다.=========");
		
	}//Toys
	
	public void Extra() {
		System.out.println("=========기타 상품 페이지입니다.=========");
		
	}//Extra
	
	

}
