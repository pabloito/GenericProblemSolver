package ar.edu.itba.sia.gps;

import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;
import ar.edu.itba.sia.gps.implementation.ProblemImpl;
import ar.edu.itba.sia.gps.implementation.Square;
import ar.edu.itba.sia.gps.implementation.StateImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TestRuleImpl {
    private ProblemImpl p;
    @Before
    public void setup(){
        p = new ProblemImpl(ProblemImpl.readLevel("./src/main/java/ar/edu/itba/sia/gps/problems/level_1.json"));
    }

    @Test
    public void testMoved(){
        List<Rule> rules = p.getRules();
        State init = p.getInitState();
        List<Square> squares = ((StateImpl)init).getSquares();
        Assert.assertEquals(squares.size(), rules.size());
        Square s = squares.get(0);
        State two = rules.get(0).apply(init).get();
        List<Square> squares2 = ((StateImpl)two).getSquares();
        Assert.assertEquals(squares.get(0).getX()+s.getDirection().getX(), squares2.get(0).getX());
        Assert.assertEquals(squares.get(0).getY()+s.getDirection().getY(), squares2.get(0).getY());
    }
}
