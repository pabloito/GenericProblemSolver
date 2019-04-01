package ar.edu.itba.sia.gps.implementation;

import ar.edu.itba.sia.gps.GPSEngine;
import ar.edu.itba.sia.gps.SearchStrategy;
import ar.edu.itba.sia.gps.api.Heuristic;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));
        String filename;
        SearchStrategy searchStrategy;
        Heuristic heuristicImpl = null;

        try {
            if (argsList.size() < 4)
                throw new IllegalArgumentException();
            else {
                int p = argsList.indexOf("-p");
                if (p == -1)
                    throw new IllegalArgumentException();

                filename = argsList.get(p + 1);

                int s = argsList.indexOf("-s");
                if (s == -1)
                    throw new IllegalArgumentException();

                String strategy = argsList.get(s + 1);
                switch (strategy) {
                    case "BFS":
                        searchStrategy = SearchStrategy.BFS;
                        break;
                    case "DFS":
                        searchStrategy = SearchStrategy.DFS;
                        break;
                    case "GREEDY":
                        searchStrategy = SearchStrategy.GREEDY;
                        break;
                    case "IDDFS":
                        searchStrategy = SearchStrategy.IDDFS;
                        break;
                    case "ASTAR":
                        searchStrategy = SearchStrategy.ASTAR;
                        break;
                    default:
                        throw new IllegalArgumentException();
                }

                if (searchStrategy == SearchStrategy.ASTAR || searchStrategy == SearchStrategy.GREEDY) {
                    int h = argsList.indexOf("-h");
                    if (h == -1)
                        throw new IllegalArgumentException();

                    String heuristic = argsList.get(h + 1);
                    switch (heuristic) {
                        case "AVG":
                            heuristicImpl = new AverageDistanceHeuristic();
                            break;
                        case "MAX":
                            heuristicImpl = new MaxPathHeuristic();
                            break;
                        case "GOD":
                            heuristicImpl = new GodfridHeuristic();
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                }

            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid parameters, try: \n" +
                    "\tjava -jar gps-1.0.jar -p filename -s strategy -h [heuristic]\n\n" +
                    "\t-p determines the problem level. 'filename' should be the json with the level representation\n" +
                    "\t-s determines the search strategy, 'strategy' value can be:\n" +
                    "\t\tBFS\n" +
                    "\t\tDFS\n" +
                    "\t\tGREEDY\n" +
                    "\t\tIDDFS\n" +
                    "\t\tASTAR\n" +
                    "\t-h determines the heuristic in case strategy is GREEDY or ASTAR, 'heuristic' value can be:\n" +
                    "\t\tAVG\n" +
                    "\t\tMAX\n" +
                    "\t\tGOD\n");
            return;
        }

        GPSEngine engine = new GPSEngine(new ProblemImpl(ProblemImpl.readLevel(filename)), searchStrategy, heuristicImpl);

        long time_before = System.currentTimeMillis();
        engine.findSolution();
        long time_after = System.currentTimeMillis();
        long computational_time = time_after-time_before;

        System.out.println("");
        System.out.println("Search strategy: " + searchStrategy.name()+"\n");

        switch(searchStrategy.name()){
            case "GREEDY":
            case "ASTAR":
                System.out.println("Heuristic: " + heuristicImpl +"\n");
                break;
            default:
                break;
        }

        System.out.printf("Status: ");
        if(engine.isFailed()){
            ColorsService.colorPrint("red","Failed");
            System.out.println("");
            System.out.println("");
            System.out.println("Initial state: \n");
            System.out.println(engine.getProblem().getInitState().getRepresentation());
        }
        else {
            ColorsService.colorPrint("green","Succeeded");
            System.out.println("");
            System.out.println("");
            System.out.println("Step by step solution: \n");
            System.out.println(engine.getSolutionNode().getSolution());
            System.out.println("--------------------------");
            System.out.println("Solution depth: "+engine.getSolutionNode().getDepth()+"\n");
            System.out.println("Solution cost: "+engine.getSolutionNode().getCost()+"\n");
        }

        System.out.println("Expanded nodes: "+engine.getExplosionCounter()+"\n");
        System.out.println("States analyzed: "+engine.getBestCosts().size()+"\n");
        System.out.println("Frontier nodes: "+engine.getOpen().size()+"\n");
        System.out.println("Computation time: "+computational_time+" ms");
        System.out.println("--------------------------");

    }

}
