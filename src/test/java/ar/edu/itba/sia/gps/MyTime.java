package ar.edu.itba.sia.gps;

public class MyTime {
    private long nanoSeconds;

    public MyTime(long nanoSeconds){
        this.nanoSeconds=nanoSeconds;
    }

    @Override
    public String toString(){

        long tempMiliSec = nanoSeconds/(1000*1000*1000/1000);
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
