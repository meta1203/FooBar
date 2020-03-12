import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeyIAlreadyDidThat {
	public static List<String> results = new ArrayList<String>();
	
	public static void main(String[] args) {
		System.out.println(solution("210022", 3));
		System.out.println(solution("10001001", 2));
		System.out.println(solution("1211", 10));
	}

	public static int solution(String n, int b) {
		final String base = "0123456789ABCDEF";

		char[] tempArray = n.toCharArray();
		int k = tempArray.length;

		Arrays.sort(tempArray);
		String y = new String(tempArray); // ascending order

		String x = ""; // descending order
		for (int ptr = 0; ptr < tempArray.length; ptr++) {
			x = tempArray[ptr] + x;
		}

		int zTemp = Integer.valueOf(x, b) - Integer.valueOf(y, b);
		String z = "";
		while (zTemp > 0) {
			z = base.charAt(zTemp % b) + z;
			zTemp = ((Number)Math.floor(zTemp / b)).intValue();
		}

		if (z.length() < k) {
			int lel = k - z.length();
			for (int curr = 0; curr < lel; curr++) {
				z = "0" + z;
			}
		}
		
		if (results.contains(z)) { // we've hit this number before, find out how long the pattern is
			return results.size() - results.indexOf(z);
		} else {
			results.add(z);
			return solution(z, b);
		}
	}
}
