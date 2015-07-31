public class RotateImage {

	public static void main(String[] args) {
		/*
		 * You are given an n x n 2D matrix representing an image.
		 * 
		 * Rotate the image by 90 degrees (clockwise).
		 * 
		 * Follow up: Could you do this in-place?
		 */
		int[][] matrix = new int[10][2];

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = (10 * i) + (j + 1);
			}
		}
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] > 9)
					System.out.print(matrix[i][j] + " ");
				else
					System.out.print(" " + matrix[i][j] + " ");
			}
			System.out.println();
		}
		rotateInplace(matrix);
		System.out.println();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] > 9)
					System.out.print(matrix[i][j] + " ");
				else
					System.out.print(" " + matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void rotateInplace(int[][] matrix) {
		int mid1 = (int) Math.ceil((float) matrix.length / 2);
		int mid2 = (int) Math.ceil((float) matrix[0].length / 2);

		for (int i = 0; i < mid1; i++) {
			for (int j = 0; j < mid2; j++) {
				int tmp = matrix[i][j];
				matrix[i][j] = matrix[matrix.length - 1 - i][j];
				matrix[matrix.length - 1 - i][j] = matrix[matrix.length - 1 - i][matrix[i].length
				                                         						- 1 - j];
				matrix[matrix.length - 1 - i][matrix[i].length - 1 - j] = matrix[i][matrix[i].length
						- 1 - j];
				matrix[i][matrix[i].length - 1 - j] = tmp;
			}
		}
	}
}
