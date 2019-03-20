package ar.edu.itba.sia.gps.implementation;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;

public class MaxPathHeuristic implements Heuristic {

    @Override
    public Integer getValue(State state) {
        StateImpl st = (StateImpl) state;
        int cost = 0;

        for (Square square : st.getSquares()){
            int distance = Math.abs(square.getX() - square.getObjective().getX())
                    + Math.abs(square.getY() - square.getObjective().getY());

            cost = Math.max(cost, distance);
        }

        return cost;
    }
}
