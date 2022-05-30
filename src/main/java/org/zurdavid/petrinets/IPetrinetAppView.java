package org.zurdavid.petrinets;

/**
 * The interface serves as the main interface of the application's view. It
 * provides methods to load new Petri net graphs and reachability graphs, as
 * well as methods that provide access to the other three interfaces of the
 * view:
 * <ul>
 * <li>{@link IPetrinetView}
 * <li>{@link IReachabilityGraph}
 * <li>{@link ITextView}
 * </ul>
 * 
 * @author David Zurschmitten
 */
public interface IPetrinetAppView {
	/**
	 * The method loads the given Petri net into the Petri net view and displays it.
	 * 
	 * @param petrinet The Petri net to be displayed.
	 */
	void loadPetrinet(IPetrinet petrinet);

	/**
	 * The method loads the given reachability graph into reachability graph view
	 * and displays it.
	 * 
	 * @param graph The reachability graph to be displayed.
	 */
	void loadReachabilityGraph(IReachabilityGraph graph);

	/**
	 * The method provides access to the Petri net view interface.
	 * 
	 * @return The Petri net view interface.
	 */
	IPetrinetView getPetrinetGraph();

	/**
	 * The method provides access to the reachability graph view interface.
	 * 
	 * @return The reachability graph view interface.
	 */
	IReachabilityGraphView getpRGView();

	/**
	 * The metheod provides access to interface to display text messages.
	 * 
	 * @return The text view interface.
	 */
	ITextView getTextView();
}