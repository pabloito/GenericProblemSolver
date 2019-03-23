package ar.edu.itba.sia.gps.implementation;

import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

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
        return square.getColor()+" square moves " + square.getDirection().getName();
    }

    @Override
    public Optional<State> apply(State state) {

        StateImpl oldState = (StateImpl) state;

        Square oldSquare = getSquareFromState(oldState);

        //Saves the direction beforehand in case of Changer tiles
        Direction direction = oldSquare.getDirection();

        StateImpl changed = new StateImpl(oldState.getWidth(), oldState.getHeight(), oldState.getSquares(), oldState.getChangers());

        //Checks before moving if next tile is occupied by a square
        Optional<Tile> next = changed.getTile(oldSquare.getX()+ direction.getX(), oldSquare.getY()+direction.getY());

        Tile current = changed.getTile(oldSquare.getX(),oldSquare.getY()).get();
        if (current instanceof Changer)
            this.square = ((Changer)current).getSquare().get();
        else
            this.square = (Square)current;

        Deque<Tile> tilesToBeMoved = new ArrayDeque<>();

        while (next.isPresent() && (next.get() instanceof Square || ( next.get() instanceof Changer && ((Changer) next.get()).getSquare().isPresent()))){
                tilesToBeMoved.add(current);
                current = next.get();
                Tile aux = next.get();
                next = changed.getTile(aux.getX()+ direction.getX(), aux.getY()+direction.getY());
        }

        //If trying to move out of bounds, returns Null state
        if(!next.isPresent())
            return Optional.empty();

        tilesToBeMoved.add(current);

        while (!tilesToBeMoved.isEmpty()){
                Tile t = tilesToBeMoved.removeLast();
                Tile destination = changed.getTile(t.getX()+direction.getX(),t.getY()+direction.getY()).get();
                Square square;
                if(t instanceof Changer){
                    square = ((Changer) t).getSquare().get();
                    ((Changer) t).setSquare(null);
                }
                else {
                    square = (Square)t;
                    changed.getBoard()[t.getX()][t.getY()] = new Tile(t.getX(),t.getY());
                }
                square.move(direction);
                changed.getBoard()[destination.getX()][destination.getY()] = square;
                if(destination instanceof Changer) {
                    square.setDirection(((Changer) destination).getDirection());
                    ((Changer) destination).setSquare(square);
                }
        }

        return Optional.of(changed);
    }

    private Square getSquareFromState(StateImpl oldState) {
        for(Square square : oldState.getSquares()){
            if(square.getColor().equals(this.square.getColor())){
                return square;
            }
        }
        throw new IllegalArgumentException("Square color not found in state");
    }
}
