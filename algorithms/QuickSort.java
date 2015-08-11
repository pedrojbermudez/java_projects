public class QuickSort {
  /*
   * Quicksort is a divide and conquer algorithm. It first divides a large
   * list into two smaller sub-lists and then recursively sort the two
   * sub-lists. If we want to sort an array without any extra space, quicksort
   * is a good option. On average, time complexity is O(n log(n)).
   * 
   * The basic step of sorting an array are as follows:
   * 
   * Select a pivot, normally the middle one From both ends, swap elements and
   * make all elements on the left less than the pivot and all elements on the
   * right greater than the pivot Recursively sort left part and right part
   */
  public static void main(String[] args) {
    int[] nums = { 5, 3, 7, 6, 2, 1, 4 };
    quickSort(nums, 0, nums.length-1);
    for (int num : nums) {
      System.out.print(" " + num);
    }
  }

  public static void quickSort(int[] numbers, int left, int right) {
    int pivot = numbers[left + (right - left) / 2];
    int i = left;
    int j = right;
    while(i <=j){
      while(numbers[i] < pivot){
        i++;
      }
      while(numbers[j] > pivot){
        j--;
      }
      if(i<=j){
        int tmp = numbers[i];
        numbers[i] = numbers[j];
        numbers[j] = tmp;
        i++;
        j--;
      }
      if(left < j){
        quickSort(numbers, left, j);
      }
      if(right > i){
        quickSort(numbers, i, right);
      }
    }
    
  }

}
