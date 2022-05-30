package org.zurdavid.petrinets.model;

import org.zurdavid.petrinets.controller.PNMLParser;

/**
 * The class is a data class, derived from {@link PetrinetNode} and implements
 * properties (as well as getters and setters) which only appear in places.
 * 
 * Additional properties:
 * <ul>
 * <li>tokens: number of tokens
 * </ul>
 * 
 * @author David Zurschmitten
 */
public class Place extends PetrinetNode {
	private int tokens;

	/**
	 * Nodes are only given an id when constructed. All other attributes are set
	 * later. This is due to how the pnml Parsers works. See {@link PNMLParser}
	 * 
	 * @param id The nodes id.
	 */
	public Place(String id) {
		super(id);
	}

	/**
	 * Sets the number of tokens in the place.
	 * 
	 * @param tokens
	 */
	public void setTokens(int tokens) {
		this.tokens = tokens;
	}

	/**
	 * Returns the number of tokens in the place.
	 * 
	 * @return Number of tokens.
	 */
	public int getTokens() {
		return tokens;
	}

	/**
	 * Sets the number of tokens to: current tokens + x. (x can be negative)
	 * 
	 * @param x A positive or negative integer.
	 */
	public void addTokens(int x) {
		if (tokens > 0 | x > 0)
			tokens += x;
	}
}
