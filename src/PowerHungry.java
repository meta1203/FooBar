import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PowerHungry {
	public static void main(String[] args) {
		int[] t1 = {2, 0, 2, 2, 0};
		System.out.println(solution(t1));
		int[] t2 = {-2, -3, 4, -5};
		System.out.println(solution(t2));
		int[] t3 = {2,-3,1,0,-5};
		System.out.println(solution(t3));
		int[] zeros = {0,0,0,0,0};
		System.out.println(solution(zeros));
		int[] onePos = {5};
		System.out.println(solution(onePos));
		int[] oneNeg = {-5};
		System.out.println(solution(oneNeg));
		int[] t5 = {7, -8, -9, 10, -11};
		System.out.println(solution(t5));
		int[] t6 = {-7, 0, 0, 0};
		System.out.println(solution(t6));
		int[] t7 = new int[50];
		for (int x = 0; x < 50; x++) {
			t7[x] = (int)(Math.round(Math.random()*2000)-1000);
		}
		System.out.println(solution(t7));
	}
	
	public static String solution(int[] xs) {
		List<Integer> pos = new ArrayList<Integer>();
		List<Integer> neg = new ArrayList<Integer>();
		int zero = 0;
		
		for (int x  : xs) {
			if (x > 0) {
				pos.add(x);
			} else if (x < 0) {
				neg.add(x);
			} else {
				zero++;
			}
		}
		
		if (zero == xs.length) { // return zero if all zeros
			return "0";
		}
		
		if (neg.size() == 1 && zero == 0) { // return negative if only negative
			return neg.get(0).toString();
		}
		
		if (neg.size() == 1 && zero > 0) { // return 0 if only one negative and zeros
			return "0";
		}
		
		BigInteger accumulator = BigInteger.ONE;
		
		if (neg.size() % 2 == 1) {
			Collections.sort(neg);
			Collections.reverse(neg);
			// System.out.println(neg);
			neg.remove(0);
		}
		
		for (Integer x : pos) {
			accumulator = accumulator.multiply(BigInteger.valueOf(x));
		}
		
		for (Integer x : neg) {
			accumulator = accumulator.multiply(BigInteger.valueOf(x));
		}
		
		return accumulator.toString();
	}
}
