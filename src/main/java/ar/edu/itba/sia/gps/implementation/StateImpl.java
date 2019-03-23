package ar.edu.itba.sia.gps.implementation;

import ar.edu.itba.sia.gps.api.State;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class StateImpl implements State {

    private Tile[][] board;

    private List<Square> squares;
    private List<Changer> changers;
    private int width, height;

    public StateImpl(int width, int height, List<Square> squares, List<Changer> changers) {
        board = new Tile[width][height];

        this.squares = new ArrayList<>();
        for(Square s: squares){
            this.squares.add(new Square(s.getX(),s.getY(),s.getColor(),s.getDirection(),s.getObjective()));
        }
        this.changers = new ArrayList<>();
        if(changers!=null){
            for(Changer c: changers){
                Changer toAdd = new Changer(c.getX(),c.getY(),c.getDirection());
                if(c.getSquare().isPresent()){
                    Square inOld = c.getSquare().get();
                    int indexOf = this.squares.indexOf(inOld);
                    if(indexOf != -1){
                        toAdd.setSquare(this.squares.get(indexOf));
                    }
                }
                this.changers.add(toAdd);
            }
        }
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

    @Override
    public String getRepresentation() {
        String rep = "";
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(board[j][i] instanceof Square){
                    rep += ColorsService.getColorCode(((Square)board[j][i]).getColor());
                    switch (((Square) board[j][i]).getDirection()) {
                        case UP:
                            rep += "\u21D1";
                            break;
                        case DOWN:
                            rep += "\u21D3";
                            break;
                        case LEFT:
                            rep += "\u21D0";
                            break;
                        case RIGHT:
                            rep += "\u21D2";
                            break;
                    }
                    rep += ColorsService.ANSI_RESET;
                } else if(board[j][i] instanceof Changer) {
                    switch (((Changer) board[j][i]).getDirection()) {
                        case UP:
                            rep += "\u2191";
                            break;
                        case DOWN:
                            rep += "\u2193";
                            break;
                        case LEFT:
                            rep += "\u2190";
                            break;
                        case RIGHT:
                            rep += "\u2192";
                            break;
                    }
                } else {
                    boolean flag = false;
                    for(Square s : squares) {
                        if(s.getObjective().getX() == j && s.getObjective().getY() == i){
                            rep += ColorsService.getColorCode(s.getColor());
                            rep += "o";
                            rep += ColorsService.ANSI_RESET;
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
        rep += "\n";
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StateImpl state = (StateImpl) o;

        if (!squares.equals(state.squares)) return false;
        return changers.equals(state.changers);
    }

    @Override
    public int hashCode() {
        int result = squares.hashCode();
        result = 31 * result + changers.hashCode();
        return result;
    }

    /*
    @Override
    public boolean equals(Object state) {
        if(state==this){
            return true;
        }
        if(!(state instanceof State)) {
            return false;
        }
        StateImpl s2 = (StateImpl)state;
        return Arrays.deepEquals(s2.getSquares().toArray(), this.getSquares().toArray()) &&
                    Arrays.deepEquals(s2.getChangers().toArray(), this.getChangers().toArray());
    }


    @Override
    public int hashCode() {
        int result = squares.hashCode();
        return squares.hashCode();
    }
*/

    public Optional<Tile> getTile(int x, int y) {
        if(x>=width || x<0 || y>=height || y<0)
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
