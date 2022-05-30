package org.zurdavid.petrinets.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import org.zurdavid.petrinets.IReachabilityGraph;
import org.zurdavid.petrinets.IReachabilityGraphView;
import org.zurdavid.petrinets.controller.BoundednessAnalysisResult;
import org.zurdavid.petrinets.model.Marking;
import org.zurdavid.petrinets.model.Transition;
import org.zurdavid.petrinets.model.reachabilitygraph.RGEdge;

/**
 * The class is an implementation of the interface
 * {@link IReachabilityGraphView} using the GraphStream library (it derives from
 * {@link MultiGraph}) to display a reachability graph. It is part of the
 * applications view.
 * 
 * 
 * @author David Zurschmitten
 */
public class GraphStreamReachabilityGraph extends MultiGraph implements IReachabilityGraphView {
	// path to css graph layout
	private static String CSS_FILE = "url(" + GraphStreamPetrinet.class.getResource("/reachabilityGraph.css") + ")";

	private IReachabilityGraph reachabilityGraph;
	private Node lastMarking;
	private Edge lastEdge;

	/**
	 * Constructor. Loads an empty graph.
	 */
	public GraphStreamReachabilityGraph() {
		super("Reachability Graph");
		this.addAttribute("ui.stylesheet", CSS_FILE);

		addAttribute("ui.antialias");
		addAttribute("ui.quality");
	}

	@Override
	public void addMarking(Marking newMarking) {
		Node node = this.addNode(newMarking.toString());
		node.addAttribute("ui.label", newMarking.toString());
	}

	@Override
	public void addEdge(Transition t, Marking from, Marking to) {
		String id = from.toString() + t.getId() + to.toString();
		String label = String.format("[%s] %s", t.getId(), t.getName());
		Edge edge = this.addEdge(id, from.toString(), to.toString(), true);
		edge.addAttribute("ui.label", label);
	}

	@Override
	public void load(IReachabilityGraph graph) {
		reachabilityGraph = graph;
		clear();

		lastMarking = null;
		lastEdge = null;

		this.addAttribute("ui.stylesheet", CSS_FILE);
		this.addAttribute("ui.antialias");
		this.addAttribute("ui.quality");

		Collection<Marking> markings = reachabilityGraph.getMarkings();
		for (Marking marking : markings) {
			addMarking(marking);
		}
		for (Marking marking : markings) {
			List<RGEdge> edges = reachabilityGraph.getEdges(marking);
			for (RGEdge edge : edges) {
				addEdge(edge.getTransition(), marking, edge.getMarking());
			}
		}

		Marking initialMarking = reachabilityGraph.getInitialMarking();
		Node node = this.getNode(initialMarking.toString());
		node.addAttribute("ui.class", "startnode");
	}

	@Override
	public void updateLastFired(Transition t, Marking before, Marking after) {
		if (lastMarking != null) {
			removeClass(lastMarking, "fired");
		}
		if (lastEdge != null)
			removeClass(lastEdge, "fired");
		lastMarking = this.getNode(after.toString());
		addClass(lastMarking, "fired");
		lastEdge = this.getEdge(before.toString() + t.getId() + after.toString());
		addClass(lastEdge, "fired");
	}

	private void addClass(Element e, String css_class) {
		String classes = e.getAttribute("ui.class");
		classes = css_class + ", " + classes;
		e.addAttribute("ui.class", classes);
	}

	private void removeClass(Element e, String css_class) {
		String classes = e.getAttribute("ui.class");
		classes = classes.replace(css_class, "");
		e.addAttribute("ui.class", classes);
	}

	@Override
	public void hilightPath(BoundednessAnalysisResult r) {
		Node m1 = getNode(r.getM1().toString());
		Node m2 = getNode(r.getM2().toString());
		addClass(m1, "bounded");
		addClass(m2, "bounded");

		Iterator<Marking> markings = r.getMarkingPath().iterator();
		Marking from = markings.next();
		for (Transition trans : r.getPath()) {
			Marking to = markings.next();
			String id = from.toString() + trans.getId() + to.toString();
			Edge edge = getEdge(id);
			addClass(edge, "path");
			from = to;
		}
	}

}
