/*
 * @author Pedro Javier Bermudez Araguez
 * 
 */
package secretSanta;

import java.io.*;
import java.util.*;

public class ClubCouple extends Club {
  private Set<Couple> couples;

  public ClubCouple(String listMembers) throws IOException {
    super(listMembers);
  }

  public void readMembers(Scanner sc) {
    members = new ArrayList<Person>();
    couples = new HashSet<Couple>();
    while (sc.hasNext()) {
      try (Scanner coup = new Scanner(sc.next())) {
        coup.useDelimiter("[ ,;]+");
        while (coup.hasNext()) {
          String[] indiv = coup.next().split("-");
          if (indiv.length == 1) {
            members.add(new Person(indiv[0]));
          } else if (indiv.length == 2) {
            couples.add(new Couple(new Person(indiv[0]),
                new Person(indiv[1])));
            members.add(new Person(indiv[0]));
            members.add(new Person(indiv[1]));
          } else {
            throw new FriendException(
                "Couple can\'t be include 3 persons or more");
          }
        }
      }
    }
  }

  /**
   * Make all relation friends and check if the friend is its couple.
   */
  public void makeFriends() {
    boolean equalCouple = true;
    while (equalCouple) {
      super.makeFriends();
      int i = 0;
      equalCouple = false;
      while (i < members.size() && !equalCouple) {
        equalCouple = couples.contains(new Couple(new Person(members
            .get(i).getName()), members.get(i).getFriend()));
        i++;
      }
    }
  }
}
