package ar.edu.itba.sia.gps.eightpuzzle;

import ar.edu.itba.sia.gps.api.State;

import java.util.Arrays;

public class E8State implements State{
    private Pair array[];
    Pair blank;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof E8State)) return false;

        E8State e8State = (E8State) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (array == null){
            if (e8State.array != null)
                return false;
        } else {
            for (int i = 0;i < 8; i++){
                if(!array[i].equals(e8State.array[i]))
                    return false;
            }
        }
        return blank.equals(e8State.blank);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(array);
        result = 31 * result + blank.hashCode();
        return result;
    }
    /*@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        E8State other = (E8State) obj;
        if (blank == null) {
            if (other.blank != null)
                return false;
            else if (array == null)
                if (other.array != null)
                    return false;
        } else if (array == null){
            if (other.array != null)
                return false;
        } else {
            for (int i = 0;i < 8; i++){
                if(!array[i].equals(other.array[i]))
                    return false;
            }
        }
        return true;
    }*/

    public Pair getBlank() {
        return blank;
    }

    public Pair[] getArray() {
        return array;
    }

    public E8State(Pair blank, Pair[] array){
        this.blank = blank;
        this.array = array;
    }
    private static StringBuilder stringBuilder = new StringBuilder();

    @Override
    public String toString() {
        int strOrder[] = new int[9];
        for (int i = 0; i < 8; i++) {
            strOrder[array[i].getX()+array[i].getY()*3] = i+1;
        }
        strOrder[blank.getX()+blank.getY()*3] = -1;
        int a;
        stringBuilder.delete(0,stringBuilder.capacity());
        for (int i = 0; i < 9; i++) {
            a = strOrder[i];
            if(a > 0)
                stringBuilder.append(a).append(' ');
            else
                stringBuilder.append('B').append(' ');
            if(i%3 == 2)
                stringBuilder.append('\n');
        }
        return stringBuilder.append('\n').toString();
    }
	
	@Override
	public String getRepresentation() {
		return toString();
	}
}
