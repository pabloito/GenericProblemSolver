package gps.implementation;

import gps.api.Rule;
import gps.api.State;

import java.util.Optional;

public class RuleImpl implements Rule {

    @Override
    public Integer getCost() {
        return 1;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Optional<State> apply(State state) {
        return Optional.empty();
    }
}
