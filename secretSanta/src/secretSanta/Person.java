package secretSanta;
class Person implements Comparable<Person> {
	private String name;
	private Person friend;
		
	public Person(String n) {
		this.name = n;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Person getFriend() {
		return this.friend;
	}
	
	public void setFriend(Person a) {
		this.friend = a;
	}
	
	public boolean equals(Object o) {
		boolean res = o instanceof Person;
		Person p = res ? (Person) o : null;
		return res && p.name.toLowerCase().equals(this.name.toLowerCase());
	}
	
	public int hashCode() {
		return this.name.toLowerCase().hashCode();
	}
	
	public int compareTo(Person obj) {
		return this.name.compareTo(obj.name);
	}
	
	public String toString() {
		return name + " --> " + (friend != null ? friend.name : " without friend");
	}
	
}
