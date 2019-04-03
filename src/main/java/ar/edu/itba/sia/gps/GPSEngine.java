package ar.edu.itba.sia.gps;

import java.util.*;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.Problem;
import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

import static ar.edu.itba.sia.gps.SearchStrategy.IDDFS;

public class GPSEngine {

	private Queue<GPSNode> open;
    private long iddfsNodesViewedPrev;
	private Map<State, CostAndDepth> bestCosts;
	private Problem problem;
	private long explosionCounter;
	private boolean finished;
	private boolean failed;
	private GPSNode solutionNode;
	private Heuristic heuristic;
	private long analyzedCounter;

	// Use this variable in open set order.
	private  SearchStrategy strategy;

	public GPSEngine(Problem problem, SearchStrategy strategy, Heuristic heuristic) {

		switch(strategy){
			case BFS:
				open = new PriorityQueue<>(Comparator.comparingInt(GPSNode::getDepth));
				break;
			case DFS:
				open = new PriorityQueue<>(Comparator.comparingInt(GPSNode::getDepth).reversed());
				break;
			case ASTAR:
				open = new PriorityQueue<>(Comparator.comparingDouble((GPSNode n)-> (n.getCost()+heuristic.getValue(n.getState())+Math.random())));
				break;
			case IDDFS:
				open = new PriorityQueue<>(Comparator.comparingInt(GPSNode::getDepth).reversed());
				break;
			case GREEDY:
				open = new PriorityQueue<>(Comparator.comparingDouble((GPSNode n) -> (heuristic.getValue(n.getState())+Math.random())));
				break;
		}

		bestCosts = new HashMap<>();
		iddfsNodesViewedPrev = 0;
		this.problem = problem;
		this.strategy = strategy;
		this.heuristic = heuristic;
		explosionCounter = 0;
		analyzedCounter = 0;
		finished = false;
		failed = false;
	}

	public void findSolution() {
		GPSNode rootNode = new GPSNode(problem.getInitState(), 0, null, 0);
		if (strategy == IDDFS) {
			findSolution_IDDFS(rootNode);
		} else {
			open.add(rootNode);
			findSolution_Generic();
		}
	}
	private void findSolution_Generic()
	{
		while (open.size() > 0) {
			GPSNode currentNode = open.remove();
			analyzedCounter++;
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

	private void findSolution_IDDFS(GPSNode initNode) {
		IDDFSPackage pack = new IDDFSPackage(null,true);
		int depth =0;
		while(pack.remainingNodes)
		{
			Initialize_IDDFS(initNode);
			pack= depthLimitedDFS(depth);
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

	private void Initialize_IDDFS(GPSNode initNode) {
        iddfsNodesViewedPrev = bestCosts.size();
		bestCosts.clear();
		open.clear();
		open.add(initNode);
	}


	private IDDFSPackage depthLimitedDFS(int depth)	{
		while (open.size() > 0) {
			GPSNode currentNode = open.remove();
			if(currentNode.getDepth()==depth){
				if (problem.isGoal(currentNode.getState())) {
					return new IDDFSPackage(currentNode,true);
				}
				analyzedCounter++;
				explode(currentNode,depth);
			}
			else{
				explode(currentNode);
			}
		}
		return new IDDFSPackage(null,hasRemainingNodes()|| depth==0);
	}

	private boolean hasRemainingNodes() {
		return bestCosts.size()!=iddfsNodesViewedPrev;
	}

	private void explode(GPSNode node){
		explode(node,-1);
	}

	private void explode(GPSNode node, int depth) {
		switch (strategy) {
			case BFS:
			case DFS:
			case GREEDY:
				if (bestCosts.containsKey(node.getState())) {
					return;
				}
				break;
			case IDDFS:
				if (!isShallowest(node.getState(),node.getDepth())) {
					return;
				}
				if(node.getDepth()==depth){
					return;
				}
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
		return !bestCosts.containsKey(state) || cost < bestCosts.get(state).getCost();
	}

	private boolean isShallowest(State state, Integer depth){
		return !bestCosts.containsKey(state) || depth < bestCosts.get(state).getDepth();

	}

	private void updateBest(GPSNode node) {
		bestCosts.put(node.getState(), new CostAndDepth(node.getCost(),node.getDepth()));
	}




	private class CostAndDepth{
		private int cost;
		private int depth;
		CostAndDepth(int cost, int depth){
			this.cost=cost;
			this.depth=depth;
		}

		public int getCost() {
			return cost;
		}

		public int getDepth() {
			return depth;
		}
	}



	// GETTERS FOR THE PEOPLE!

	public Queue<GPSNode> getOpen() {
		return open;
	}

	public Map<State, CostAndDepth> getBestCosts() {
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
		private boolean remainingNodes;
		private GPSNode node;

		public IDDFSPackage(GPSNode node, boolean remainingNodes)
		{
			this.node = node;
			this.remainingNodes = remainingNodes;
		}
	}

	public long getAnalyzedCounter() {
		return analyzedCounter;
	}
}
