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

public class ProblemImpl implements Problem {


    private int width, height;
    private List<Square> squares;
    private List<Changer> changers;
    public ProblemImpl(JSONObject level){
        //parse level
        //this.width = width;
        //this.height = height;

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

            System.out.println(jsonTxt);
            JSONObject json = new JSONObject(jsonTxt);
            return json;
        }
        catch (FileNotFoundException e){
            System.out.println("File "+filename+" not found.");
            return null;
        }
    }
}
