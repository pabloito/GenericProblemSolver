package ar.edu.itba.sia.gps.implementation;

public enum Direction {
    UP(0,-1, "UP"), DOWN(0,1, "DOWN"), LEFT(-1,0, "LEFT"), RIGHT(1,0, "RIGHT");

    private int x,y;
    private String name;
    Direction(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }
}
