package ar.edu.itba.sia.gps.implementation;

import ar.edu.itba.sia.gps.GPSEngine;
import ar.edu.itba.sia.gps.SearchStrategy;
import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.Problem;
import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;
import org.json.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.InputStream;
import java.util.Scanner;

import static ar.edu.itba.sia.gps.implementation.Direction.*;

public class ProblemImpl implements Problem {


    private int width, height;
    private List<Square> squares;
    private List<Changer> changers;

    public ProblemImpl(JSONObject level){
        squares = new ArrayList<>();
        changers = new ArrayList<>();

        this.width = (int)level.get("width");
        this.height = (int)level.get("height");

        int x,y,obj_x,obj_y;
        String color,direction_string;
        Direction direction;

        Changer newch;
        JSONArray ch = level.getJSONArray("changers");
        for(Object o : ch) {
            JSONObject obj = (JSONObject) o;

            x = (int) obj.get("x");
            y = (int) obj.get("y");
            direction_string = (String) obj.get("direction");

            switch (direction_string) {
                case "up":
                    direction = UP;
                    break;
                case "down":
                    direction = DOWN;
                    break;
                case "left":
                    direction = LEFT;
                    break;
                case "right":
                    direction = RIGHT;
                    break;
                default:
                    direction = null;
            }
            newch = new Changer(x, y, direction);
            changers.add(newch);
        }

        JSONArray sq = level.getJSONArray("squares");
        for(Object o : sq) {
            JSONObject obj = (JSONObject)o;

            x = (int)obj.get("x");
            y = (int)obj.get("y");
            color = (String)obj.get("color");
            direction_string = (String)obj.get("direction");
            obj_x = (int)((JSONObject)obj.get("objective")).get("x");
            obj_y = (int)((JSONObject)obj.get("objective")).get("y");

            switch(direction_string){
                case "up":
                    direction = UP;
                    break;
                case "down":
                    direction = DOWN;
                    break;
                case "left":
                    direction = LEFT;
                    break;
                case "right":
                    direction = RIGHT;
                    break;
                default:
                    direction = null;
            }

            Square newSquare = new Square(x,y,color,direction,new Tile(obj_x,obj_y));

            squares.add(newSquare);
            for (Changer changer: changers) {
                if(changer.getX()==x && changer.getY()==y)
                {
                    changer.setSquare(newSquare);
                }
            }
        }
    }

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

    @Override
    public State getInitState() {
       return new StateImpl(width, height, squares, changers);
    }

    @Override
    public boolean isGoal(State state) {
        return ((StateImpl)state).isGoal();
    }

    @Override
    public List<Rule> getRules() {
        List<Rule> rules = new ArrayList<>();
        StateImpl state = (StateImpl)getInitState();
        for(Square square : state.getSquares())
        {
            rules.add(new RuleImpl(square));
        }
        return rules;
    }

    public static JSONObject readLevel(String filename){
        try {
            InputStream is = new FileInputStream(filename);

            Scanner s = new Scanner(is).useDelimiter("\\A");
            String jsonTxt = s.hasNext() ? s.next() : "";

            return new JSONObject(jsonTxt);
        }
        catch (FileNotFoundException e){
            System.out.println("File "+filename+" not found.");
            return null;
        }
    }
}
