package gps.implementation;

import gps.api.State;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class StateImpl implements State {

    private Tile[][] board;

    private List<Square> squares;
    private List<Changer> changers;
    private int width, height;

    @Override
    public int hashCode() {
        return squares.hashCode();
    }

    public StateImpl(int width, int height, List<Square> squares, List<Changer> changers) {
        board = new Tile[width][height];
        this.squares = squares;
        this.changers = changers;
        this.width = width;
        this.height = height;
        initializeBoard();
    }

    private void initializeBoard() {
        for(Square s: squares){
            board[s.getX()][s.getY()] = s;
        }
        if(changers!=null){
            for(Changer c: changers){
                board[c.getX()][c.getY()] = c;
            }
        }
        for(int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                if(board[j][i] == null){
                    board[j][i] = new Tile(j,i);
                }
            }
        }

    }

    public void setTile(int x, int y, Tile tile){
        board[x][y]=tile;
    }

    @Override
    public String getRepresentation() {
        String rep = "";
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(board[j][i] instanceof Square)
                    rep += "S";
                else if(board[j][i] instanceof Changer) {
                    switch (((Changer) board[j][i]).getDirection()) {
                        case UP:
                            rep += "^";
                            break;
                        case DOWN:
                            rep += "v";
                            break;
                        case LEFT:
                            rep += "<";
                            break;
                        case RIGHT:
                            rep += ">";
                            break;
                    }
                }
                else{
                    boolean flag = false;
                    for(Square s : squares) {
                        if(s.getObjective().getX() == j && s.getObjective().getY() == i){
                            rep += "o";
                            flag = true;
                        }
                    }
                    if(!flag)
                        rep += "x";


                }
                rep+="  ";
            }
            rep += "\n";
        }
        return rep;
    }


    public boolean isGoal() {
        for(Square s : squares) {
            if (!s.atObjective())
                return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object state) {
        if(state instanceof State){
            StateImpl s2 = (StateImpl)state;
            if(s2.getSquares().size() == this.getSquares().size() && s2.getChangers().size() == this.getChangers().size()){
                if(!Arrays.deepEquals(s2.getSquares().toArray(), this.getSquares().toArray()) || !Arrays.deepEquals(s2.getChangers().toArray(), this.getChangers().toArray()))
                    return false;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
        return true;
    }

    public Optional<Tile> getTile(int x, int y) {
        if(x>=width || y>=height)
            return Optional.empty();
        return Optional.of(board[x][y]);
    }

    public List<Square> getSquares() {
        return squares;
    }

    public List<Changer> getChangers() {
        return changers;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public Tile[][] getBoard() {
        return board;
    }
}
