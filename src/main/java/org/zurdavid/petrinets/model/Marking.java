package org.zurdavid.petrinets.model;

/**
 * The class is a data class storing a marking of a Petri net. It contains the
 * number of tokens on each place ordered alphabetically by the places ids. It
 * does not contain any information about the places.
 * 
 * @author David Zurschmitten
 */
public class Marking {
	private int[] marking;

	/**
	 * @param marking An array of int containing the number of tokens in each place
	 *                ordered alphabetically by their ids.
	 */
	public Marking(int[] marking) {
		this.marking = marking;
	}

	/**
	 * @param index The index of the place in the alphabetically list of place ids.
	 * @return Number of tokens in that place for the given marking.
	 */
	public int getTokensAt(int index) {
		return marking[index];
	}

	/**
	 * @return The length of the marking as an in, which is equal to the number of
	 *         places in the Petri net.
	 */
	public int length() {
		return marking.length;
	}

	/**
	 * The method returns the marking represented as a String. The Strings are build
	 * as follows: "(x1|x2|...|xn)", where x1...xn are the number of tokens.
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("(");
		for (int i : marking) {
			s.append(String.valueOf(i) + "|");
		}
		s.deleteCharAt(s.length() - 1);
		s.append(")");
		return s.toString();
	}

	/**
	 * Two markings are equal, if they are of equal length and the number of tokens
	 * for each place is equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Marking)
			return toString().equals(obj.toString());
		else
			return false;
	}

	/**
	 * The method returns the return value of hashCode() of its String
	 * representation.
	 */
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}