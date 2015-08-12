public class MinimumRotatedArray {

	/*
	 * Suppose a sorted array is rotated at some pivot unknown to you
	 * beforehand. (i.e., 0 1 2 4 5 6 7 might become 4 5 6 7 0 1 2).
	 * 
	 * Find the minimum element.You may assume no duplicate exists in the array.
	 */
	public static void main(String[] args) {
		// In my example I let a rotated array.
		int[] arr = { 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34,
				35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
				51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66,
				67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82,
				83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98,
				99, 100, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
				17, 18, 19, 20, };
		System.out.println("The minimum value is: " + minimumValue(arr));
	}

	public static int minimumValue(int[] arr) {
		long start = System.nanoTime();
		if (arr == null || arr.length == 0) {
			return 0;
		} else if (arr.length == 1) {
			long end = System.nanoTime();
			System.out.println("Executed min in: " + ((end - start)));
			return arr[0];
		} else if (arr.length == 2) {
			if (arr[0] > arr[1]) {
				long end = System.nanoTime();
				System.out.println("Executed min in: " + ((end - start)));
				return arr[1];
			}
			long end = System.nanoTime();
			System.out.println("Executed min in: " + ((end - start)));
			return arr[0];
		}
		if (arr[0] == arr[arr.length - 1]) {
			long end = System.nanoTime();
			System.out.println("Executed min in: " + ((end - start)));
			return arr[0];
		}
		int middle = (arr.length) / 2;
		int min1 = arr[middle];

		// right side
		for (int i = arr.length - 1; i >= middle; i--) {
			if (arr[i] < min1) {
				min1 = arr[i];
			} else {
				break;
			}
		}

		// left side
		for (int i = middle - 1; i >= 0; i--) {
			if (arr[i] < min1) {
				min1 = arr[i];
			} else {
				long end = System.nanoTime();
				System.out.println("sExecuted min in: " + ((end - start)));
				return min1;
			}
		}

		long end = System.nanoTime();
		System.out.println("Executed min in: " + ((end - start)));
		return min1;
	}
}
