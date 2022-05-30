package org.zurdavid.petrinets.view;

import java.awt.Container;
import java.awt.Font;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.zurdavid.petrinets.IPetrinetAppView;
import org.zurdavid.petrinets.ITextView;
import org.zurdavid.petrinets.controller.BoundednessAnalysisResult;

/**
 * The class implements the interface {@link org.zurdavid.petrinets.ITextView ITextView}.
 * 
 * @author David Zurschmitten
 */
public class TextView extends JTextArea implements ITextView {
	private ResourceBundle re;

	public TextView(IPetrinetAppView parentView) {
		setEditable(false);
		/*
		Font font = this.getFont();
		float size = font.getSize() + 4.0f;
		this.setFont(font.deriveFont(size));
		*/
		this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));

		Locale currentLocale = Locale.getDefault();
		re = ResourceBundle.getBundle("Strings", currentLocale);
	}

	@Override
	public void printSingleResult(BoundednessAnalysisResult result, boolean showDialog) {

		if (showDialog) {
			// display pop-up message
			Container frame = this.getParent().getParent().getParent();
			String shortMsg = result.isBounded() ? re.getString("isBounded") : re.getString("notBounded");
			JOptionPane.showMessageDialog(frame, shortMsg);
		}

		// build message String
		String longMsg;
		if (result.isBounded()) {
			longMsg = String.format("%s %s: %d | %s: %d", re.getString("isBounded"), re.getString("nodes"),
					result.getNodes(), re.getString("edges"), result.getEdges());
		} else {
			longMsg = String.format("%s \n  m:%s , m':%s \n  %s: %d, %s: (%s)", re.getString("notBounded"),
					result.getM1(), result.getM2(), re.getString("path_length"), result.getPathLength(),
					re.getString("path"), result.getPathAsString());
		}
		String txt = String.format("\n%s", longMsg);
		this.append(txt);
	}

	@Override
	public void printMultiResult(BoundednessAnalysisResult result) {
		String msgIsBounded, msgResult;
		if (result.isBounded()) {
			msgIsBounded = re.getString("yes");
			msgResult = result.getNodes() + " / " + result.getEdges();
		} else {
			msgIsBounded = re.getString("no");
			msgResult = String.format("%s, %s; %d:(%s)", result.getM1().toString(), result.getM2().toString(),
					result.getPathLength(), result.getPathAsString());
		}
		String msg = String.format("%-70s | %-10s | %s\n", result.getFilename(), msgIsBounded, msgResult);
		this.append(msg);
	}

	@Override
	public void printHeader() {
		String header = String.format("\n\n%-70s | %-10s | %s \n", re.getString("filename"), re.getString("bounded"),
				re.getString("resultDetails"));
		String line = String.format("%70s + %10s +  %45s\n", " ", " ", " ").replace(" ", "-");
		this.append(header);
		this.append(line);
	}

	@Override
	public void printPropertyHeader() {
		String header = String.format("\n\n%-70s | %-10s | %-14s | %-12s \n", re.getString("filename"),
				re.getString("bounded"), re.getString("deadlock-free"), re.getString("terminates"));
		String line = String.format("%70s + %10s +  %14s +  %12s\n", " ", " ", " ", " ").replace(" ", "-");
		this.append(header);
		this.append(line);
	}

	@Override
	public void printPropertyResult(BoundednessAnalysisResult result, boolean deadlockFree) {
		String msgIsBounded, msgDeadlockFree, msgTerminates;
		if (result.isBounded()) {
			msgIsBounded = re.getString("yes");
			msgDeadlockFree = deadlockFree ? re.getString("yes") : re.getString("no");
			msgTerminates = !result.hasCycle() ? re.getString("yes") : re.getString("no");
		} else {
			msgIsBounded = re.getString("no");
			msgDeadlockFree = "-";
			msgTerminates = "-";
		}
		String msg = String.format("%-70s | %-10s | %-14s | %-12s\n", result.getFilename(), msgIsBounded,
				msgDeadlockFree, msgTerminates);
		this.append(msg);
	}

	@Override
	public void clear() {
		this.setText("");
	}

	@Override
	public void printAnalysisStarted(String filename) {
		String text = String.format("\n%s %s...", re.getString("analysisof"), filename);
		append(text);
	}

	@Override
	public void append(String text) {
		super.append(text);
		this.setCaretPosition(this.getDocument().getLength());
	}
}
