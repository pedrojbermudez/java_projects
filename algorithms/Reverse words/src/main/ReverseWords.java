package main;

public class ReverseWords {
  /*
   * Given an input string, reverse the string word by word.
   * 
   * For example, given s = "the sky is blue", return "blue is sky the".
   */

  public static void main(String[] args) {
    String example = "This is an example";
    System.out.println(example + "\n" + reverseWords(example));
  }

  public static String reverseWords(String str) {
    if(str == null || str.length() == 0){
      return "";
    }
    String[] str_split = str.split(" ");
    StringBuilder sb = new StringBuilder();
    for (int i = str_split.length - 1; i >= 0; i--) {
      if (i != 0) {
        sb.append(str_split[i] + " ");
      } else {
        sb.append(str_split[i]);
      }
    }
    return sb.toString();
  }
}
