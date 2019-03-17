package gps.api;

import java.util.Optional;

import gps.exception.NotAppliableException;

/**
 * Rule interface.
 */
public interface Rule {

	/**
	 * Provides the cost of the rule.
	 * 
	 * @return The cost of the rule.
	 */
	Integer getCost();

	/**
	 * Provides the name of the rule so it can be printed on the solution representation.
	 * 
	 * @return The name of the rule.
	 */
	String getName();

	/**
	 * Computes the next state based on the previous one. The next state is the result of applying the rule to the previous state.
	 * 
	 * @param state
	 *            The previous state of the problem.
	 * @return The next state of the problem, or NULL if it can't be applied to such state.
	 */
	Optional<State> apply(State state);
}
