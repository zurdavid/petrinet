package org.zurdavid.petrinets.controller;

import java.io.File;

import org.zurdavid.petrinets.IPetrinet;
import org.zurdavid.petrinets.model.Petrinet;

/**
 * The class extends the abstract class {@link PNMLWopedParser} and implements its abstract
 * methods in order to build a Petri net using the application's data model
 * classes in the package <tt>org.zurdavid.petrinets.model</tt>.
 *
 * @author David Zurschmitten
 */
public class PNMLParser extends AbstractPNMLParser {
	private IPetrinet petrinet;

	/**
	 * Constructor.
	 * 
	 * @param pnml The pnml file to be parsed.
	 */
	public PNMLParser(File pnml) {
		super(pnml);
	}

	/**
	 * The method parses the pnml file passed in at construction and builds the
	 * Petri net using the application's data model classes.
	 * 
	 * @return The Petrinet built from the pnml file.
	 */
	public IPetrinet loadPetrinet() {
		petrinet = new Petrinet();

		initParser();
		parse();
		petrinet.setCurrentAsInitialMarking();

		return petrinet;
	}

	@Override
	protected void newTransition(String id) {
		petrinet.addTransition(id);
	}

	@Override
	public void newPlace(String id) {
		petrinet.addPlace(id);
	}

	@Override
	public void newArc(String id, String source, String target) {
		petrinet.addArc(id, source, target);
	}

	@Override
	public void setName(String id, String name) {
		petrinet.setName(id, name);
	}

	@Override
	public void setPosition(String id, String x, String y) {
		int px = Integer.parseInt(x);
		int py = Integer.parseInt(y);
		petrinet.setPosition(id, px, py);
	}

	@Override
	public void setTokens(String id, String tokens) {
		int t = Integer.parseInt(tokens);
		petrinet.setInitialTokens(id, t);
	}
}
