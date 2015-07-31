package main;

public class SetMatrixZeroes {

	public static void main(String[] args) {
		/*
		 * Given a m x n matrix, if an element is 0, set its entire row and
		 * column to 0. Do it in place.
		 */
		int[][] matrix = {{2,4,5,2},{3,0,1,0},{2,1,4,5}};
		setZeroes(matrix);
		int outsideLength = matrix.length;
		int insideLength = matrix[0].length;

		for (int i = 0; i < outsideLength; i++) {
			for (int j = 0; j < insideLength; j++) {
				System.out.print(" " + matrix[i][j]);
			}
			System.out.println();
		}

	}

	public static void setZeroes(int[][] matrix) {
		boolean isZero = false;
		for (int col = 0; col < matrix.length; col++) {
			if (!isZero) {
				for (int row = 0; row < matrix[col].length; row++) {
					if (matrix[col][row] == 0 && row == 0 && col == 0) {
						for (int i = 0; i < matrix.length; i++) {
							for (int j = 0; j < matrix[i].length; j++) {
								matrix[i][j] = 0;
							}
						}
						isZero = true;
						break;
					} else if (matrix[col][row] == 0) {
						// Columns set zero from started index until the last
						// index.
						for (int i = 0; i < matrix.length; i++) {
							matrix[i][row] = 0;
						}
						// Rows set zero from started index until the last
						// index.
						for (int i = 0; i < matrix[col].length; i++) {
							matrix[col][i] = 0;
						}
					}
				}
			} else{
				break;
			}
		}
	}
}
