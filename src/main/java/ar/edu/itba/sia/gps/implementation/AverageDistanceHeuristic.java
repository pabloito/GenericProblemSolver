package ar.edu.itba.sia.gps.implementation;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;

public class AverageDistanceHeuristic implements Heuristic {

    @Override
    public Integer getValue(State state) {
        StateImpl st = (StateImpl) state;

        int distance=0;
        for (Square square : st.getSquares()){
            distance += Math.abs(square.getX() - square.getObjective().getX()) + Math.abs(square.getY() - square.getObjective().getY());
        }

        return distance/st.getSquares().size();
    }
}
