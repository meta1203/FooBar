public class ILoveLanceJanice {
	public static String solution(String x) {
		final String key = "abcdefghijklmnopqrstuvwxyz";
		final String value = "zyxwvutsrqponmlkjihgfedcba";
		
		String ret = "";
		
		for (int ptr = 0; ptr < x.length(); ptr++) {
			if (Character.isLowerCase(x.charAt(ptr))) {
				ret = ret + value.charAt(key.indexOf(x.charAt(ptr)));
			} else {
				ret = ret + x.charAt(ptr);
			}
		}
		
		return ret;
	}
}
