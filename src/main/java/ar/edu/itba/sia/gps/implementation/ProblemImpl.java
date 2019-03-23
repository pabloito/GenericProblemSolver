package ar.edu.itba.sia.gps.implementation;

import ar.edu.itba.sia.gps.api.Problem;
import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;
import org.json.*;

import java.io.*;
import java.util.ArrayList;
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

            squares.add(new Square(x,y,color,direction,new Tile(obj_x,obj_y)));
        }

        Square square;

        JSONArray ch = level.getJSONArray("changers");
        for(Object o : ch) {
            JSONObject obj = (JSONObject)o;

            x = (int)obj.get("x");
            y = (int)obj.get("y");
            direction_string = (String)obj.get("direction");

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

            JSONObject chsq = (JSONObject) ((JSONObject)o).get("square");

            Changer newch = new Changer(x,y,direction);

            if(chsq!=null){
                x = (int)chsq.get("x");
                y = (int)chsq.get("y");
                color = (String)chsq.get("color");
                direction_string = (String)chsq.get("direction");

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

                obj_x = (int)((JSONObject)chsq.get("objective")).get("x");
                obj_y = (int)((JSONObject)chsq.get("objective")).get("y");

                square = new Square(x,y,color,direction,new Tile(obj_x,obj_y));

                newch.setSquare(square);
            }
            changers.add(newch);
        }
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

            JSONObject json = new JSONObject(jsonTxt);
            return json;
        }
        catch (FileNotFoundException e){
            System.out.println("File "+filename+" not found.");
            return null;
        }
    }
}
