package day2;

public class OperTest6 {

	public static void main(String[] args) {
		int num1=100, num2=50;
		
		System.out.println(num1+num2);
		System.out.println(num1-num2);
		System.out.println(num1*num2);
		System.out.println(num1/num2);
		System.out.println(num1%num2);
		
		System.out.println(num1>num2);
		System.out.println(num1<num2);
		System.out.println(num1==num2); // equal 두 항의 값이 동일하면 true
		System.out.println(num1!=num2); // not equal 두 항의 값이 다르면 true
		
		System.out.println(num1>0 && num2>0);
		System.out.println(num1>0 && num2==0); // 첫번째 항이 거짓이면 두번째 항은 실행하지 않음
		
		System.out.println(num1>0 || num2>0); // 첫번째 항이 참이면 두번째 항은 실행하지 않음
		System.out.println(num1>0 || num2==0);
		
		System.out.println(num1 + 10 > 110 && num2 % 2 == 0);
		System.out.println(num1>0 && num2 % 2 == 1);

	}

}
