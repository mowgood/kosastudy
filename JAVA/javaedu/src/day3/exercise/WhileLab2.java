package day3.exercise;

public class WhileLab2 {

	public static void main(String[] args) {
		int pairNum1;
		int pairNum2;
		
		while(true) {
			pairNum1 = (int)(Math.random()*6)+1;
			pairNum2 = (int)(Math.random()*6)+1;
			
			if(pairNum1 == pairNum2) {
				System.out.print("게임 끝");
				break;
			}
			
			if(pairNum1 > pairNum2) 
				System.out.println(pairNum1 + "이 " + pairNum2 + " 보다 크다");
			else
				System.out.println(pairNum1 + "이 " + pairNum2 + " 보다 작다");
			
		}
		
		

	}

}
