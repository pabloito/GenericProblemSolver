package gps.implementation;

import gps.api.Heuristic;
import gps.api.State;

public class HeuristicOneImpl implements Heuristic {

    @Override
    public Integer getValue(State state) {
        int max_up=0, max_down=0, max_left=0, max_right=0;

        StateImpl st = (StateImpl) state;
        int x, y;
        for (Square square:st.getSquares()){
            x = Math.abs(square.getObjective().getX() - square.getX());
            y = Math.abs(square.getObjective().getY() - square.getY());

            if(x<0)
                max_left = max_left > x*-1 ? max_left : x;
            else
                max_right = max_right > x ? max_right : x;

            if(y<0)
                max_down = max_down > y*-1 ? max_down : y;
            else
                max_up = max_up > y ? max_up : y;

        }

        return max_up + max_down + max_left + max_right;
    }
}
