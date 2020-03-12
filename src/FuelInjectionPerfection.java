import java.math.BigInteger;

public class FuelInjectionPerfection {
	public static void main(String[] args) {
		System.out.println("4 -> " + solution("4"));
		System.out.println("15 -> " + solution("15"));
		System.out.println("768 -> " + solution("768"));
		System.out.println("7 -> " + solution("7"));
		System.out.println("6 -> " + solution("6"));
	}

	public static int solution(String x) {
		BigInteger bint = new BigInteger(x);
		BigInteger two = BigInteger.valueOf(2);
		BigInteger three = BigInteger.valueOf(3);
		int counter = 0;
		
		while (!bint.equals(BigInteger.ONE)) {
			byte test = bint.and(BigInteger.valueOf(3)).byteValue();
//			System.out.println(bint.toString() + " | " + Integer.toBinaryString(test));
			
			if (test == 0 || test == 2) {
				bint = bint.divide(two);
			} else if (test == 1 || bint.equals(three)) {
				bint = bint.subtract(BigInteger.ONE);
			} else {
				bint = bint.add(BigInteger.ONE);
			}
			counter++;
		}

		return counter;
	}
}
