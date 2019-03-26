package ar.edu.itba.sia.gps;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.implementation.*;
import org.junit.*;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestGPSEngine {


    private GPSEngine gpsEngine;

    private String level_name;

    private SearchStrategy searchStrategy;

    public TestGPSEngine(String level_name, SearchStrategy searchStrategy){
        this.level_name=level_name;
        this.searchStrategy=searchStrategy;
    }

    @Before
    public void setup(){
        Heuristic heuristic = new GodfridHeuristic();
        gpsEngine = new GPSEngine(new ProblemImpl(ProblemImpl.readLevel("./src/main/java/ar/edu/itba/sia/gps/problems/"+level_name+".json")),searchStrategy, heuristic);
    }


    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void succeeded(long nanos, Description description) {
            fillData(true,nanos);
        }

        @Override
        protected void failed(long nanos, Throwable e, Description description) {
            fillData(false,nanos);
        }

        private void fillData(boolean b, long nanos) {
            MetricComparer.getInstance().addTime(searchStrategy, level_name, nanos);
            MetricComparer.getInstance().addCost(searchStrategy,level_name,gpsEngine.getSolutionNode().getCost());
            MetricComparer.getInstance().addDepth(searchStrategy,level_name,gpsEngine.getSolutionNode().getDepth());
            MetricComparer.getInstance().addResult(searchStrategy,level_name,false);
            MetricComparer.getInstance().addExpandedNodes(searchStrategy,level_name,gpsEngine.getExplosionCounter());
            MetricComparer.getInstance().addFrontierNodes(searchStrategy,level_name,gpsEngine.getFrontierNodes());
            MetricComparer.getInstance().addAnalyzedNodes(searchStrategy,level_name,gpsEngine.getBestCosts().size());

        }

        @Override
        protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
        }

        @Override
        protected void finished(long nanos, Description description) {

        }
    };

    @AfterClass
    public static void printResults(){
        MetricComparer.getInstance().printResults();
    }

    @Test
    public void testFindSolution(){
        gpsEngine.findSolution();

        Assert.assertTrue(gpsEngine.isFinished());
        Assert.assertFalse(gpsEngine.isFailed());
        Assert.assertNotNull(gpsEngine.getSolutionNode());
    }

    @Parameterized.Parameters
    public static Collection paramaters(){
        return Arrays.asList(new Object[][]
                {
                        {"level_1",SearchStrategy.DFS},
                        {"level_2",SearchStrategy.DFS},
                        {"level_3",SearchStrategy.DFS},
                        {"gameaboutsquares_com_level_13",SearchStrategy.DFS},
                        {"gameaboutsquares_com_level_14",SearchStrategy.DFS},
                        {"stress_problem",SearchStrategy.DFS},
                        {"stress_problem_2",SearchStrategy.DFS},

                        {"level_1",SearchStrategy.BFS},
                        {"level_2",SearchStrategy.BFS},
                        {"level_3",SearchStrategy.BFS},
                        {"gameaboutsquares_com_level_13",SearchStrategy.BFS},
                        {"gameaboutsquares_com_level_14",SearchStrategy.BFS},
                        {"stress_problem",SearchStrategy.BFS},
                        {"stress_problem_2",SearchStrategy.BFS},

                        {"level_1",SearchStrategy.IDDFS},
                        {"level_2",SearchStrategy.IDDFS},
                        {"level_3",SearchStrategy.IDDFS},
                        {"gameaboutsquares_com_level_13",SearchStrategy.IDDFS},
                        {"gameaboutsquares_com_level_14",SearchStrategy.IDDFS},
                        {"stress_problem",SearchStrategy.IDDFS},
                        {"stress_problem_2",SearchStrategy.IDDFS},
                       // {"no_solution",SearchStrategy.IDDFS},

                        {"level_1",SearchStrategy.GREEDY},
                        {"level_2",SearchStrategy.GREEDY},
                        {"level_3",SearchStrategy.GREEDY},
                        {"gameaboutsquares_com_level_13",SearchStrategy.GREEDY},
                        {"gameaboutsquares_com_level_14",SearchStrategy.GREEDY},
                        {"stress_problem",SearchStrategy.GREEDY},
                        {"stress_problem_2",SearchStrategy.GREEDY},

                        {"level_1",SearchStrategy.ASTAR},
                        {"level_2",SearchStrategy.ASTAR},
                        {"level_3",SearchStrategy.ASTAR},
                        {"gameaboutsquares_com_level_13",SearchStrategy.ASTAR},
                        {"gameaboutsquares_com_level_14",SearchStrategy.ASTAR},
                        {"stress_problem",SearchStrategy.ASTAR},
                        {"stress_problem_2",SearchStrategy.ASTAR},
                });
    }
}
