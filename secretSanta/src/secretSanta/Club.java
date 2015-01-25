/*
 * @author Pedro Javier Bermudez Araguez
 * 
 */
package secretSanta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Club {
  protected List<Person> members;

  /**
   * Constructor for Club class.
   * 
   * @param listMembers
   *            - String containing a text file with the club members.
   * @throws FileNotFoundException
   *             - An exception when the file was not found.
   */
  public Club(String listMembers) throws FileNotFoundException {
    try (Scanner scMembers = new Scanner(new File(listMembers))) {
      readMembers(scMembers);
    }
  }

  /**
   * Method to read the members.
   * 
   * @param sc
   *            - Scanner to read the members in a file.
   */
  protected void readMembers(Scanner sc) {
    members = new ArrayList<Person>();
    while (sc.hasNextLine()) {
      try (Scanner scMembers = new Scanner(sc.nextLine())) {
        scMembers.useDelimiter("[ ,;-]+");
        while (scMembers.hasNext()) {
          members.add(new Person(scMembers.next()));
        }
      }
    }
  }

  /**
   * Check number of friends and throw a FriendException when the number is 2
   * or less. If the number is 3 or more set a friend to a person.
   */
  public void makeFriends() {
    if (members.size() <= 2) {
      throw new FriendException("Number of friends insufficient.");
    }
    List<Integer> posFriends = new ArrayList<>();
    for (int i = 0; i < members.size(); i++) {
      posFriends.add(i);
    }

    while (mistmatch(posFriends)) {
      Collections.shuffle(posFriends);
    }

    for (int i = 0; i < members.size(); i++) {
      members.get(i).setFriend(members.get(posFriends.get(i)));
    }
  }

  /**
   * Check if friend position is the same person or not. False is not the same
   * person. True is the same person.
   * 
   * @param pos
   * @return
   */
  private static boolean mistmatch(List<Integer> pos) {
    int i = 0;
    while (i < pos.size()) {
      if (i == pos.get(i)) {
        return true;
      }
      i++;
    }
    return false;
  }

  /**
   * Display the relation between two persons.
   * @param out - String with relation friends.
   * @throws IOException - Throws an IOException when it is impossible create out.
   */
  public void displayFriends(String out) throws IOException {
    try (PrintWriter pw = new PrintWriter(out)) {
      displayFriends(pw);
    }
  }

  /**
   * Display the relation between two persons.
   * @param pw - PrintWrite for displaying the relation between two person.
   */
  public void displayFriends(PrintWriter pw) {
    Collections.sort(members);
    for (int i = 0; i < members.size(); i++) {
      pw.println(members.get(i));
    }
  }
}
