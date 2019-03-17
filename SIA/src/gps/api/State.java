package gps.api;

/**
 * State interface.
 */
public interface State {

	/**
	 * Compares self to another state to determine whether they are the same or not.
	 * 
	 * @param state
	 *            The state to compare to.
	 * @return true if self is the same as the state given, false if they are different.
	 */
	boolean equals(Object state);


	/**
	 * Provides the representation of the state so it can be printed on the solution representation.
	 *
	 * @return The STRING representation of the state.
	 */
	String getRepresentation();

	boolean isGoal();

}
