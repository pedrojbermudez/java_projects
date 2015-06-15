import java.util.Scanner;
import ticTacToe.*;

public class MainGame {
	public static void main(String[] args) {
		int playersNumber, cell = 0;
		boolean done = false, win1 = false, win2 = false;
		Player player1 = null, player2 = null;
		Board board = new Board();
		Scanner keyboard = new Scanner(System.in); 
		do{
			System.out.println("Introduce the number of players (1/2): ");
			playersNumber = keyboard.nextInt();
		}while(playersNumber < 1 || playersNumber > 2);
		switch(playersNumber)
		{
			case 1:
				player2 = new Player("CPU", "O");
				System.out.println("Introduce your name: ");
				player1 = new Player(keyboard.next(), "X");
				do
				{
					System.out.println("CPU turn");
					player2.writeBoard();
					win2 = player2.winMatch();
					done = false;
					System.out.println(board);
					do
					{
						if(!win2)
						{
							do
							{
								System.out.println(player1.getPlayerName() + " turn");
								System.out.println("Introduce a number of cell " + 
										"1 to 9");
								cell = keyboard.nextInt();
							}while(cell < 1 || cell > 9);
							done = player1.writeBoard(cell-1);
							win1 = player1.winMatch();
							System.out.println(board);
						}
					}while(!done && !board.fullBoard() && !win2);
				}while(!win1 && !win2 &&
						!board.fullBoard());
				break;
			case 2:
				System.out.println("Introduce player 1 name: ");
				player1 = new Player(keyboard.next(), "X");
				System.out.println("Introduce player 2 name: ");
				player2 = new Player(keyboard.next(), "O");
				do
				{
					done = false;
					System.out.println(board);
					do
					{
						if(!win2)
						{
							do
							{
								System.out.println(player1.getPlayerName() + " turn");
								System.out.println("Introduce a number cell " + 
										"1 to 9");
								cell = keyboard.nextInt();
							}while(cell < 1 || cell > 9);
							done = player1.writeBoard(cell-1);
							win1 = player1.winMatch();
							System.out.println(board);
						}
					}while(!done && !board.fullBoard() && !win2);
					do
					{
						if(!win1)
						{
							do
							{
								System.out.println(player2.getPlayerName() + " turn");
								System.out.println("Introduce a number cell " + 
										"1 to 9");
								cell = keyboard.nextInt();
							}while(cell < 1 || cell > 9);
							done = player2.writeBoard(cell-1);
							win2 = player2.winMatch();
							System.out.println(board);
						}
					}while(!done && !board.fullBoard() && !win1);
				}while(!win1 && !win2 &&
						!board.fullBoard());
				break;
		}
		
		if(board.fullBoard() && !win1 && ! win2)
		{
			System.out.println("The match is a draw.");
		}
		else if(win1)
		{
			System.out.println("The winner is " + player1.getPlayerName());
		}
		else
		{
			System.out.println("The winner is " + player2.getPlayerName());
		}
		keyboard.close();
	}
}
