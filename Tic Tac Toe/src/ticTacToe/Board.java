package ticTacToe;

public class Board {
	private static String[] board;

	public Board() {
		board = new String[9];
	}

	public boolean writeBoard(int position, String tile) {
		if (board[position] == null) {
			board[position] = tile;
			return true;
		} else {
			return false;
		}
	}

	public boolean fullBoard() {
		for (int i = 0; i < board.length; i++) {
			if (board[i] == null) {
				return false;
			}
		}
		return true;
	}

	public boolean winner(String tile) {
		// Check the 3 rows, columns and diagonals.
		if (rowWinner(0, tile) || rowWinner(3, tile)
				|| rowWinner(6, tile)
				|| columnWinner(0, tile)
				|| columnWinner(1, tile)
				|| columnWinner(2, tile)
				|| diagonal1Winner(tile)
				|| diagonal2Winner(tile)) {
			return true;
		}
		return false;
	}

	public boolean rowWinner(int position, String tile) {
		if (board[position] != null && board[position].equals(tile)
				&& board[position + 1] != null
				&& board[position + 1].equals(tile)
				&& board[position + 2] != null
				&& board[position + 2].equals(tile)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean columnWinner(int position, String tile) {
		if (board[position] != null && board[position].equals(tile)
				&& board[position + 3] != null
				&& board[position + 3].equals(tile)
				&& board[position + 6] != null
				&& board[position + 6].equals(tile)) {
			return true;
		}
		return false;
	}

	public boolean diagonal1Winner(String tile) {
		if (board[0] != null && board[0].equals(tile)
				&& board[4] != null && board[4].equals(tile)
				&& board[8] != null && board[8].equals(tile)) {
			return true;
		}
		return false;
	}

	public boolean diagonal2Winner(String tile) {
		if (board[2] != null && board[2].equals(tile)
				&& board[4] != null && board[4].equals(tile)
				&& board[6] != null && board[6].equals(tile)) {
			return true;
		}
		return false;
	}

	public String toString() {
		String theBoard = "";
		int i = 0;
		for (String tile : board) {
			i++;
			if (tile != null) {
				theBoard += i + ":" + tile + " ";
				if (i % 3 == 0) {
					theBoard += "\n";
				}
			} else {
				theBoard += i + ":_ ";
				if (i % 3 == 0) {
					theBoard += "\n";
				}
			}
		}
		return theBoard;
	}
}
