package org.zurdavid.petrinets;

import java.util.Collection;
import java.util.List;

import org.zurdavid.petrinets.model.Marking;
import org.zurdavid.petrinets.model.Transition;
import org.zurdavid.petrinets.model.reachabilitygraph.RGEdge;
import org.zurdavid.petrinets.model.reachabilitygraph.RGUpdateResult;
import org.zurdavid.petrinets.view.GSReachabilityViewerListener;

/**
 * The interface provides methods to build a reachability graph, get a
 * collection of markings in the graph and a collection of outgoing edges for a
 * given marking in the graph. It is used by the applications controller to
 * build reachability graphs and perform boundedness analyses and the view to
 * display the data (see {@link GSReachabilityViewerListener}.
 * 
 * @author David Zurschmitten
 */
public interface IReachabilityGraph {
	/**
	 * The method adds an edge / transition and the resulting marking to the graph
	 * if they are not part of the graph already add returns information on what was
	 * added.
	 * 
	 * @param t          The transition that fired.
	 * @param preFiring  Marking before transition fired.
	 * @param postFiring Marking after transition fired.
	 * 
	 * @return Wheter or not an edge and / or marking was added to the graph.
	 */
	RGUpdateResult addMarking(Transition t, Marking preFiring, Marking postFiring);

	/**
	 * The method returns the current Petri net's initial marking.
	 * 
	 * @return The initial Marking.
	 */
	Marking getInitialMarking();

	/**
	 * A collection containing all Markings in the graph.
	 * 
	 * @return A collection containing all Markings in the graph.
	 */
	Collection<Marking> getMarkings();

	/**
	 * The method returns the outgoing edges of a passed in Marking.
	 * 
	 * @param m The marking.
	 * @return A list of outgoing edges.
	 */
	List<RGEdge> getEdges(Marking m);
}
