package ar.edu.itba.sia.gps;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        printHeader();
        final Object[][] table = new String[7][];
        int row_number=0;
        for (Map.Entry<String,Map<SearchStrategy,Long>> entry : times.entrySet()) {
            table[row_number] = new String[6];
            table[row_number][0]=entry.getKey();
            for(Map.Entry<SearchStrategy,Long> innerEntry: entry.getValue().entrySet())
            {
                switch (innerEntry.getKey())
                {
                    case BFS:
                        table[row_number][1]= getReadableTime(innerEntry.getValue());
                        break;
                    case DFS:
                        table[row_number][2]=getReadableTime(innerEntry.getValue());
                        break;
                    case IDDFS:
                        table[row_number][3]=getReadableTime(innerEntry.getValue());
                        break;
                    case GREEDY:
                        table[row_number][4]=getReadableTime(innerEntry.getValue());
                        break;
                    case ASTAR:
                        table[row_number][5]=getReadableTime(innerEntry.getValue());
                        break;
                }
            }
            row_number++;
        }
        for (final Object[] row : table) {
            System.out.format("|%30s|%15s|%15s|%15s|%15s|%15s|\n", row);
        }
    }

    private void printHeader() {
        System.out.println("+---------------------------------------------------------------------------------------------------------------+");
        Object[] header = new String[] { "","BFS", "DFS", "IDDFS", "Greedy", "A*" };
        System.out.format("|%30s|%15s|%15s|%15s|%15s|%15s|\n", header);
        System.out.println("+---------------------------------------------------------------------------------------------------------------+");
    }

    private String getReadableTime(Long nanos){

        long tempMiliSec = nanos/(1000*1000*1000/1000);
        long miliSec = tempMiliSec % 1000;
        long sec = (tempMiliSec / 1000) % 60;
        long min = tempMiliSec /(1000 * 60);
        if(sec==0 && min==0){
            return String.format("%dms",miliSec);
        }
        if(min==0){
            return String.format("%ds:%dms",sec,miliSec);
        }
        return String.format("%dm:%ds:%dms", min,sec,miliSec);
    }
}
