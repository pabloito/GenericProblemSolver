package ar.edu.itba.sia.gps;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;
import ar.edu.itba.sia.gps.implementation.*;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestGPSEngine {


    private GPSEngine gpsEngine;

    @Before
    public void setup(){
        List<Square> squares = new ArrayList<>();
        Square square = new Square(0,0,"red", Direction.DOWN,new Tile(0,2));
        squares.add(square);
        List<Changer> changers = new ArrayList<>();
        State state = new StateImpl(3,3,squares,changers);

        List<Rule> rules = new ArrayList<>();
        rules.add(new RuleImpl(square));

        Heuristic heuristic = new GodfridHeuristic();

        String level_name = "./src/main/java/ar/edu/itba/sia/gps/problems/level_1.json";

        gpsEngine = new GPSEngine(new ProblemImpl(ProblemImpl.readLevel(level_name)),SearchStrategy.IDDFS, heuristic);
    }

    @Test
    public void testFindSolution(){
        gpsEngine.findSolution();

        Assert.assertTrue(gpsEngine.isFinished());
        Assert.assertFalse(gpsEngine.isFailed());
        Assert.assertNotNull(gpsEngine.getSolutionNode());
    }


}
