package org.zurdavid.petrinets;

import org.zurdavid.petrinets.controller.BoundednessAnalysisResult;

/**
 * The Interface provides methods to display text messages. The interface must
 * be implemented by an element of the view. It is the return type of the method
 * getTextView() in the IPetrinetAppView interface, which is the applications
 * main view-interface. The interface is used by the controller.
 * 
 * @author David Zurschmitten
 *
 */
public interface ITextView {
	/**
	 * Prints the result of a boundedness analysis, formated as a single result.
	 * Optionally a pop-up message dialog is shown.
	 * 
	 * @param result     Boundedness analysis result to be printed.
	 * @param showDialog Pop-up message is shown, if parameter is true.
	 */
	void printSingleResult(BoundednessAnalysisResult result, boolean showDialog);

	/**
	 * Prints the result of a boundedness analysis, formated as a row in a table, to
	 * display the analysis results of multiple files.
	 * 
	 * @param result Boundedness analysis result to be printed.
	 */
	void printMultiResult(BoundednessAnalysisResult result);

	/**
	 * The method prints the header of the table to display the results of a
	 * boundedness analysis on multiple files.
	 */
	void printHeader();

	/**
	 * The method prints a short message, informing that the analysis of a given
	 * file begins now.
	 * 
	 * @param filename Name of the file.
	 */
	void printAnalysisStarted(String filename);

	/**
	 * Method clears the TextView.
	 * 
	 * delete
	 */
	void clear();

	void append(String txt);

	void printPropertyHeader();

	void printPropertyResult(BoundednessAnalysisResult result, boolean deadlockFree);
}
