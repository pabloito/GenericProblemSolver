package ar.edu.itba.sia.gps.implementation;

import ar.edu.itba.sia.gps.api.Problem;
import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

import java.util.List;

public class ProblemImpl implements Problem {
    @Override
    public State getInitState() {
        return null;
    }

    @Override
    public boolean isGoal(State state) {
        return ((StateImpl)state).isGoal();
    }

    @Override
    public List<Rule> getRules() {
        return null;
    }
}
