package ar.edu.itba.sia.gps;

import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

public class GPSNode {

	private State state;

	private GPSNode parent;

	private Integer cost;

	private Integer depth;

	private Rule generationRule;

	public GPSNode(State state, Integer cost, Rule generationRule, Integer depth) {
		this.state = state;
		this.cost = cost;
		this.generationRule = generationRule;
		this.depth = depth;
	}

	public GPSNode getParent() {
		return parent;
	}

	public void setParent(GPSNode parent) {
		this.parent = parent;
	}

	public State getState() {
		return state;
	}

	public Integer getCost() {
		return cost;
	}

	@Override
	public String toString() {
		return state.toString();
	}

	public String getSolution() {
		if (this.parent == null) {
			return this.state.getRepresentation();
		}
		return this.parent.getSolution() + this.state.getRepresentation();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GPSNode other = (GPSNode) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

	public Rule getGenerationRule() {
		return generationRule;
	}

	public void setGenerationRule(Rule generationRule) {
		this.generationRule = generationRule;
	}

	public Integer getDepth() {
		return depth;
	}
}
