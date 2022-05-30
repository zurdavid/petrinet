package org.zurdavid.petrinets.controller;

import java.util.LinkedList;

import org.zurdavid.petrinets.model.Marking;
import org.zurdavid.petrinets.model.Transition;

/**
 * The class is a data class. It contains the data resulting from a boundedness
 * analysis.
 * 
 * @author David Zurschmitten
 */
public class BoundednessAnalysisResult {
	private final boolean isBounded;
	private final int nodes;
	private final int edges;
	private final boolean hasCycle;
	private final Marking m1, m2;
	private final LinkedList<Transition> transitionPath;
	private final LinkedList<Marking> markingPath;

	private final String filename;

	/**
	 * This constructor is to be used if the analysed Petri net is bounded.
	 * 
	 * @param filename Fileame of the analysed Petri net.
	 * @param nodes    Number of nodes / markings in the reachability graph.
	 * @param edges    Number of edges / transitions in the reachability graph.
	 */
	public BoundednessAnalysisResult(String filename, int nodes, int edges, boolean hasCycle) {
		this.isBounded = true;

		this.filename = filename;
		this.nodes = nodes;
		this.edges = edges;
		this.hasCycle = hasCycle;

		// fields not used
		m1 = null;
		m2 = null;
		transitionPath = null;
		markingPath = null;
	}

	/**
	 * This constructor is to be used if the analysed Petri net is unbounded.
	 * 
	 * @param filename       Fileame of the analysed Petri net.
	 * @param m1             Markings m1 and m2 mark the reachability graph as
	 *                       unbounded.
	 * @param m2             Markings m1 and m2 mark the reachability graph as
	 *                       unbounded.
	 * @param transitionPath Transitions on the path which marks the reachability
	 *                       graph as unbounded.
	 * @param markingPath    Markings on the path which marks the reachability graph
	 *                       as unbounded.
	 */
	public BoundednessAnalysisResult(String filename, Marking m1, Marking m2, LinkedList<Transition> transitionPath,
			LinkedList<MarkingWrapper> markingPath) {
		this.isBounded = false;

		this.filename = filename;
		this.m1 = m1;
		this.m2 = m2;
		this.transitionPath = transitionPath;
		this.markingPath = new LinkedList<Marking>();
		for (MarkingWrapper wrapper : markingPath) {
			this.markingPath.add(wrapper.getMarking());
		}

		// fields not used
		nodes = 0;
		edges = 0;
		this.hasCycle = false;
	}

	public boolean isBounded() {
		return isBounded;
	}

	/**
	 * @return Number of nodes / markings in a bounded reachability graph.
	 */
	public int getNodes() {
		return nodes;
	}

	/**
	 * @return Number of edges / transitions in a bounded reachability graph.
	 */
	public int getEdges() {
		return edges;
	}

	public Marking getM1() {
		return m1;
	}

	public Marking getM2() {
		return m2;
	}

	/**
	 * @return List of transitions / edges on the path that marks the reachability
	 *         graph as unbounded.
	 */
	public LinkedList<Transition> getPath() {
		return transitionPath;
	}

	/**
	 * @return List of Markings on the path that marks the reachability graph as
	 *         unbounded.
	 */
	public LinkedList<Marking> getMarkingPath() {
		return markingPath;
	}

	/**
	 * @return The length of the path (number of edges / transitions on path)
	 */
	public int getPathLength() {
		return transitionPath.size();
	}

	public String getFilename() {
		return filename;
	}

	public boolean hasCycle() {
		return hasCycle;
	}

	/**
	 * Method returns the transition ids on the path that mark the reachability
	 * graph as bounded separated by commas.
	 * 
	 * @return Concatenation of transition ids separated by comma.
	 */
	public String getPathAsString() {
		StringBuilder path = new StringBuilder();
		for (Transition transition : this.transitionPath) {
			path.append(transition.getId());
			path.append(',');
		}
		path.deleteCharAt(path.length() - 1);
		return path.toString();
	}
}