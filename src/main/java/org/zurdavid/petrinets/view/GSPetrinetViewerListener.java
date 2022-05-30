package org.zurdavid.petrinets.view;

import org.graphstream.graph.Node;
import org.graphstream.ui.view.ViewerListener;

import org.zurdavid.petrinets.IPetrinetController;

/**
 * The class implements the GraphStream interface ViewerListener, to handle click-events 
 * on a {@link GraphStreamPetrinet} with which the listener is registered. 
 * 
 * @author David Zurschmitten
 *
 */
public class GSPetrinetViewerListener implements ViewerListener {
	private IPetrinetController controller;
	private GraphStreamPetrinet petrinetGraph;
	private Toolbar toolbar;
	
	
	/**
	 * Constructor.
	 * 
	 * @param petrinetGraph
	 *				The GraphStreamPetrinet object the listener is registered with.
	 * @param controller
	 * 				The app's controller.
	 * @param toolbar
	 * 				The tool bar on the PetrinetFrame.
	 */
	public GSPetrinetViewerListener(GraphStreamPetrinet petrinetGraph, IPetrinetController controller, Toolbar toolbar) {
		super();
		this.petrinetGraph = petrinetGraph;
		this.controller = controller;
		this.toolbar = toolbar;
	}

	/**
	 * If an enabled transition is clicked on the method 
	 * {@link IPetrinetController#fireTransition(String) fire(String id}
	 * is called. If a place is clicked on it is marked as selected
	 * in the GraphStreamPetrinet.
	 */
	@Override
	public void buttonReleased(String id) {
		Node clicked = petrinetGraph.getNode(id);
		String classes = clicked.getAttribute("ui.class");		
		// transition clicked 
		if (classes.contains("transitionEnabled")) {
			controller.fireTransition(id);
		// place clicked
		} else if (classes.contains("place")) {
			selectPlace(id);
		}
	}
	
	// add CSS class "highlight" to selected place
	private void selectPlace(String selected) {
		// remove highlight class from old selected place
		String oldSelected = petrinetGraph.getSelectedPlace();
		if (oldSelected != null) {
			Node oldFocused = petrinetGraph.getNode(oldSelected);
			String classes = oldFocused.getAttribute("ui.class");
			classes = classes.replace(", highlight", "");
			oldFocused.addAttribute("ui.class", classes);
		}
		// Highlight new selected place
		petrinetGraph.setSelectedPlace(selected);
		Node newSelected = petrinetGraph.getNode(selected);
		String classes = newSelected.getAttribute("ui.class");
		newSelected.addAttribute("ui.class", classes + ", highlight");
		// activate control buttons in tool bar that act on places
		toolbar.onPlaceSelected();
	}
	
	/**
	 *  Empty method. Not implemented.
	 */
	@Override
	public void buttonPushed(String id) {}
	
	/**
	 *	Empty method. Not implemented.
	 */
	@Override
	public void viewClosed(String viewName) {}
	
}