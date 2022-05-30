package org.zurdavid.petrinets;

import java.util.LinkedList;

import org.zurdavid.petrinets.model.Petrinet;

/**
 * The interface provides methods to display a {@link Petrinet}. It is part of
 * the applications view.
 * 
 * @author David Zurschmitten
 */
public interface IPetrinetView {

	/**
	 * The method loads the passed in Petrinet in the view and displays it.
	 * 
	 * @param petrinet Petrinet which is to be displayed.
	 */
	void loadPetrinet(IPetrinet petrinet);

	/**
	 * The number of tokens on the place whose id is passed in are synchronized with
	 * the data model.
	 * 
	 * @param id The id of the place, whose number of tokens need to be updated.
	 */
	void updateTokens(String id);

	/**
	 * Returns the id of the place that is currently selected.
	 * 
	 * @return id of currently selected place
	 */
	String getSelectedPlace();

	/**
	 * Sets the place, the id of which is passed in, as selected.
	 * 
	 * @param placeId id of selected place,
	 */
	void setSelectedPlace(String placeId);

	/**
	 * Highlights the transitions which are enabled under the current marking.
	 * 
	 * @param enabled A list of transition ids which are enabled under the current
	 *                marking.
	 */
	void setEnabled(LinkedList<String> enabled);

}