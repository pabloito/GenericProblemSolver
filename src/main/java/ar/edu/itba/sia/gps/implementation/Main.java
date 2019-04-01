package ar.edu.itba.sia.gps.implementation;

import ar.edu.itba.sia.gps.GPSEngine;
import ar.edu.itba.sia.gps.SearchStrategy;
import ar.edu.itba.sia.gps.api.Heuristic;

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
        engine.findSolution();
        System.out.println("Search strategy: " + searchStrategy.name());
        System.out.println("Search " + (engine.isFailed()? "failed" : "succeeded"));
        System.out.println("");
        System.out.println(engine.getSolutionNode().getSolution());
        // TODO: imprimir cosas que pide el enunciado

    }

}
