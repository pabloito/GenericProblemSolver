package ar.edu.itba.sia.gps.eightpuzzle;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;

import static ar.edu.itba.sia.gps.eightpuzzle.E8Problem.solution;

public class E8HeuristicB implements Heuristic {
	
	private E8HeuristicB() {
	}
	
	private static E8HeuristicB instance;
	
	public static E8HeuristicB instance() {
		if (instance == null) {
			instance = new E8HeuristicB();
		}
		return instance;
	}
	
	@Override
	public Integer getValue(State state) {
		int count = 0;
		for (int i = 0; i < 8; i++) {
			if (((E8State) state).getArray()[i].getX() == (solution.getArray()[i].getX())) {
				count++;
			}
			if (((E8State) state).getArray()[i].getY() == (solution.getArray()[i].getY())) {
				count++;
			}
		}
		return count;
	}
}
