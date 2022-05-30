package org.zurdavid.petrinets;

import org.zurdavid.petrinets.controller.GraphController;
import org.zurdavid.petrinets.view.PetrinetFrame;

/**
 * The class contains the static main method, which launches the Petrinets application.
 * 
 * @author David Zurschmitten
 *
 */
public class PetrinetApp {
	/**
	 * The applications main-method.
	 * 
	 * @param args
	 * 			The args parameter is not used.
	 */
	public static void main(String[] args) {
		IPetrinetController controller = new GraphController();
		
		PetrinetFrame view = new PetrinetFrame(controller);
		controller.setView(view);
		
	}
}
