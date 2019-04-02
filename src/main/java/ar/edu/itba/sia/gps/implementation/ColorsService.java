package ar.edu.itba.sia.gps.implementation;

public class ColorsService {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void colorPrint(String color, String text){
        switch(color){
            case "red":
                System.out.print(ANSI_RED+text+ANSI_RESET);
                break;
            case "blue":
                System.out.print(ANSI_BLUE+text+ANSI_RESET);
                break;
            case "green":
                System.out.print(ANSI_GREEN+text+ANSI_RESET);
                break;
            case "purple":
                System.out.print(ANSI_PURPLE+text+ANSI_RESET);
                break;
        }
    }

    public static String getColorCode(String color){
        switch(color){
            case "red":
                return ANSI_RED;
            case "blue":
                return ANSI_BLUE;
            case "green":
                return ANSI_GREEN;
            case "purple":
                return ANSI_PURPLE;
            case "cyan":
                return ANSI_CYAN;
            case "yellow":
                return ANSI_YELLOW;
            case "black":
                return ANSI_BLACK;
        }
        return ANSI_WHITE;
    }
}
