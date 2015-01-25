/*
 * @author Pedro Javier Bermudez Araguez
 * 
 */
package secretSanta;

class Couple {
	private Person first, second;

	public Couple(Person u, Person o) {
		this.first = u;
		this.second = o;
	}

	public String toString() {
		return "(" + first.getName() + "," + second.getName() + ")";
	}

	public boolean equals(Object o) {
		boolean res = o instanceof Couple;
		Couple p = res ? (Couple) o : null;
		return (this.first.compareTo(p.first) == 0
				&& this.second.compareTo(p.second) == 0)
				|| (this.first.compareTo(p.second) == 0
				&& this.second.compareTo(p.first) == 0);
	}

	public int hashCode() {
		return this.first.hashCode() + this.second.hashCode();
	}
}
