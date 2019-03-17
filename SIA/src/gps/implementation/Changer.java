package gps.implementation;

public class Changer extends Tile {

    private Direction direction;

    public Changer(int x, int y, Direction direction) {
        super(x, y);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
