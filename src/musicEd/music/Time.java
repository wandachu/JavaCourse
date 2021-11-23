package musicEd.music;

import java.util.ArrayList;

public class Time {
    public int x;

    private Time(int x, List tl) {this.x = x; tl.add(this);} // singleton

    //-------------------------------List--------------------------------
    public static class List extends ArrayList<Time> {
        public Sys sys;

        public List(Sys sys) {this.sys = sys;}
        
        public Time getTime(int x) {
            Time res = null;
            int dist = UC.snapTime;
            for (Time t : this) {
                int d = Math.abs(x - t.x);
                if (d < dist) {dist = d; res = t;} // found a closer dist. Only found a time less than snapTime.
            }
            return (res != null) ? res : new Time(x, this);
        }
    }
}
