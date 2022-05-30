package org.zurdavid.petrinets.model.reachabilitygraph;

import org.zurdavid.petrinets.model.Marking;
import org.zurdavid.petrinets.model.Transition;

/**
 * Reachability graph edge. The class is a data class that implements an edge in
 * the reachybility graph. It contains the transition that fired and the
 * resulting marking.
 * 
 * See {@link ReachabilityGraph}.
 * 
 * @author David Zurschmitten
 */
public class RGEdge {
	private final Marking marking;
	private final Transition transition;

	public RGEdge(Marking marking, Transition transition) {
		this.marking = marking;
		this.transition = transition;
	}

	/**
	 * Returns the marking the edge leads to.
	 * 
	 * @return The marking the edge leads to.
	 */
	public Marking getMarking() {
		return marking;
	}

	/**
	 * Returns the firing transition.
	 * 
	 * @return The firing transition.
	 */
	public Transition getTransition() {
		return transition;
	}
}
