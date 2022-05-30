package org.zurdavid.petrinets.view;

import java.util.LinkedList;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import org.zurdavid.petrinets.IPetrinet;
import org.zurdavid.petrinets.IPetrinetView;
import org.zurdavid.petrinets.model.Arc;
import org.zurdavid.petrinets.model.Place;
import org.zurdavid.petrinets.model.Transition;

/**
 * The class is an implementation of the interface {@link IPetrinetView} using
 * the GraphStream library (it derives from {@link MultiGraph}) to display a
 * Petri net graph. It is part of the applications view.
 * 
 * 
 * @author David Zurschmitten
 */
public class GraphStreamPetrinet extends MultiGraph implements IPetrinetView {
	// path to css graph layout
	private static final String CSS_FILE = "url(" + GraphStreamPetrinet.class.getResource("/petrinetGraph.css") + ")";

	private IPetrinet petrinet;

	// currently selected Position, used to unselect it, after selection changed and
	// to add / remove tokens
	private String selectedPosition = null;
	// currently (under given marking) enabled transitions
	private LinkedList<String> enabledTransitions = null;

	/**
	 * Constructor. Loads an empty graph.
	 */
	public GraphStreamPetrinet() {
		super("Petrinet");
		this.addAttribute("ui.stylesheet", CSS_FILE);

		addAttribute("ui.antialias");
		addAttribute("ui.quality");
	}

	/**
	 * Constructor. Loads the passed in Petri net.
	 * 
	 * @param pn The Petri net to be loaded.
	 */
	public GraphStreamPetrinet(IPetrinet pn) {
		this();
		loadPetrinet(pn);
	}

	@Override
	public void loadPetrinet(IPetrinet pn) {
		petrinet = pn;
		selectedPosition = null;
		enabledTransitions = null;
		clear();
		this.addAttribute("ui.stylesheet", CSS_FILE);

		addAttribute("ui.antialias");
		addAttribute("ui.quality");

		// adding the places
		for (Place place : pn.getPlaces()) {
			Node place_node = this.addNode(place.getId());
			String label = String.format("[%s] %s <%d>", place.getId(), place.getName(), place.getTokens());
			place_node.addAttribute("ui.label", label);
			String classes = String.format("place, m%d", Math.min(10, place.getTokens()));
			place_node.addAttribute("ui.class", classes);
			place_node.addAttribute("xy", place.getX(), -place.getY());
		}

		// adding the transitions
		for (Transition trans : pn.getTransitions()) {
			Node trans_node = this.addNode(trans.getId());
			String label = String.format("[%s] %s", trans.getId(), trans.getName());
			trans_node.addAttribute("ui.label", label);
			trans_node.addAttribute("ui.class", "transition");
			trans_node.addAttribute("xy", trans.getX(), -trans.getY());

			// adding the edges
			for (Arc arc : trans.getPostconditionArcs()) {
				Edge edge = this.addEdge(arc.getId(), trans.getId(), arc.getPlace().getId(), true);
				label = String.format("[%s]", arc.getId());
				edge.addAttribute("ui.label", label);
			}
			for (Arc arc : trans.getPreconditionArcs()) {
				Edge edge = this.addEdge(arc.getId(), arc.getPlace().getId(), trans.getId(), true);
				label = String.format("[%s]", arc.getId());
				edge.addAttribute("ui.label", label);
			}
		}
	}

	@Override
	public void updateTokens(String id) {
		Place place = petrinet.getPlace(id);
		Node node = getNode(id);
		String classes = node.getAttribute("ui.class");
		classes = classes.replaceAll("m\\d+", "m" + Math.min(10, place.getTokens()));
		node.changeAttribute("ui.class", classes);
		String label = String.format("[%s] %s <%d>", place.getId(), place.getName(), place.getTokens());
		node.addAttribute("ui.label", label);
	}

	@Override
	public String getSelectedPlace() {
		return selectedPosition;
	}

	@Override
	public void setSelectedPlace(String activeNode) {
		this.selectedPosition = activeNode;
	}

	@Override
	public void setEnabled(LinkedList<String> enabled) {
		if (enabledTransitions != null) {
			// enabled transitions are set to not enabled
			for (String id : enabledTransitions) {
				Node transition = getNode(id);
				String classes = transition.getAttribute("ui.class");
				classes = classes.replace("transitionEnabled", "transition");
				transition.addAttribute("ui.class", classes);
			}
		}
		enabledTransitions = enabled;
		for (String id : enabled) {
			Node transition = getNode(id);
			String classes = transition.getAttribute("ui.class");
			classes = classes.replace("transition", "transitionEnabled");
			transition.addAttribute("ui.class", classes);
		}
	}
}
