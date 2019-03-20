package ar.edu.itba.sia.gps.implementation;

import ar.edu.itba.sia.gps.api.Problem;
import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

import java.util.List;

public class ProblemImpl implements Problem {

    private State initState;
    private List<Rule> rules;

    public ProblemImpl(State initState, List<Rule> rules){
        this.initState=initState;
        this.rules=rules;
    }

    @Override
    public State getInitState() {
        return initState;
    }

    @Override
    public boolean isGoal(State state) {
        return ((StateImpl)state).isGoal();
    }

    @Override
    public List<Rule> getRules() {
        return rules;
    }


}
