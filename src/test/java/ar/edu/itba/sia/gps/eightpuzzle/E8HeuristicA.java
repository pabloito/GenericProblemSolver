package ar.edu.itba.sia.gps.eightpuzzle;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;

import static ar.edu.itba.sia.gps.eightpuzzle.E8Problem.solution;

public class E8HeuristicA implements Heuristic {
	
	private E8HeuristicA() {
	}
	
	private static E8HeuristicA instance;
	
	public static E8HeuristicA instance() {
		if (instance == null) {
			instance = new E8HeuristicA();
		}
		return instance;
	}
	
	@Override
	public Integer getValue(State state) {
		int count = 0;
		for (int i = 0; i < 8; i++) {
			if (((E8State) state).getArray()[i].equals(solution.getArray()[i])) {
				count++;
			}
		}
		return count;
	}
}
