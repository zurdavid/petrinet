package org.zurdavid.petrinets.model.reachabilitygraph;

import org.zurdavid.petrinets.IReachabilityGraph;
import org.zurdavid.petrinets.controller.GraphController;
import org.zurdavid.petrinets.model.Marking;

/**
 * Reachability graph update result. The enum is used to by the controller, to
 * determine wether or not the reachability graphs view needs to be updated and
 * if so how, after a transition fired. See
 * {@link IReachabilityGraph#addMarking(Transition, Marking, Marking)},
 * {@link GraphController#fireTransition(String)}
 * 
 * @author David Zurschmitten
 */
public enum RGUpdateResult {
	ADDED_NODE, ADDED_EDGE, NONE;
}