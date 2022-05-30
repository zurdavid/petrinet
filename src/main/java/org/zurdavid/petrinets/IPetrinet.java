package org.zurdavid.petrinets;

import java.util.Collection;

import org.zurdavid.petrinets.model.Marking;
import org.zurdavid.petrinets.model.Place;
import org.zurdavid.petrinets.model.Transition;

/**
 * The interface provides methods to build a Petri net, retrieve data from the
 * Petri net and change its marking. It is the main interface to the data model
 * and is used by the controller as well as the view. See {@link IPetrinetView}.
 * 
 * @author David Zurschmitten
 */
public interface IPetrinet {
	/**
	 * The method adds an arc to the Petri net.
	 * 
	 * @param id     The arc's id.
	 * @param source The id of the node the arc comes from.
	 * @param target The id of the node the arc leads to.
	 */
	void addArc(String id, String source, String target);

	/**
	 * The method adds a tranistion the the Petri net.
	 * 
	 * @param id The transition's id.
	 */
	void addTransition(String id);

	/**
	 * The method adds a place the the Petri net.
	 * 
	 * @param id The place's id.
	 */
	void addPlace(String id);

	/**
	 * Sets the name of the node whose id is passed in.
	 * 
	 * @param id   The node's id.
	 * @param name The name.
	 */
	void setName(String id, String name);

	/**
	 * Sets the number of initial tokens of the place whose id is passed in.
	 * 
	 * @param placeId The place's id.
	 * @param tokens  The number of tokens.
	 */
	void setInitialTokens(String placeId, int tokens);

	/**
	 * The method sets the position of the node whose id is passed in to (x,y).
	 * 
	 * @param id The nodes id.
	 * @param x  The horizontal coordinate.
	 * @param y  The vertical coordinate.
	 */
	void setPosition(String id, int x, int y);

	/**
	 * Returns true if the Petri net contains a place with the given id.
	 * 
	 * @param id The place's id.
	 * @return Returns true if the Petri net contains the place.
	 */
	boolean containsPlace(String id);

	/**
	 * Returns true if the Petri net contains a transition with the given id.
	 * 
	 * @param id The place's id.
	 * @return Returns true if the Petri net contains the transition.
	 */
	boolean containsTransition(String id);

	/**
	 * Returns the Place with the given id.
	 * 
	 * @param id The Place's id.
	 * @return The Place.
	 */
	Place getPlace(String id);

	/**
	 * Returns the Transition with the given id.
	 * 
	 * @param id The Transition's id.
	 * @return The Transition.
	 */
	Transition getTransition(String id);

	/**
	 * The method returns an iterable collection with all Places in the Petri net.
	 * 
	 * @return A collection of all Places in the Petri net.
	 */
	Collection<Place> getPlaces();

	/**
	 * The method returns an iterable collection with all Transitions in the Petri
	 * net.
	 * 
	 * @return A collection of all Transitions in the Petri net.
	 */
	Collection<Transition> getTransitions();

	/**
	 * The method returns the Petri net's current marking.
	 * 
	 * @return The Petri net's current marking.
	 */
	Marking getMarking();

	/**
	 * The Petri net's marking is set to the passed in marking.
	 * 
	 * @param marking The marking.
	 */
	void setMarking(Marking marking);

	/**
	 * Resets the Petri net's marking to its initial marking.
	 */
	void resetInitialMarking();

	/**
	 * The method returns the Petri net's initial marking.
	 * 
	 * @return The Petri net's initial marking.
	 */
	Marking getInitialMarking();

	/**
	 * The current marking is set as the Petri nets initial marking.
	 */
	void setCurrentAsInitialMarking();

	/**
	 * The method adds <i>x</i> tokens to the current amount of tokens in a place.
	 * <i>x</i> can also be negative.
	 * 
	 * @param placeId The place's id.
	 * @param x       Number of tokens to be added (subracted if x is negative).
	 */
	void addTokens(String placeId, int x);
}
