package ar.edu.itba.sia.gps;

import java.util.*;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.Problem;
import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;
import ar.edu.itba.sia.gps.implementation.*;

import javax.naming.OperationNotSupportedException;

import static ar.edu.itba.sia.gps.SearchStrategy.IDDFS;
import static ar.edu.itba.sia.gps.implementation.Direction.*;

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
		ProblemImpl p = new ProblemImpl(ProblemImpl.readLevel("./src/main/java/ar/edu/itba/sia/gps/problems/test_level.json"));
		System.out.println("done");
	}

	public GPSEngine(Problem problem, SearchStrategy strategy, Heuristic heuristic) {

		switch(strategy){
			case BFS:
				open = new PriorityQueue<>(Comparator.comparingInt(GPSNode::getDepth).reversed());
				break;
			case DFS:
				open = new PriorityQueue<>(Comparator.comparingInt(GPSNode::getDepth));
				break;
			case ASTAR:
				open = new PriorityQueue<>(Comparator.comparingInt((GPSNode n)-> (n.getCost()+heuristic.getValue(n.getState()))));
				break;
			case IDDFS:
				open = new PriorityQueue<>(Comparator.comparingInt(GPSNode::getDepth));
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
		GPSNode rootNode = new GPSNode(problem.getInitState(), 0, null,0);
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
			depth++;
		}
		finished=true;
		failed=true;
	}

	private IDDFSPackage depthLimitedDFS(GPSNode initNode, int depth) {

		GPSNode curr = initNode;
		boolean remaining=false;

		while(curr!=null) {
			if (curr.getDepth() == depth) {
				if (problem.isGoal(curr.getState())) {
					return new IDDFSPackage(curr, true); //El booleano no importa
				}
				remaining=true;
			}
			else{
				explode(curr);
			}
			curr=open.poll();
		}
		return new IDDFSPackage(null, remaining);
	}

	private void explode(GPSNode node) {
		switch (strategy) {
			case BFS:
			case DFS:
			case IDDFS:
				if (bestCosts.containsKey(node.getState())) {
					return;
				}
				break;
			case GREEDY:
				break;
			case ASTAR:
				if (!isBest(node.getState(), node.getCost())) {
					return;
				}
				break;
		}
		open.addAll(addCandidates(node));
	}

	private Collection<GPSNode> addCandidates(GPSNode node) {
		Collection<GPSNode> candidates = new ArrayList<>();
		explosionCounter++;
		updateBest(node);
		for (Rule rule : problem.getRules()) {
			Optional<State> newState = rule.apply(node.getState());
			if (newState.isPresent()) {
				GPSNode newNode = new GPSNode(newState.get(), node.getCost() + rule.getCost(), rule, node.getDepth()+1);
				newNode.setParent(node);
				candidates.add(newNode);
			}
		}
		return candidates;
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
}
