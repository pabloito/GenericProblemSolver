package ar.edu.itba.sia.gps.eightpuzzle.rules;

import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;
import ar.edu.itba.sia.gps.eightpuzzle.E8State;
import ar.edu.itba.sia.gps.eightpuzzle.Pair;

import java.util.Optional;

/**
 * Created by eric on 15/03/17.
 */
public abstract class E8MoveRule implements Rule {

    @Override
    public Integer getCost() {
        return 1;
    }

    public Optional<State> apply(State state, Pair destiny){
        E8State e8state = (E8State) state;
        Pair[] array = e8state.getArray().clone();
        Pair blank = e8state.getBlank();
        int index;
        E8State ans = e8state;
        for(int i = 0; i < 8; i++){
            if(destiny.equals(array[i])){
                array[i]=blank;
                blank=destiny;
                ans = new E8State(blank,array);
            }
        }
        return Optional.of(ans);
    }
}
