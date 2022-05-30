package org.zurdavid.petrinets.controller;

import java.io.File;
import java.util.*;

import org.zurdavid.petrinets.IPetrinet;
import org.zurdavid.petrinets.IPetrinetAppView;
import org.zurdavid.petrinets.IPetrinetController;
import org.zurdavid.petrinets.IPetrinetView;
import org.zurdavid.petrinets.IReachabilityGraph;
import org.zurdavid.petrinets.IReachabilityGraphView;
import org.zurdavid.petrinets.ITextView;
import org.zurdavid.petrinets.model.Arc;
import org.zurdavid.petrinets.model.Marking;
import org.zurdavid.petrinets.model.Place;
import org.zurdavid.petrinets.model.Transition;
import org.zurdavid.petrinets.model.reachabilitygraph.RGEdge;
import org.zurdavid.petrinets.model.reachabilitygraph.RGUpdateResult;
import org.zurdavid.petrinets.model.reachabilitygraph.ReachabilityGraph;

/**
 * The class implements the applications main controller. It contains most of
 * the application logic, in particular the boundedness analysis algorithm.
 * 
 * @author David Zurschmitten
 */
public class GraphController implements IPetrinetController {
	private IPetrinet petrinet;
	private IReachabilityGraph reachabilityGraph;
	private IPetrinetAppView view;
	private String filename;

	@Override
	public void loadPetrinet(File pnml) {
		filename = pnml.getName();
		PNMLParser parser = new PNMLParser(pnml);
		petrinet = parser.loadPetrinet();
		reachabilityGraph = new ReachabilityGraph(petrinet.getInitialMarking());

		view.loadPetrinet(petrinet);
		view.loadReachabilityGraph(reachabilityGraph);

		view.getPetrinetGraph().setEnabled(getEnabledTransitions());
	}

	// loads a Petri net from a file without displaying it in the view
	private void loadPetrinetinBackground(File pnml) {
		PNMLParser parser = new PNMLParser(pnml);
		petrinet = parser.loadPetrinet();
		reachabilityGraph = new ReachabilityGraph(petrinet.getInitialMarking());
	}

	@Override
	public void setView(IPetrinetAppView view) {
		this.view = view;
	}

	@Override
	public void onAddToken() {
		changeInitialTokens(+1);
	}

	@Override
	public void onSubtractToken() {
		changeInitialTokens(-1);
	}

	// see onAddToken() and onSubtractToken() and corresponding documentation
	private void changeInitialTokens(int i) {
		String selectedPlace = view.getPetrinetGraph().getSelectedPlace();
		if (selectedPlace != null && petrinet.containsPlace(selectedPlace)) {
			petrinet.addTokens(selectedPlace, i);
			petrinet.setCurrentAsInitialMarking();

			IPetrinetView visualGraph = view.getPetrinetGraph();
			visualGraph.updateTokens(selectedPlace);
			view.getPetrinetGraph().setEnabled(getEnabledTransitions());
		}
		reachabilityGraph = new ReachabilityGraph(petrinet.getInitialMarking());
		view.loadReachabilityGraph(reachabilityGraph);
	}

	// returns true if the Transition t is enabled under the current marking
	private boolean isEnabled(Transition t) {
		boolean canFire = true;
		for (Arc a : t.getPreconditionArcs()) {
			if (a.getPlace().getTokens() < 1) {
				canFire = false;
				break;
			}
		}
		return canFire;
	}

	/**
	 * Returns a list of all Transitions which are enabled under the current marking
	 * 
	 * @return A list of all Transitions which are enabled under the current marking
	 */
	private LinkedList<String> getEnabledTransitions() {
		LinkedList<String> enabledTransitions = new LinkedList<>();
		for (Transition t : petrinet.getTransitions()) {
			if (isEnabled(t))
				enabledTransitions.add(t.getId());
		}
		return enabledTransitions;
	}

