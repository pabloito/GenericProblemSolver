package gps.implementation;

import gps.api.State;

import java.util.List;

public class StateImpl implements State {

    private Tile[][] board;

    private List<Square> squares;

    public StateImpl(int width, int height, List<Square> squares) {
        board = new Tile[width][height];
        this.squares = squares;
    }

    public void setTile(int x, int y, Tile tile){
        board[x][y]=tile;
    }

    @Override
    public String getRepresentation() {
        return null;
    }

    public boolean isGoal() {
        for(Square s : squares) {
            if (!s.atObjective())
                return false;
        }
        return true;
    }

    public List<Square> getSquares() {
        return squares;
    }
}
