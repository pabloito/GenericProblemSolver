package ar.edu.itba.sia.gps.api;

import java.util.List;

/**
 * Problem interface.
 */
public interface Problem {

	/**
	 * Provides the initial state for the GPS to start from.
	 * 
	 * @return The initial state of the problem to be solved.
	 */
	State getInitState();

	/**
	 * Given a state, resolves if it is a solution to the problem.
	 * 
	 * @param state
	 *            The state to establish if it is a goal state.
	 * @return TRUE if the state is a goal state, FALSE otherwise.
	 */
	boolean isGoal(State state);

	/**
	 * Provides the list of all the rules that the problem involves. These rules are state independent.
	 * 
	 * @return The initial state of the problem to be solved.
	 */
	List<Rule> getRules();

}
