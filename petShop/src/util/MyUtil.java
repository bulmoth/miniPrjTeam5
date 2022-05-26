package util;

import java.util.Scanner;

public class MyUtil {
	
	public static Scanner sc = new Scanner(System.in);
	
	
	public static int scInt() {
		int n = sc.nextInt();
		sc.nextLine();			
		return n;
	}

}
