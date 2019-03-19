package ar.edu.itba.sia.gps.api;

public interface Heuristic {


    /**
     * Computes the value of the Heuristic for the given state.
     *
     * @param state
     *            The state where the Heuristic should be computed.
     * @return The value of the Heuristic.
     */
    Integer getValue(State state);
}
