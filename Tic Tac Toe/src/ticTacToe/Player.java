package ticTacToe;

import java.util.Random;

public class Player {
	private String playerName, tile;
	private static int playerNumber = 1;
	private static Board board = new Board();

	public Player() {
		this("Player" + playerNumber, "X");
		playerNumber++;
	}

	public Player(String player) {
		this(player, "X");
	}

	public Player(String player, String playerTile) {
		this.playerName = player;
		this.tile = playerTile;
	}

	public boolean writeBoard(int pos) {
		return board.writeBoard(pos, this.tile);
	}

	public void writeBoard() {
		boolean done = false;
		int random;
		Random rnd = new Random();
		do {
			random = rnd.nextInt(8);
			done = board.writeBoard(random, this.tile);
		} while (!done);
	}

	public boolean winMatch() {
		return board.winner(this.tile);
	}

	public String getPlayerName() {
		return this.playerName;
	}

	public String getTile() {
		return this.tile;
	}

	public String toString() {
		return "Name => " + this.playerName + "\nTile => " + this.tile;
	}
}
