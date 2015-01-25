/*
 * @author Pedro Javier Bermudez Araguez
 * 
 */
import java.io.IOException;
import java.io.PrintWriter;

import secretSanta.Club;

public class Main {
  public static void main(String [] args) throws IOException {
    Club club = new Club("members.txt");
    club.makeFriends();
    try (PrintWriter pw = new PrintWriter(System.out, true)) {
      club.displayFriends(pw);
    }
  }
}
