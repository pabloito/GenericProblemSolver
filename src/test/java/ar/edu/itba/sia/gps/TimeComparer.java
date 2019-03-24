package ar.edu.itba.sia.gps;

import java.util.HashMap;
import java.util.Map;

class TimeComparer {

    private static final TimeComparer onlyInstance = new TimeComparer();

    Map<String,Map<SearchStrategy,Long>> times;

    private TimeComparer(){
        if(onlyInstance!=null){
            throw new IllegalArgumentException("Singleton");
        }
        times = new HashMap<>();
    }

    static TimeComparer getInstance(){
        return onlyInstance;
    }

    void addTime(SearchStrategy searchStrategy, String problemTested, long time){
        Map<SearchStrategy,Long> innerMap = times.get(problemTested);
        if(innerMap==null){
            innerMap= new HashMap<>();
            times.put(problemTested,innerMap);
        }
        innerMap.put(searchStrategy,time);
    }

    void printResults() {
        final Object[][] table = new String[8][];
        table[0] = new String[] { "","BFS", "DFS", "IDDFS", "Greedy", "A*" };
        int row_number=1;
        for (Map.Entry<String,Map<SearchStrategy,Long>> entry : times.entrySet()) {
            table[row_number] = new String[6];
            table[row_number][0]=entry.getKey();
            for(Map.Entry<SearchStrategy,Long> innerEntry: entry.getValue().entrySet())
            {
                switch (innerEntry.getKey())
                {
                    case BFS:
                        table[row_number][1]=innerEntry.getValue().toString();
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
            System.out.format("%30s%15s%15s%15s%15s%15s\n", row);
        }
    }
}
