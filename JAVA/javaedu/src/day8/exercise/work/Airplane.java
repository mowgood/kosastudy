package day8.exercise.work;

public class Airplane extends Plane{
	public Airplane() {
	}
	public Airplane(String planeName, int fuelSize) {
		super(planeName, fuelSize);
	}
	public void flight(int distance) {
		super.setFuelSize(super.getFuelSize() - distance * 3);
	}

}
