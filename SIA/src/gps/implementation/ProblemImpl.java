package gps.implementation;

import gps.api.Problem;
import gps.api.Rule;
import gps.api.State;

import java.util.List;

public class ProblemImpl implements Problem {
    @Override
    public State getInitState() {
        return null;
    }

    @Override
    public boolean isGoal(State state) {
        return state.isGoal();
    }

    @Override
    public List<Rule> getRules() {
        return null;
    }
}
