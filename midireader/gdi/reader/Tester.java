package gdi.reader;

import java.util.Random;

public class Tester {
	public static void main(String[] args) {
		Random generator = new Random();
		int randomIndex = generator.nextInt(1);

		float div = (float) 0.125;
		randomIndex = 0;
		randomIndex += div * 16;
		System.out.println(randomIndex);
	}
}
