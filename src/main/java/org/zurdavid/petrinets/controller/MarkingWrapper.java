package org.zurdavid.petrinets.controller;

import java.util.Iterator;

import org.zurdavid.petrinets.model.Marking;
import org.zurdavid.petrinets.model.Petrinet;
import org.zurdavid.petrinets.model.Transition;

/**
 * The class has two fields: a Marking and an Iterator over the Transitions in a
 * {@link Petrinet}. It is used by the controller in the boundedness analysis
 * algorithm and is package private.
 * 
 * @author David Zurschmitten
 */
class MarkingWrapper {
	private final Marking m;
	private final Iterator<Transition> it;

	/**
	 * Constructor.
	 * 
	 * @param m  A Marking.
	 * @param it An Iterator over all Transitions in a {@link Petrinet}.
	 */
	public MarkingWrapper(Marking m, Iterator<Transition> it) {
		this.m = m;
		this.it = it;
	}

	/**
	 * The method returns the Marking.
	 * 
	 * @return The marking.
	 */
	public Marking getMarking() {
		return m;
	}

	/**
	 * The method returns an Iterator over all Transitions in a {@link Petrinet}.
	 * 
	 * @return Iterator over all Transitions in a {@link Petrinet}.
	 */
	public Iterator<Transition> getIterator() {
		return it;
	}

	/**
	 * Returns the String representation of the Marking in the wrapper.
	 * (Marking.toString()).
	 */
	@Override
	public String toString() {
		return m.toString();
	}

}
