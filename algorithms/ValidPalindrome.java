public class ValidPalindrome {
	/*
	 * Given a string, determine if it is a palindrome, considering only
	 * alphanumeric characters and ignoring cases.
	 * 
	 * For example, "Red rum, sir, is murder" is a palindrome, while
	 * "Programcreek is awesome" is not.
	 * 
	 * Note: Have you consider that the string might be empty? This is a good
	 * question to ask during an interview.
	 * 
	 * For the purpose of this problem, we define empty string as valid
	 * palindrome.
	 */
	public static void main(String[] args) {
		System.out.println(validPalindrome("Red rum, sir, is murder"));
	}

	public static boolean validPalindrome(String str) {
		if(str.isEmpty()){
			return true;
		}
		str = str.toLowerCase().replaceAll("[^0-9a-zA-Z]", "");
		for(int i = 0; i <= str.length() / 2; i++){
			System.out.println(str.charAt(i) + " = " + str.charAt(str.length()-1-i));
			if(str.charAt(i) != str.charAt(str.length()-1-i)){
				return false;
			}
		}
		return true;
	}
}
