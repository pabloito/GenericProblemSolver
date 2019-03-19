package gps;

import java.util.*;

import gps.api.Heuristic;
import gps.api.Problem;
import gps.api.Rule;
import gps.api.State;
import gps.implementation.*;

import javax.naming.OperationNotSupportedException;

import static gps.SearchStrategy.IDDFS;
import static gps.implementation.Direction.*;

public class GPSEngine {

	private Queue<GPSNode> open;
	private Map<State, Integer> bestCosts;
	private Problem problem;
	private long explosionCounter;
	private boolean finished;
	private boolean failed;
	private GPSNode solutionNode;
	private Heuristic heuristic;

	// Use this variable in open set order.
	private  SearchStrategy strategy;

	public static void main(String args[]){
		Heuristic heuristic1 = new HeuristicOneImpl();
		Direction up = UP;
		Direction down = DOWN;
		Direction left = LEFT;
		Direction right = RIGHT;

		List<Square> squares = new ArrayList<>();

		squares.add(new Square(1,0,"red",down,new Tile(1,3)));
		squares.add(new Square(2,1,"green",down,new Tile(0,2)));
		squares.add(new Square(3,2,"blue",left,new Tile(0,3)));

		State s = new StateImpl(4,4,squares, null);

		System.out.println("El valor de h(s) es " + heuristic1.getValue(s));
	}

	public GPSEngine(Problem problem, SearchStrategy strategy, Heuristic heuristic) {

		switch(strategy){
			case BFS:
				open = new LinkedList<>();
				break;
			case DFS:
				open = new PriorityQueue<>(Comparator.comparingInt(GPSNode::getCost));
				break;
			case ASTAR:
				open = new PriorityQueue<>(Comparator.comparingInt((GPSNode n)-> (n.getCost()+heuristic.getValue(n.getState()))));
				break;
			case IDDFS:
				//no se usa queue
				break;
			case GREEDY:
				open = new PriorityQueue<>(Comparator.comparingInt((GPSNode n) -> heuristic.getValue(n.getState())));
				break;
		}

		bestCosts = new HashMap<>();
		this.problem = problem;
		this.strategy = strategy;
		this.heuristic = heuristic;
		explosionCounter = 0;
		finished = false;
		failed = false;
	}

	public void findSolution() {
		GPSNode rootNode = new GPSNode(problem.getInitState(), 0, null);
		open.add(rootNode);
		if(strategy==IDDFS)
		{
			findSolution_IDDFS(rootNode);
			return;
		}
		while (open.size() <= 0) {
			GPSNode currentNode = open.remove();
			if (problem.isGoal(currentNode.getState())) {
				finished = true;
				solutionNode = currentNode;
				return;
			} else {
				explode(currentNode);
			}
		}
		failed = true;
		finished = true;
	}

	private void findSolution_IDDFS(GPSNode rootNode) {
		IDDFSPackage pack = new IDDFSPackage(null,true);
		int depth =0;
		while(pack.remaindingNodes)
		{
			pack= depthLimitedDFS(rootNode,depth);
			if(pack.node!=null){
				finished=true;
				solutionNode=pack.node;
				return;
			}
			if(!pack.remaindingNodes) {
				finished=true;
				failed=true;
				return;
			}
			depth++;

		}
	}

	private IDDFSPackage depthLimitedDFS(GPSNode node, int depth) {
		if(depth==0){
			if(problem.isGoal(node.getState())) {
				return new IDDFSPackage(node, true); //El booleano no importa
			}
			return new IDDFSPackage(null, true);
		}
		boolean remaining = false;
		Collection<GPSNode> collection = new LinkedList<>();
		addCandidates(node,collection);

		for (GPSNode childNode: collection ) {
			IDDFSPackage pack = depthLimitedDFS(childNode, depth-1);
			if(pack.node!=null){
				return new IDDFSPackage(pack.node,true);
			}
			remaining|=pack.remaindingNodes;
		}
		return new IDDFSPackage(null,remaining);

	}

	private class IDDFSPackage
	{
		private boolean remaindingNodes;
		private GPSNode node;

		public IDDFSPackage(GPSNode node, boolean remaindingNodes)
		{
			this.node=node;
			this.remaindingNodes=remaindingNodes;
		}
	}

	private void explode(GPSNode node) {
		Collection<GPSNode> newCandidates;
		switch (strategy) {
		case BFS:
			if (bestCosts.containsKey(node.getState())) {
				return;
			}
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);
			open.addAll(newCandidates);
			break;
		case DFS:
			if (bestCosts.containsKey(node.getState())) {
				return;
			}
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);
			open.addAll(newCandidates);
			break;
		case IDDFS:
			if (bestCosts.containsKey(node.getState())) {
				return;
			}
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);
			// TODO: ¿Cómo se agregan los nodos a open en IDDFS?
			break;
		case GREEDY:
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);
			open.addAll(newCandidates);
			break;
		case ASTAR:
			if (!isBest(node.getState(), node.getCost())) {
				return;
			}
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);
			open.addAll(newCandidates);
			break;
		}
	}

	private void addCandidates(GPSNode node, Collection<GPSNode> candidates) {
		explosionCounter++;
		updateBest(node);
		for (Rule rule : problem.getRules()) {
			Optional<State> newState = rule.apply(node.getState());
			if (newState.isPresent()) {
				GPSNode newNode = new GPSNode(newState.get(), node.getCost() + rule.getCost(), rule);
				newNode.setParent(node);
				candidates.add(newNode);
			}
		}
	}

	private boolean isBest(State state, Integer cost) {
		return !bestCosts.containsKey(state) || cost < bestCosts.get(state);
	}

	private void updateBest(GPSNode node) {
		bestCosts.put(node.getState(), node.getCost());
	}





	// GETTERS FOR THE PEOPLE!

	public Queue<GPSNode> getOpen() {
		return open;
	}

	public Map<State, Integer> getBestCosts() {
		return bestCosts;
	}

	public Problem getProblem() {
		return problem;
	}

	public long getExplosionCounter() {
		return explosionCounter;
	}

	public boolean isFinished() {
		return finished;
	}

	public boolean isFailed() {
		return failed;
	}

	public GPSNode getSolutionNode() {
		return solutionNode;
	}

	public SearchStrategy getStrategy() {
		return strategy;
	}

}
