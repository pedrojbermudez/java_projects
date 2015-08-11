import java.util.Arrays;
import java.util.Random;

public class CountingSort {
  /*
   * Given an array with n objects colored red, white or blue, sort them so
   * that objects of the same color are adjacent, with the colors in the order
   * red, white and blue.
   * 
   * Here, we will use the integers 0, 1, and 2 to represent the color red,
   * white, and blue respectively.
   */
  public static void main(String[] args) {
    // int[] n1 = { 0, 2, 1, 2, 1, 1, 1, 0, 0, 1, 0, 1, 0, 2, 0, 0 };
    // int[] n2 = Arrays.copyOf(n1, n1.length);
    int[] n1 = new int[5000];
    int[] n2 = new int[5000];

    for (int i = 0; i < n1.length; i++) {
      Random rnd = new Random();
      n1[i] = rnd.nextInt(3);
      n2[i] = n1[i];
    }

    n1 = countingSort(n1);
    n2 = countingSort(n2, 0, 2);
    System.out.println("n1{");
    for (int i = 0; i < n1.length; i++) {
      switch (n1[i]) {
      case 0:
        System.out.print("red ");
        break;

      case 1:
        System.out.print("white ");
        break;
      case 2:
        System.out.print("blue ");
        break;
      }
    }
    System.out.println("\n}\nn2{");
    for (int i = 0; i < n1.length; i++) {
      switch (n2[i]) {
      case 0:
        System.out.print("red ");
        break;

      case 1:
        System.out.print("white ");
        break;
      case 2:
        System.out.print("blue ");
        break;
      }
    }
    System.out.println("\n}");

  }

  public static int[] countingSort(int[] numbers) {
    // Counting Sort with no minimum and maximum value given.
    long start = System.nanoTime();
    int minimum = 0, maximum = 0;
    for (int i = 0; i < numbers.length; i++) {
      if (numbers[i] < minimum) {
        minimum = numbers[i];
      }
      if (numbers[i] > maximum) {
        maximum = numbers[i];
      }
    }
    int[] index = new int[maximum - minimum + 1];
    for (int number : numbers) {
      index[number - minimum]++;
    }
    int c = 0;
    for (int i = minimum; i <= maximum; i++) {
      while (index[i - minimum] > 0) {
        numbers[c++] = i;
        index[i - minimum]--;
      }
    }
    long end = System.nanoTime();
    System.out.println("Sort Slow executed in -> "
        + ((float) (end - start) / 1000000));
    return numbers;
  }

  public static int[] countingSort(int[] numbers, int minimum, int maximum) {
    // Counting Sort with minimum and maximum value given.
    long start = System.nanoTime();
    int[] index = new int[maximum - minimum + 1];
    for (int number : numbers) {
      index[number - minimum]++;
    }
    int c = 0;
    for (int i = minimum; i <= maximum; i++) {
      while (index[i - minimum] > 0) {
        numbers[c++] = i;
        index[i - minimum]--;
      }
    }
    long end = System.nanoTime();
    System.out.println("Sort Fast executed in -> "
        + ((float) (end - start) / 1000000));
    return numbers;
  }
}
import java.util.Arrays;
import java.util.Random;

public class CountingSort {
  /*
   * Given an array with n objects colored red, white or blue, sort them so
   * that objects of the same color are adjacent, with the colors in the order
   * red, white and blue.
   * 
   * Here, we will use the integers 0, 1, and 2 to represent the color red,
   * white, and blue respectively.
   */
  public static void main(String[] args) {
    // int[] n1 = { 0, 2, 1, 2, 1, 1, 1, 0, 0, 1, 0, 1, 0, 2, 0, 0 };
    // int[] n2 = Arrays.copyOf(n1, n1.length);
    int[] n1 = new int[5000];
    int[] n2 = new int[5000];

    for (int i = 0; i < n1.length; i++) {
      Random rnd = new Random();
      n1[i] = rnd.nextInt(3);
      n2[i] = n1[i];
    }

    n1 = countingSort(n1);
    n2 = countingSort(n2, 0, 2);
    System.out.println("n1{");
    for (int i = 0; i < n1.length; i++) {
      switch (n1[i]) {
      case 0:
        System.out.print("red ");
        break;

      case 1:
        System.out.print("white ");
        break;
      case 2:
        System.out.print("blue ");
        break;
      }
    }
    System.out.println("\n}\nn2{");
    for (int i = 0; i < n1.length; i++) {
      switch (n2[i]) {
      case 0:
        System.out.print("red ");
        break;

      case 1:
        System.out.print("white ");
        break;
      case 2:
        System.out.print("blue ");
        break;
      }
    }
    System.out.println("\n}");

  }

  public static int[] countingSort(int[] numbers) {
    // Counting Sort with no minimum and maximum value given.
    long start = System.nanoTime();
    int minimum = 0, maximum = 0;
    for (int i = 0; i < numbers.length; i++) {
      if (numbers[i] < minimum) {
        minimum = numbers[i];
      }
      if (numbers[i] > maximum) {
        maximum = numbers[i];
      }
    }
    int[] index = new int[maximum - minimum + 1];
    for (int number : numbers) {
      index[number - minimum]++;
    }
    int c = 0;
    for (int i = minimum; i <= maximum; i++) {
      while (index[i - minimum] > 0) {
        numbers[c++] = i;
        index[i - minimum]--;
      }
    }
    long end = System.nanoTime();
    System.out.println("Sort Slow executed in -> "
        + ((float) (end - start) / 1000000));
    return numbers;
  }

  public static int[] countingSort(int[] numbers, int minimum, int maximum) {
    // Counting Sort with minimum and maximum value given.
    long start = System.nanoTime();
    int[] index = new int[maximum - minimum + 1];
    for (int number : numbers) {
      index[number - minimum]++;
    }
    int c = 0;
    for (int i = minimum; i <= maximum; i++) {
      while (index[i - minimum] > 0) {
        numbers[c++] = i;
        index[i - minimum]--;
      }
    }
    long end = System.nanoTime();
    System.out.println("Sort Fast executed in -> "
        + ((float) (end - start) / 1000000));
    return numbers;
  }
}
