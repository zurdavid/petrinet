package org.zurdavid.petrinets;

import org.zurdavid.petrinets.controller.BoundednessAnalysisResult;
import org.zurdavid.petrinets.model.Marking;
import org.zurdavid.petrinets.model.Transition;

/**
 * The interface provides methods to display a reachability graph. It is part of
 * the applications view.
 * 
 * @author David Zurschmitten
 */
public interface IReachabilityGraphView {
	/**
	 * The method adds a marking to the graph view.
	 * 
	 * @param newMarking The new marking.
	 */
	void addMarking(Marking newMarking);

	/**
	 * The method adds an edge to the graph view.
	 * 
	 * @param t    The firing transition.
	 * @param from The marking the edge comes from.
	 * @param to   The marking the edge leads to.
	 */
	void addEdge(Transition t, Marking from, Marking to);

	/**
	 * Updates the transition that last fired, so the corresponding edge and node /
	 * marking can be highlighted.
	 * 
	 * @param t          The firing transition.
	 * @param preFiring  The Petri nets marking before the transition fired.
	 * @param postFiring The Petri nets marking after the transition fired.
	 */
	void updateLastFired(Transition t, Marking preFiring, Marking postFiring);

	/**
	 * The passed in reachability graph is loaded and displayed.
	 * 
	 * @param graph The reachability graph to be displayed.
	 */
	void load(IReachabilityGraph graph);

	/**
	 * The method hilights the path that marks the reachability graph as unbounded.
	 * 
	 * @param r The negative result of the boundedness analysis that contains the
	 *          path.
	 */
	void hilightPath(BoundednessAnalysisResult r);
}
