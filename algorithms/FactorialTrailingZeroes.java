public class FactorialTrailingZeroes {
  /*
  * Given an integer n, return the number of trailing zeroes in n!.
  *
  * Note: Your solution should be in logarithmic time complexity.
  */

  public static void main(String[] args) {
    System.out.println(trailingZeroes(15L));

  }
  public static long trailingZeroes(long n) {
    if (n < 0)
      return -1;
   
    long count = 0;
    for (long i = 5; n / i >= 1; i *= 5) {
      count += n / i;
    }
   
    return count;
  }
}
