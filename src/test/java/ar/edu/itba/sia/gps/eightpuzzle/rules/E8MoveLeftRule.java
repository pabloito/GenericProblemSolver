package ar.edu.itba.sia.gps.eightpuzzle.rules;

import ar.edu.itba.sia.gps.api.State;
import ar.edu.itba.sia.gps.eightpuzzle.E8State;
import ar.edu.itba.sia.gps.eightpuzzle.Pair;

import java.util.Optional;

public class E8MoveLeftRule extends E8MoveRule {

    @Override
    public String getName() {
        return "Move Left";
    }

    @Override
    public Optional<State> apply(State state) {
        E8State e8state = (E8State) state;
        Pair blank = e8state.getBlank();
        if(blank.getX() == 0)
            return Optional.empty();
        Pair destiny = new Pair(blank.getX()-1, blank.getY());
        return super.apply(state,destiny);
    }
}
