package main;

import java.util.Scanner;

public class RotateArray {

	public static void main(String[] args) {
		/*
		 * Rotate an array of n elements to the right by k steps. For example,
		 * with n = 7 and k = 3, the array [1,2,3,4,5,6,7] is rotated to
		 * [5,6,7,1,2,3,4]
		 */
		char c = 'd';
		char d = 'g';
		System.out.println((int) c);
		System.out.println((int) d);
		int[] n;
		int k, length;
		Scanner sc = new Scanner(System.in);
		do {
			System.out
					.println("Please introduce the length for the array (n>1)");
			length = sc.nextInt();
		} while (length < 2);
		n = new int[length];
		for (int i = 0; i < length; i++) {
			n[i] = i + 1;
		}
		do {
			System.out.println("Please introduce the index you wish to rotate");
			k = sc.nextInt();
			k--;
			System.out.println(k);
		} while (k < 0 && k == length);
		sc.close();
		System.out.print("Original array: [");
		for (int i = 0; i < length; i++) {
			if (i != length - 1) {
				System.out.print(n[i] + ", ");
			} else{
				System.out.print(n[i]);
			}
		}
		n = rotateArray(n, k);
		System.out.print("]\nRotated array: [");
		for (int i = 0; i < length; i++) {
			if (i != length - 1) {
				System.out.print(n[i] + ", ");
			} else{
				System.out.print(n[i]);
			}
		}
		System.out.println("]");
	}

	public static int[] rotateArray(int[] array, int index) {
		int[] tmp = new int[array.length];
		int count = 0;
		for (int i = index; i < array.length; i++) {
			tmp[count] = array[i];
			count++;
		}
		for (int i = 0; i < index; i++) {
			tmp[count] = array[i];
			count++;
		}
		return tmp;
	}

}
