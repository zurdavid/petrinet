package org.zurdavid.petrinets.view;

import org.graphstream.ui.view.ViewerListener;

import org.zurdavid.petrinets.IPetrinetController;

/**
 * The class implements the GraphStream interface ViewerListener, to handle click-events 
 * on a {@link GraphStreamReachabilityGraph} with which the listener is registered. 
 * 
 * @author David Zurschmitten
 *
 */
public class GSReachabilityViewerListener implements ViewerListener {
	IPetrinetController controller;
	
	/**
	 * Constructor.
	 * 
	 * @param controller
	 * 				The applications controller.
	 */
	public GSReachabilityViewerListener(IPetrinetController controller) {
		super();
		this.controller = controller;
	}
	
	/**
	 * Calls the method {@link IPetrinetController#gotoMarking(String) gotoMarking(String id)} 
	 * passing the id of the node clicked.
	 */
	@Override
	public void buttonReleased(String id) {
		controller.gotoMarking(id);
	}

	/**
	 * Empty method. Not implemented.
	 */
	@Override
	public void viewClosed(String viewName) {}
		
	/**
	 * Empty method. Not implemented.
	 */
	@Override
	public void buttonPushed(String id) {}

	
}
