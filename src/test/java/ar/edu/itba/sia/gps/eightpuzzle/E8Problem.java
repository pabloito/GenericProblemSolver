package ar.edu.itba.sia.gps.eightpuzzle;

import ar.edu.itba.sia.gps.api.Problem;
import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;
import ar.edu.itba.sia.gps.eightpuzzle.rules.E8MoveDownRule;
import ar.edu.itba.sia.gps.eightpuzzle.rules.E8MoveLeftRule;
import ar.edu.itba.sia.gps.eightpuzzle.rules.E8MoveRightRule;
import ar.edu.itba.sia.gps.eightpuzzle.rules.E8MoveUpRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 15/03/17.
 */
public class E8Problem implements Problem {
    
    private State initState;

    public static List<Rule> rules;
    public static E8State solution;
    static{
        rules = new ArrayList<>();
        rules.add(new E8MoveDownRule());
        rules.add(new E8MoveUpRule());
        rules.add(new E8MoveRightRule());
        rules.add(new E8MoveLeftRule());
        Pair array[] = new Pair[8];
        array[0] = new Pair(0,0);
        array[1] = new Pair(1,0);
        array[2] = new Pair(2,0);
        array[3] = new Pair(0,1);
        array[4] = new Pair(1,1);
        array[5] = new Pair(2,1);
        array[6] = new Pair(0,2);
        array[7] = new Pair(1,2);
        /*
        * Actual solution table
        * 1 2 3
        * 4 5 6
        * 7 8 B
        * Use for references*/

        solution = new E8State(new Pair(2,2),array);
    }
    @Override
    public List<Rule> getRules() {
        return rules;
    }

    @Override
    public boolean isGoal(State state) {
        return solution.equals(state);
    }

    @Override
    public State getInitState() {
        return initState;
    }
    
    public E8Problem(boolean noSolution){
        if (! noSolution){
    
            Pair array[] = new Pair[8];
            array[0] = new Pair(1,2); //OK
            array[1] = new Pair(2,2); //OK
            array[2] = new Pair(0,2); //OK
            array[3] = new Pair(1,1); //OK
            array[4] = new Pair(2,0); //OK
            array[5] = new Pair(2,1); //OK
            array[6] = new Pair(0,0); //OK
            array[7] = new Pair(1,0); //0K 2,0*/
    
            initState = new E8State(new Pair(0,1),array); //0K  0.2
            /**
             * 7 8 5    1 2 3
             * 3 4 6    4 5 6
             * B 1 2    7 8 B
             */
        }else{
    
    
            Pair array[] = new Pair[8];
            /*array[0] = new Pair(1,0); //0,0
            array[1] = new Pair(1,1); //1,0
            array[2] = new Pair(2,0); //2,0
            array[3] = new Pair(0,0); //2,1
            array[4] = new Pair(2,1); //2,2
            array[5] = new Pair(2,2); //1,2
            array[6] = new Pair(1,2); //0,2
            array[7] = new Pair(0,1); //0,1/
            /*/
            array[0] = new Pair(0,1); //OK
            array[1] = new Pair(2,2); //OK
            array[2] = new Pair(2,1); //OK
            array[3] = new Pair(0,2); //OK
            array[4] = new Pair(2,0); //OK
            array[5] = new Pair(1,1); //OK
            array[6] = new Pair(0,0); //OK
            array[7] = new Pair(1,0); //0K 2,0*/
    
            initState = new E8State(new Pair(1,2),array); //0K  0.2
        }
        
    }
}
