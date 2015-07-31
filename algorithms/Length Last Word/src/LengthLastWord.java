public class LengthLastWord {

	public static void main(String[] args) {
		/*
		 * Given a string s consists of upper/lower-case alphabets and empty
		 * space characters ' ', return the length of last word in the string.
		 * If the last word does not exist, return 0.
		 */
		String str = "This is a test for the LengthLastWord";
		System.out.println(lengthLastWord(str));
	}
	
	
	public static int lengthLastWord(String str){
		if(str == null || str.length() == 0){
			return 0;
		}
		
		int len = 0;
		for(int i = str.length()-1; i >= 0; i--){
			char ch = str.charAt(i);
			if(ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z'){
				len++;
			} else if(ch == ' '){
				break;
			}
		}
		return len;
	}
}
