package day6.exercise;

public class SalaryExam {

	public static void main(String[] args) {
		 int month = day5.exercise.MethodLab5.getRandom(1, 12);
		 int grade = day5.exercise.MethodLab5.getRandom(1, 4);
		 SalaryExpr salary;
		 
		 if(month % 2 == 0) { // 짝수달
			 salary = new SalaryExpr(100);
		 } else {
			 salary = new SalaryExpr();
		 }
		 
		 System.out.printf("%d월 %d등급의 월급은 %d입니다.", month, grade, salary.getSalary(grade));

	}

}
