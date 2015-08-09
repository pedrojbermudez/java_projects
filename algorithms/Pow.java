public class Pow {
  /* Implement pow(x, n). */
  public static void main(String[] args) {
    System.out.println(pow(7, 100));
  }

  public static double pow(double x, int n) {
    if (x == 0) {
      return 0;
    }
    if (n == 0) {
      return 1;
    }
    double res = 1;
    for (int i = 0; i < n; i++) {
      res *= x;
    }
    return res;
  }
}
