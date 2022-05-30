package org.zurdavid.petrinets.model;

import java.util.ArrayList;

import org.zurdavid.petrinets.controller.PNMLParser;

/**
 * The class is a data class, derived from {@link PetrinetNode} and implements
 * properties (as well as getters and setters) which only appear in transition.
 * 
 * A transition contains two lists:
 * <ul>
 * <li>preconditionArcs: arcs leading from pre-condition places to the
 * transition.
 * <li>postconditionArcs: arcs leading from the transition to post-condition
 * places.
 * </ul>
 * 
 * @author David Zurschmitten
 */
public class Transition extends PetrinetNode {
	private ArrayList<Arc> preconditionArcs = new ArrayList<Arc>();
	private ArrayList<Arc> postconditionArcs = new ArrayList<Arc>();

	/**
	 * Nodes are only given an id when constructed. All other attributes are set
	 * later. This is due to how the pnml Parsers works. See {@link PNMLParser}
	 * 
	 * @param id The nodes id.
	 */
	public Transition(String id) {
		super(id);
	}

	/**
	 * Adds a post-condition arc.
	 * 
	 * @param arc The post-condition arc.
	 */
	public void addPreconditionArc(Arc arc) {
		preconditionArcs.add(arc);
	}

	/**
	 * Add a pre-condition arc.
	 * 
	 * @param arc The pre-condition arc.
	 */
	public void addPostconditionArc(Arc arc) {
		postconditionArcs.add(arc);
	}

	/**
	 * Returns pre-condition arcs as an {@link Iterable}.
	 * 
	 * @return The pre-condition arcs as an {@link Iterable}.
	 */
	public Iterable<Arc> getPreconditionArcs() {
		return preconditionArcs;
	}

	/**
	 * Returns post-condition arcs as an {@link Iterable}.
	 * 
	 * @return The post-condition arcs as an {@link Iterable}.
	 */
	public Iterable<Arc> getPostconditionArcs() {
		return postconditionArcs;
	}
}