	@Override
	public void fireTransition(String id) {
		Transition t = petrinet.getTransition(id);
		if (isEnabled(t)) {
			Marking before = petrinet.getMarking();
			IPetrinetView visualGraph = view.getPetrinetGraph();
			for (Arc a : t.getPreconditionArcs()) {
				Place p = a.getPlace();
				p.addTokens(-1);
				visualGraph.updateTokens(p.getId());
			}
			for (Arc a : t.getPostconditionArcs()) {
				Place p = a.getPlace();
				p.addTokens(+1);
				visualGraph.updateTokens(p.getId());
			}
			Marking after = petrinet.getMarking();

			view.getPetrinetGraph().setEnabled(getEnabledTransitions());

			RGUpdateResult r = reachabilityGraph.addMarking(t, before, after);
			// update reachability graph view
			IReachabilityGraphView rpGView = view.getpRGView();
			if (r == RGUpdateResult.ADDED_NODE) {
				rpGView.addMarking(after);
				rpGView.addEdge(t, before, after);
			} else if (r == RGUpdateResult.ADDED_EDGE) {
				rpGView.addEdge(t, before, after);
			}
			rpGView.updateLastFired(t, before, after);
		}
	}

	@Override
	public void resetInitialMarking(boolean resetReachabilityGraph, boolean updateView) {
		petrinet.resetInitialMarking();
		if (updateView) {
			for (Place p : petrinet.getPlaces()) {
				view.getPetrinetGraph().updateTokens(p.getId());
			}
			view.getPetrinetGraph().setEnabled(getEnabledTransitions());
		}
		if (resetReachabilityGraph) {
			reachabilityGraph = new ReachabilityGraph(petrinet.getInitialMarking());
			if (updateView)
				view.loadReachabilityGraph(reachabilityGraph);
		}
	}

	@Override
	public void performBoundednessAnalysis() {
		view.getTextView().printAnalysisStarted(filename);
		resetInitialMarking(false, true);
		BoundednessAnalysisResult result = boundednessAlgorithm(filename);
		view.getpRGView().load(reachabilityGraph);
		if (!result.isBounded())
			view.getpRGView().hilightPath(result);
		view.getTextView().printSingleResult(result, true);
	}

	// The boundedness analysis algorithm. The algorithm performs a kind of
	// Depth-first search.
	private BoundednessAnalysisResult boundednessAlgorithm(String filename) {
		// initialise necessary data structures
		resetInitialMarking(true, false);
		// Markings on a path. The algorithm uses markings wrapped in a MarkingWrapper
		// that contains an iterator over all transitions for a marking, so the dfs can
		// be performed.
		LinkedList<MarkingWrapper> markingPath = new LinkedList<>();
		// Transitions on a path
		LinkedList<Transition> transitionPath = new LinkedList<>();

		// add first marking to the path
		Iterator<Transition> it = petrinet.getTransitions().iterator();
		MarkingWrapper start = new MarkingWrapper(petrinet.getInitialMarking(), it);
		markingPath.add(start);

		// number of nodes / edges in the constructed reachability graph
		int nodes = 1;
		int edges = 0;
		boolean hasCycle = false;

		/*
		 * In the while-loop the reachability graph is build up step-by-step, according
		 * to depth-first search. After a new marking is added to the graph, the path to
		 * the root is searched for a second marking, which would show that the Petri
		 * net is unbounded. If that is the case a negative BoundednessAnalysisResult is
		 * created and returned. The algorithm stops.
		 * 
		 * As said before, the algorithm is a kind of dfs. If all enabled transitions
		 * under a given marking already fired, this means there ar no more markings to
		 * be reached on this path and thus the marking is removed from markingPath. If
		 * markingPath is empty, the whole reachability graph was created and thus the
		 * Petri net is bounded.
		 */
		while (!markingPath.isEmpty()) {
			MarkingWrapper mw = markingPath.getLast();
			it = mw.getIterator();
			Marking current = mw.getMarking();
			petrinet.setMarking(current);
			// No further markings can be reached from mw, the last marking in the path .
			// It can be removed from the path.
			if (!it.hasNext()) {
				markingPath.removeLast();
				if (!transitionPath.isEmpty())
					transitionPath.removeLast();
				continue;
			}

			Transition t = it.next();
			if (isEnabled(t)) {
				Marking m = fire(t);

				for (MarkingWrapper tmw : markingPath) {
					if (tmw.getMarking().equals(m)) {
						hasCycle = true;
						break;
					}
				}

				RGUpdateResult r = reachabilityGraph.addMarking(t, current, m);
				if (r == RGUpdateResult.ADDED_NODE) {
					nodes++;
					edges++;
					Iterator<Transition> newIt = petrinet.getTransitions().iterator();
					MarkingWrapper newmw = new MarkingWrapper(m, newIt);
					markingPath.addLast(newmw);
					transitionPath.add(t);
					Marking mhat = isUnbounded(markingPath);
					if (mhat != null) {
						return new BoundednessAnalysisResult(filename, mhat, m,
								transitionPath, markingPath);
					}
				} else if (r == RGUpdateResult.ADDED_EDGE) {
					edges++;
				}
			}
		}

		// the whole reachability graph was created, this means the Petri net is bounded
		return new BoundednessAnalysisResult(filename, nodes, edges, hasCycle);
	}

