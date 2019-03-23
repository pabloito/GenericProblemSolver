package ar.edu.itba.sia.gps;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.implementation.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestGPSEngine {


    private GPSEngine gpsEngine;

    private String level_name;

    public TestGPSEngine(String level_name){
        this.level_name=level_name;
    }

    @Before
    public void setup(){
        Heuristic heuristic = new GodfridHeuristic();
        gpsEngine = new GPSEngine(new ProblemImpl(ProblemImpl.readLevel(level_name)),SearchStrategy.BFS, heuristic);
    }

    @Test
    public void testFindSolution(){
        gpsEngine.findSolution();

        Assert.assertTrue(gpsEngine.isFinished());
        Assert.assertFalse(gpsEngine.isFailed());
        Assert.assertNotNull(gpsEngine.getSolutionNode());
        System.out.println(gpsEngine.getSolutionNode().getSolution());
    }

    @Parameterized.Parameters
    public static Collection paramaters(){
        return Arrays.asList(new Object[][]
                {
                        {"./src/main/java/ar/edu/itba/sia/gps/problems/level_1.json"},
                        {"./src/main/java/ar/edu/itba/sia/gps/problems/level_2.json"},
                        {"./src/main/java/ar/edu/itba/sia/gps/problems/level_3.json"},
                        {"./src/main/java/ar/edu/itba/sia/gps/problems/gameaboutsquares_com_level_14.json"},
                });
    }
}
