package org.zurdavid.petrinets.model.reachabilitygraph;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.zurdavid.petrinets.IReachabilityGraph;
import org.zurdavid.petrinets.model.Marking;
import org.zurdavid.petrinets.model.Transition;

/**
 * The class is an implementation of the interface {@link IReachabilityGraph}.
 * It is the data model representation of a reachability graph. A reachability
 * graph is implemented as a Map of markings and directed edges (see
 * {@link RGEdge} leaving the marking.
 * 
 * @author David Zurschmitten
 */
public class ReachabilityGraph implements IReachabilityGraph {
	private Marking initialMarking;
	private HashMap<Marking, LinkedList<RGEdge>> markings = new HashMap<>();

	/**
	 * Constructor for a reachability graph containing only the initial marking.
	 * 
	 * @param initialMarking The Petri net's initial marking.
	 */
	public ReachabilityGraph(Marking initialMarking) {
		this.initialMarking = initialMarking;
		markings.put(initialMarking, new LinkedList<RGEdge>());
	}

	@Override
	public RGUpdateResult addMarking(Transition t, Marking a, Marking b) {
		RGUpdateResult updateResult = RGUpdateResult.NONE;
		LinkedList<RGEdge> edges = markings.get(a);

		// the marking is not part of the graph and thus added
		if (!markings.containsKey(b)) {
			markings.put(b, new LinkedList<RGEdge>());
			edges.add(new RGEdge(b, t));
			updateResult = RGUpdateResult.ADDED_NODE;
			// the marking is part of the graph, only the edge needs to be added
		} else if (!containsEdge(t, a, b)) {
			edges.add(new RGEdge(b, t));
			updateResult = RGUpdateResult.ADDED_EDGE;
		}
		return updateResult;
	}

	@Override
	public Marking getInitialMarking() {
		return initialMarking;
	}

	@Override
	public Collection<Marking> getMarkings() {
		return markings.keySet();
	}

	@Override
	public List<RGEdge> getEdges(Marking m) {
		return markings.get(m);
	}

	private boolean containsEdge(Transition t, Marking a, Marking b) {
		LinkedList<RGEdge> edges = markings.get(a);
		boolean contains = false;
		for (RGEdge edge : edges) {
			if (edge.getMarking().equals(b) && edge.getTransition().equals(t)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

}
