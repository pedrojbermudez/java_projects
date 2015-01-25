/*
 * @author Pedro Javier Bermudez Araguez
 * 
 */
import java.io.IOException;
import java.io.PrintWriter;

import secretSanta.Club;
import secretSanta.ClubCouple;

public class MainCouple {
	public static void main(String [] args) throws IOException {
		Club club = new ClubCouple("members.txt");
		club.makeFriends();
		try (PrintWriter pw = new PrintWriter(System.out, true)) {
			club.displayFriends(pw);
		}
	}
}
