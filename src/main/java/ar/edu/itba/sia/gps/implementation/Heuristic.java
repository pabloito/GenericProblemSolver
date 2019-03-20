package ar.edu.itba.sia.gps.implementation;

import ar.edu.itba.sia.gps.api.State;

public enum Heuristic {
    maxPath(1), maxXandY(2);

    private Integer index;

    private Heuristic(Integer index) {
        this.index = index;
    }

    public Integer getValue(State state) {
        switch(this) {
            case maxPath : return maxPathGetValue(state);
            case maxXandY : return maxXandYGetValue(state);
            default : return 0;
        }
    }

    private Integer maxPathGetValue(State state) {
        StateImpl st = (StateImpl) state;
        int cost = 0;

        for (Square square : st.getSquares()){
            int distance = Math.abs(square.getX() - square.getObjective().getX())
                    + Math.abs(square.getY() - square.getObjective().getY());

            cost = Math.max(cost, distance);
        }

        return cost;
    }

    private Integer maxXandYGetValue(State state) {
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
