package org.zurdavid.petrinets.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

import org.zurdavid.petrinets.IPetrinet;

/**
 * The class implements the {@link IPetrinet} Interface and serves as the main
 * class to model a Petri net in the application.
 * 
 * @author David Zurschmitten
 */
public class Petrinet implements IPetrinet {
	// a TreeMap is used, beacause the places need to be sorted alphabetically when
	// creating a marking (in a TreeMap this is already the case)
	private TreeMap<String, Place> places = new TreeMap<>();
	private HashMap<String, Transition> transitions = new HashMap<>();
	private Marking initialMarking;

	@Override
	public void addPlace(String id) {
		places.put(id, new Place(id));
	}

	@Override
	public void addTransition(String id) {
		transitions.put(id, new Transition(id));
	}

	@Override
	public void addArc(String id, String source, String target) {
		if (transitions.containsKey(source)) {
			Arc arc = new Arc(id, places.get(target));
			transitions.get(source).addPostconditionArc(arc);
		} else if (transitions.containsKey(target)) {
			Arc arc = new Arc(id, places.get(source));
			transitions.get(target).addPreconditionArc(arc);
		}
	}

	@Override
	public void setName(String id, String name) {
		if (transitions.containsKey(id)) {
			transitions.get(id).setName(name);
		} else if (places.containsKey(id)) {
			places.get(id).setName(name);
		}
	}

	@Override
	public void setInitialTokens(String id, int tokens) {
		places.get(id).setTokens(tokens);
	}

	@Override
	public void setPosition(String id, int x, int y) {
		if (transitions.containsKey(id)) {
			Transition t = transitions.get(id);
			t.setX(x);
			t.setY(y);
		} else if (places.containsKey(id)) {
			Place p = places.get(id);
			p.setX(x);
			p.setY(y);
		}
	}

	@Override
	public boolean containsPlace(String id) {
		return places.containsKey(id);
	}

	@Override
	public Place getPlace(String id) {
		return places.get(id);
	}

	@Override
	public Collection<Place> getPlaces() {
		return places.values();
	}

	@Override
	public boolean containsTransition(String id) {
		return transitions.containsKey(id);
	}

	@Override
	public Transition getTransition(String id) {
		return transitions.get(id);
	}

	@Override
	public Collection<Transition> getTransitions() {
		return transitions.values();
	}

	@Override
	public void addTokens(String placeId, int x) {
		Place place = getPlace(placeId);
		place.addTokens(x);
	}

	@Override
	public void setMarking(Marking marking) {
		int i = 0;
		for (Place place : places.values()) {
			int tokens = marking.getTokensAt(i);
			place.setTokens(tokens);
			i++;
		}
	}

	@Override
	public Marking getMarking() {
		int length = places.size();
		int[] marking = new int[length];
		int i = 0;
		for (Place place : places.values()) {
			marking[i] = place.getTokens();
			i++;
		}
		return new Marking(marking);
	}

	@Override
	public Marking getInitialMarking() {
		return initialMarking;
	}

	@Override
	public void resetInitialMarking() {
		setMarking(initialMarking);
	}

	@Override
	public void setCurrentAsInitialMarking() {
		initialMarking = getMarking();
	}

}
