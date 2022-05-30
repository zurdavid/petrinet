package org.zurdavid.petrinets.view;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JToolBar;

import org.zurdavid.petrinets.IPetrinetController;
import org.zurdavid.petrinets.ITextView;

/**
 * The class implements a toolbar for a PetrinetFrame. The toolbar provides the
 * following buttons:
 * <ul>
 * <li>plus/minus: to add and subtract tokens on a place and change the initial
 * marking.
 * <li>reset: resets the marking to the initial marking
 * <li>analyseBoundedness
 * <li>deleteReachybilityGraph: delete current reachability graph
 * <li>resetTxtOutput: deletes the content of the applications text output
 * element
 * </ul>
 * 
 * @author David Zurschmitten
 *
 */
public class Toolbar extends JToolBar {
	private JButton plus, minus;
	private JButton resetMarking, analyseBoundedness, deleteReachybilityGraph;

	/**
	 * In the constructor the buttons are created and added to the tool bar.
	 * 
	 * @param controller The Petri net applications main controller.
	 * @param textView   The applications view to display text messages.
	 */
	public Toolbar(IPetrinetController controller, ITextView textView) {
		Locale currentLocale = Locale.getDefault();
		ResourceBundle re = ResourceBundle.getBundle("Strings", currentLocale);

		plus = new JButton(re.getString("plus"));
		plus.setToolTipText(re.getString("plusTip"));
		plus.addActionListener(e -> controller.onAddToken());
		plus.setEnabled(false);
		add(plus);

		minus = new JButton(re.getString("minus"));
		minus.setToolTipText(re.getString("minusTip"));
		minus.addActionListener(e -> controller.onSubtractToken());
		minus.setEnabled(false);
		add(minus);

		addSeparator();

		resetMarking = new JButton(re.getString("reset"));
		resetMarking.setToolTipText(re.getString("resetTip"));
		resetMarking.addActionListener(e -> controller.resetInitialMarking(false, true));
		resetMarking.setEnabled(false);
		add(resetMarking);

		addSeparator();
		analyseBoundedness = new JButton(re.getString("analyseBoundedness"));
		analyseBoundedness.setToolTipText(re.getString("analyseBoundednessTip"));
		analyseBoundedness.addActionListener(e -> controller.performBoundednessAnalysis());
		analyseBoundedness.setEnabled(false);
		add(analyseBoundedness);

		deleteReachybilityGraph = new JButton(re.getString("delRGraph"));
		deleteReachybilityGraph.setToolTipText(re.getString("delRGraphTip"));
		deleteReachybilityGraph.addActionListener(e -> controller.resetInitialMarking(true, true));
		deleteReachybilityGraph.setEnabled(false);
		add(deleteReachybilityGraph);

		addSeparator();
		JButton resetTxtOutput = new JButton(re.getString("resetTxtOutput"));
		resetTxtOutput.setToolTipText(re.getString("resetTxtOutputTip"));
		resetTxtOutput.addActionListener(e -> textView.clear());
		add(resetTxtOutput);

	}

	/**
	 * Enables / disables buttons on the toolbar, after loading a new Petri net.
	 */
	public void onPetrinetLoaded() {
		resetMarking.setEnabled(true);
		analyseBoundedness.setEnabled(true);
		deleteReachybilityGraph.setEnabled(true);
		plus.setEnabled(false);
		minus.setEnabled(false);
	}

	/**
	 * Enables +/- buttons, to add / subtract tokens, after a place is selected.
	 */
	public void onPlaceSelected() {
		plus.setEnabled(true);
		minus.setEnabled(true);
	}

	/**
	 * Disables +/- buttons (in case not place is selected).
	 */
	public void onPlaceLostFocus() {
		plus.setEnabled(false);
		minus.setEnabled(false);
	}

}
