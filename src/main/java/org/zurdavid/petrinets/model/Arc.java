package org.zurdavid.petrinets.model;


/**
 * The class is a data class, modelling an arc. An arc leads from a transition to a 
 * place or from a place to a transition. In this Petri net implementation 
 * a {@link Transition} has one list of its pre-condition and on of its 
 * post-condition arcs while the places the arc leads to / comes from is stored in an Arc. 
 * 
 * 
 * @author David Zurschmitten
 */
public class Arc {
	private final String id;
	private final Place place;
	
	/**
	 * @param id
	 * 			The arc's id. 
	 * @param place
	 * 			The place the arc leads to / comes from.
	 */
	public Arc(String id, Place place) {
		super();
		this.id = id;
		this.place = place;
	}
	
	public String getId() {
		return id;
	}
	public Place getPlace() {
		return place;
	}
	
	/**
	 *	Returns the arcs id as a String.
	 */
	@Override
	public String toString() {
		return id;
	}
}
