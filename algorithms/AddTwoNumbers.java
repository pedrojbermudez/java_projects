import java.util.LinkedList;

public class AddTwoNumbers {
  /*
   * You are given two linked lists representing two non-negative numbers. The
   * digits are stored in reverse order and each of their nodes contain a
   * single digit. Add the two numbers and return it as a linked list.
   * 
   * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4) Output: 7 -> 0 -> 8
   */

  public static void main(String[] args) {
    LinkedList<Integer> l1 = new LinkedList<Integer>();
    LinkedList<Integer> l2 = new LinkedList<Integer>();
    l1.add(2);
    l1.add(4);
    l1.add(9);
    l1.add(3);
    l2.add(5);
    l2.add(6);
    l2.add(1);
    l2.add(4);
    l2.add(7);
    System.out.println(addNumbers(l1, l2));
  }

  public static LinkedList<Integer> addNumbers(LinkedList<Integer> l1,
      LinkedList<Integer> l2) {
    int carry = 0;
    int getL1 = 0, getL2 = 0;
    LinkedList<Integer> res = new LinkedList<Integer>();
    while (!l1.isEmpty() || !l2.isEmpty()) {
      if (!l1.isEmpty()) {
        getL1 = l1.pop();
      } else {
        getL1 = 0;
      }
      if (!l2.isEmpty()) {
        getL2 = l2.pop();
      } else {
        getL2 = 0;
      }
      if (getL1 + getL2 >= 10) {
        res.add((getL1 + getL2 - 10));
        carry = 1;
      } else {
        res.add(getL1 + getL2 + carry);
        carry = 0;
      }
    }
    if(carry == 1){
      res.add(1);
    }
    return res;
  }
}
