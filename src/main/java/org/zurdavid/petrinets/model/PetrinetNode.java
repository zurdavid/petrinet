package org.zurdavid.petrinets.model;

import org.zurdavid.petrinets.controller.PNMLParser;

/**
 * The class is an abstract data class that implements the common properties of
 * the two types of nodes in a Petri net: {@link Place}, {@link Transition}.
 * Their implementations are derived from this abstract class.
 * 
 * The properties are:
 * <ul>
 * <li>id: the nodes unique id.
 * <li>name: the nodes name.
 * <li>x: horizontal coordinate
 * <li>y: vertical coordinate
 * </ul>
 * 
 * @author David Zurschmitten
 */
public abstract class PetrinetNode {
	private final String id;
	private String name;
	private int x;
	private int y;

	/**
	 * Nodes are only given an id when constructed. All other attributes are set
	 * later. This is due to how the pnml Parsers works. See {@link PNMLParser}
	 * 
	 * @param id The nodes id.
	 */
	PetrinetNode(String id) {
		this.id = id;
	}

	/**
	 * Two objects of type PetrinetNode are equal, if their id is equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PetrinetNode))
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		PetrinetNode node = (PetrinetNode) obj;
		return id.equals(node.getId());
	}

	/**
	 * Returns the node's id.
	 * 
	 * @return The node's id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the node's name.
	 * 
	 * @return The node's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The horizontal coordinate of the node.
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return The vertical coordinate of the node.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the node's name.
	 * 
	 * @param name The node's name.
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the horizontal coordinate of the node.
	 * 
	 * @param x The horizontal coordinate of the node.
	 */
	void setX(int x) {
		this.x = x;
	}

	/**
	 * Sets the vertical coordinate of the node.
	 * 
	 * @param y The vertical coordinate of the node.
	 */
	void setY(int y) {
		this.y = y;
	}

	/**
	 * Returns a String represantation of a PetrinetNode following the pattern "id -
	 * name".
	 */
	@Override
	public String toString() {
		return id + " - " + name;
	}

}
