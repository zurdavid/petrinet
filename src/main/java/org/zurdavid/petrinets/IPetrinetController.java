package org.zurdavid.petrinets;

import java.io.File;

import org.zurdavid.petrinets.model.Marking;

/**
 * The interface provides the methods of the controller, which can be called by
 * the view.
 * 
 * @author David Zurschmitten
 */
public interface IPetrinetController {
	/**
	 * Sets the view with which the controller is connected. The method is
	 * necessary, because the view needs a reference to the controller at
	 * construction, thus the view cannot be set at construction.
	 * 
	 * @param view The applications view.
	 */
	public void setView(IPetrinetAppView view);

	/**
	 * The method loads a Petri net from a pnml file and a new reachability graph is
	 * created. The loaded Petri net and the reachabiliy graph are then displayed in
	 * the view.
	 * 
	 * @param pnml The pnml file to be loaded.
	 */
	public void loadPetrinet(File pnml);

	/**
	 * The controller looks up which place is currently selected in the view and
	 * adds a token to this place. The resulting marking is set as the current Petri
	 * net's new initial marking.
	 */
	public void onAddToken();

	/**
	 * The controller looks up which place is currently selected in the view and
	 * subtracts a token from this place. The resulting marking is set as the Petri
	 * net's new initial marking.
	 */
	public void onSubtractToken();

	/**
	 * The Petri nets marking is set to its initial marking. If the parameter
	 * resetReachabilityGraph is true, the current reachability graph is discarded
	 * and a new one is created. If the updateView is true, the changes in the model
	 * will be displayed in the view.
	 * 
	 * @param resetReachabilityGraph If the parameter is true, the current
	 *                               reachability graph is discarded and a new one
	 *                               is created
	 * @param updateView             If the parameter is true, the changes in the
	 *                               model will be displayed in the view.
	 */
	public void resetInitialMarking(boolean resetReachabilityGraph, boolean updateView);

	/**
	 * A boundedness analysis of the currently loaded Petri net is performed and the
	 * results are displayed in the view.
	 */
	public void performBoundednessAnalysis();

	/**
	 * The method performs a boundedness analysis for each file in the passed in
	 * array. The results are displayed in the text view.
	 * 
	 * @param files An array of pnml files.
	 */
	public void boundednessAnalysisOnMultipleFiles(File[] files);

	/**
	 * If the transition whose id is passed in is enabled it fires, which results in
	 * a new marking of the Petri net and possibly expansion of the reachability
	 * graph. All changes are displayed in the views.
	 * 
	 * @param id Id of the transition that should fire.
	 */
	public void fireTransition(String id);

	/**
	 * The Petri nets marking is set to the passed in marking.
	 * 
	 * @param marking A String representation of a Marking. See
	 *                {@link Marking#toString()}.
	 */
	public void gotoMarking(String marking);

	public void propertyAnalysis(File[] files);
}