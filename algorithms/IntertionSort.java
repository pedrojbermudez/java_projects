import java.util.LinkedList;
import java.util.Random;

public class InsertionSort {
  /*
   * Insertion Sort List:
   * 
   * Sort a linked list using insertion sort.
   *
   */
  public static void main(String[] args) {
    LinkedList<Integer> linkedL = new LinkedList<Integer>();
    for(int i = 0; i < 1000; i++){
      Random rnd = new Random();
      linkedL.add(rnd.nextInt(10000));
    }
    System.out.println("Before -> " + linkedL);
    sortedList(linkedL);
    System.out.println("After -> " + linkedL);
  }

  public static void sortedList(LinkedList<Integer> linkedL) {
    int i = 0;
    for(i = 1; i < linkedL.size(); i++){
      int x = linkedL.get(i);
      int j = i;
      while(j > 0 && linkedL.get(j-1) > x){
        linkedL.set(j, linkedL.get(j-1));
        j--;
      }
      linkedL.set(j, x);
    }
  }
}
