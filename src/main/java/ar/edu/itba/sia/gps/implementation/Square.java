package ar.edu.itba.sia.gps.implementation;

public class Square extends Tile {
    private String color;
    private Direction direction;
    private Tile objective;

    public Square(int x, int y, String color, Direction direction, Tile objective) {
        super(x,y);
        this.color = color;
        this.direction = direction;
        this.objective = objective;
    }


    public String getColor() {
        return color;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Tile getObjective() {
        return objective;
    }

    public boolean atObjective() {
        return objective.getX()==this.getX() && objective.getY()==this.getY();
    }

    public void move() {
        this.setX(this.getX() + this.direction.getX());
        this.setY(this.getY() + this.direction.getY());
    }

    public void move(Direction direction) {
        this.setX(this.getX() + direction.getX());
        this.setY(this.getY() + direction.getY());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==this) return true;
        if (!(obj instanceof Square))
            return false;
        Square s2 = (Square)obj;

        return this.getX()==s2.getX() && this.getY() == s2.getY() &&
                this.getObjective().equals(s2.getObjective()) &&
                this.getDirection().equals(s2.getDirection());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + color.hashCode();
        result = 31 * result + direction.hashCode();
        result = 31 * result + objective.hashCode();
        return result;
    }
}