	// checks whether there is a marking on the passed in path, which has fewer tokens
	// in at least one place and not more tokens in the rest of the places compared
	// with the last marking in the path.
	private Marking isUnbounded(LinkedList<MarkingWrapper> path) {
		Marking last = path.getLast().getMarking();
		int length = last.length();
		boolean unbounded = false;
		for (MarkingWrapper markingWrapper : path) {
			Marking m = markingWrapper.getMarking();
			for (int i = 0; i < length; i++) {
				if (last.getTokensAt(i) < m.getTokensAt(i)) {
					unbounded = false;
					break;
				} else if (last.getTokensAt(i) > m.getTokensAt(i)) {
					unbounded = true;
				}
			}
			if (unbounded)
				return m;
		}
		return null;
	}

	// Returns the Marking that results if Transition t fires (t must be
	// enabled). No changes to the view.
	private Marking fire(Transition t) {
		for (Arc a : t.getPreconditionArcs()) {
			Place p = a.getPlace();
			p.addTokens(-1);
		}
		for (Arc a : t.getPostconditionArcs()) {
			Place p = a.getPlace();
			p.addTokens(+1);
		}
		return petrinet.getMarking();
	}

	@Override
	public void gotoMarking(String marking) {
		marking = marking.substring(1, marking.length() - 1);
		String[] mArray = marking.split("\\|");
		int[] intArray = new int[mArray.length];
		for (int i = 0; i < intArray.length; i++) {
			intArray[i] = Integer.valueOf(mArray[i]);
		}
		Marking m = new Marking(intArray);
		petrinet.setMarking(m);
		IPetrinetView petrinetView = view.getPetrinetGraph();
		Collection<Place> places = petrinet.getPlaces();
		for (Place place : places) {
			petrinetView.updateTokens(place.getId());
		}
		view.getPetrinetGraph().setEnabled(getEnabledTransitions());
	}

	@Override
	public void boundednessAnalysisOnMultipleFiles(File[] files) {
		// save current Petri net / reachability graph
		ITextView textView = view.getTextView();
		IPetrinet tempPetrinet = petrinet;
		IReachabilityGraph tempRG = reachabilityGraph;
		LinkedList<BoundednessAnalysisResult> results = new LinkedList<>();

		Arrays.sort(files, Comparator.comparing(File::getName));
		for (File file : files) {
			textView.printAnalysisStarted(file.getName());
			loadPetrinetinBackground(file);
			BoundednessAnalysisResult result = boundednessAlgorithm(file.getName());
			results.add(result);
			textView.printSingleResult(result, false);
		}

		textView.printHeader();
		for (BoundednessAnalysisResult result : results) {
			textView.printMultiResult(result);
		}

		// restore Petri net / reachability graph
		petrinet = tempPetrinet;
		reachabilityGraph = tempRG;
	}

	@Override
	public void propertyAnalysis(File[] files) {
		ITextView textView = view.getTextView();
		IPetrinet tempPetrinet = petrinet;
		IReachabilityGraph tempRG = reachabilityGraph;

		textView.printPropertyHeader();

		Arrays.sort(files, Comparator.comparing(File::getName));
		for (File file : files) {
			loadPetrinetinBackground(file);
			BoundednessAnalysisResult result = boundednessAlgorithm(file.getName());
			textView.printPropertyResult(result, deadlockFree());
		}

		// restore Petri net / reachability graph
		petrinet = tempPetrinet;
		reachabilityGraph = tempRG;
	}

	private boolean deadlockFree() {
		boolean deadlockFree = true;
		for (Marking marking : reachabilityGraph.getMarkings()) {
			List<RGEdge> edges = reachabilityGraph.getEdges(marking);
			if (edges.size() == 0) {
				deadlockFree = false;
				break;
			}
		}
		return deadlockFree;
	}

}