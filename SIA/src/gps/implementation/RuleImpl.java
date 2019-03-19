package gps.implementation;

import gps.api.Rule;
import gps.api.State;

import java.util.*;

public class RuleImpl implements Rule {

    private Square square;

    public RuleImpl(Square square) {
        this.square = square;
    }

    @Override
    public Integer getCost() {
        return 1;
    }

    @Override
    public String getName() {
        return square.getColor()+" square moves to the" + square.getDirection().getName();
    }

    @Override
    public Optional<State> apply(State state) {

        //Saves the direction beforehand in case of Changer tiles
        Direction direction = square.getDirection();

        State changed = new StateImpl(((StateImpl) state).getWidth(), ((StateImpl) state).getHeight(), ((StateImpl) state).getSquares(), ((StateImpl) state).getChangers());

        //Checks before moving if next tile is occupied by a square
        Optional<Tile> next = ((StateImpl)changed).getTile(square.getX()+ direction.getX(), square.getY()+direction.getY());

        Tile current = this.square;

        Deque<Tile> tilesToBeMoved = new ArrayDeque<>();

        while (next.isPresent() && (next.get() instanceof Square || ( next.get() instanceof Changer && ((Changer) next.get()).getSquare().isPresent()))){
                tilesToBeMoved.add(current);
                current = next.get();
                Tile aux = next.get();
                next = ((StateImpl)changed).getTile(aux.getX()+ direction.getX(), aux.getY()+direction.getY());
        }

        //If trying to move out of bounds, returns Null state
        if(!next.isPresent())
            return Optional.empty();

        tilesToBeMoved.add(current);

        while (!tilesToBeMoved.isEmpty()){
                Tile t = tilesToBeMoved.removeLast();
                Tile destination = ((StateImpl) changed).getTile(t.getX()+direction.getX(),t.getY()+direction.getY()).get();
                Square square;
                if(t instanceof Changer){
                    square = ((Changer) t).getSquare().get();
                    ((Changer) t).setSquare(null);
                }
                else {
                    square = (Square)t;
                    ((StateImpl) changed).getBoard()[t.getX()][t.getY()] = new Tile(t.getX(),t.getY());
                }
                square.move(direction);
                ((StateImpl) changed).getBoard()[destination.getX()][destination.getY()] = square;
                if(destination instanceof Changer) {
                    square.setDirection(((Changer) destination).getDirection());
                    ((Changer) destination).setSquare(square);
                }
        }

        return Optional.of(changed);
    }
}
