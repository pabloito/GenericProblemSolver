package ar.edu.itba.sia.gps;

import java.util.HashMap;
import java.util.Map;

class MetricComparer {

    private static final MetricComparer onlyInstance = new MetricComparer();

    private Map<String,Map<SearchStrategy,MyTime>> times;
    private Map<String,Map<SearchStrategy,Boolean>> success;
    private Map<String,Map<SearchStrategy,Integer>> depths;
    private Map<String,Map<SearchStrategy,Long>> expandedNodes;
    private Map<String,Map<SearchStrategy,Integer>> analyzedNodes;
    private Map<String,Map<SearchStrategy,Integer>> frontierNodes;
    private Map<String,Map<SearchStrategy,Integer>> solutionCosts;

    private MetricComparer(){
        if(onlyInstance!=null){
            throw new IllegalArgumentException("Singleton");
        }
        times = new HashMap<>();
        success = new HashMap<>();
        depths = new HashMap<>();
        expandedNodes = new HashMap<>();
        analyzedNodes = new HashMap<>();
        frontierNodes = new HashMap<>();
        solutionCosts = new HashMap<>();
    }

    static MetricComparer getInstance(){
        return onlyInstance;
    }

    void addTime(SearchStrategy searchStrategy, String problemTested, long time){
        addMetric(searchStrategy,problemTested,new MyTime(time),times);
    }

    void addResult(SearchStrategy searchStrategy, String problemTested, boolean succeded){
        addMetric(searchStrategy,problemTested,succeded,success);
    }

    void addDepth(SearchStrategy searchStrategy, String problemTested, int depth){
        addMetric(searchStrategy,problemTested,depth,depths);
    }

    void addExpandedNodes(SearchStrategy searchStrategy, String problemTested, long expandedNodes) {
        addMetric(searchStrategy,problemTested,expandedNodes,this.expandedNodes);
    }

    void addAnalyzedNodes(SearchStrategy searchStrategy, String problemTested, int analyzedNodes) {
        addMetric(searchStrategy,problemTested,analyzedNodes,this.analyzedNodes);
    }

    void addFrontierNodes(SearchStrategy searchStrategy, String problemTested, int frontierNodes) {
        addMetric(searchStrategy,problemTested,frontierNodes,this.frontierNodes);
    }

    void addCost(SearchStrategy searchStrategy, String problemTested, int cost) {
        addMetric(searchStrategy,problemTested,cost,solutionCosts);
    }

    private <T> void addMetric(SearchStrategy searchStrategy, String problemTested, T metric, Map<String,Map<SearchStrategy,T>> map)
    {
        Map<SearchStrategy,T> innerMap = map.get(problemTested);
        if(innerMap==null){
            innerMap= new HashMap<>();
            map.put(problemTested,innerMap);
        }
        innerMap.put(searchStrategy,metric);
    }

    void printResults() {
        printTable(success, "Success");
        printPadding();
        printTable(depths, "Solution Depths");
        printPadding();
        printTable(solutionCosts, "Solution Costs");
        printPadding();
        printTable(expandedNodes, "Expanded Nodes");
        printPadding();
        printTable(analyzedNodes, "Analyzed Nodes");
        printPadding();
        printTable(frontierNodes, "Frontier Nodes");
        printPadding();
        printTable(times, "Times");
        printPadding();
    }

    private <T> void printTable(Map<String, Map<SearchStrategy,T>> map, String tableName) {
        printHeader(tableName);
        final Object[][] table = new String[7][];
        int row_number=0;
        for (Map.Entry<String,Map<SearchStrategy,T>> entry : map.entrySet()) {
            table[row_number] = new String[6];
            table[row_number][0]=entry.getKey();
            for(Map.Entry<SearchStrategy,T> innerEntry: entry.getValue().entrySet())
            {
                switch (innerEntry.getKey())
                {
                    case BFS:
                        table[row_number][1]= innerEntry.getValue().toString();
                        break;
                    case DFS:
                        table[row_number][2]=innerEntry.getValue().toString();
                        break;
                    case IDDFS:
                        table[row_number][3]=innerEntry.getValue().toString();
                        break;
                    case GREEDY:
                        table[row_number][4]=innerEntry.getValue().toString();
                        break;
                    case ASTAR:
                        table[row_number][5]=innerEntry.getValue().toString();
                        break;
                }
            }
            row_number++;
        }
        for (final Object[] row : table) {
            System.out.format("|%30s|%15s|%15s|%15s|%15s|%15s|\n", row);
        }
    }

    private void printHeader(String tableName) {
        System.out.println("+--------------------------------------------------------------------------------------------------------------+");
        Object[] header = new String[] { tableName,"BFS", "DFS", "IDDFS", "Greedy", "A*" };
        System.out.format("|%30s|%15s|%15s|%15s|%15s|%15s|\n", header);
        System.out.println("+--------------------------------------------------------------------------------------------------------------+");
    }

    private void printPadding() {
        System.out.println("\n\n");
    }
}
