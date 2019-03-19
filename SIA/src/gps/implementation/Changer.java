package gps.implementation;

import java.util.Optional;

public class Changer extends Tile {

    private Direction direction;
    private Square square;
    public Changer(int x, int y, Direction direction) {
        super(x, y);
        this.direction = direction;
    }

    public void setSquare(Square square){
        this.square = square;
    }

    public Optional<Square> getSquare() {
        return Optional.ofNullable(square);
    }

    public Direction getDirection() {
        return direction;
    }
}
