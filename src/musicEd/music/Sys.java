package musicEd.music;

import java.util.ArrayList;
import java.awt.Graphics;

import musicEd.reaction.Mass;

public class Sys extends Mass {
    public ArrayList<Staff> staffs = new ArrayList<>();
    public Stem.List stems = new Stem.List();
    public Page page;
    public int iSys;
    public Time.List times;

    public Sys(Page page, int iSys) {
        super("BACK");
        this.page = page;
        this.iSys = iSys;
        times = new Time.List(this);
    }

    public int yTop() {return page.sysTop(iSys);}
    public int yBot() {return staffs.get(staffs.size() - 1).yBot();}

    public void addNewStaff(int iStaff) {staffs.add(new Staff(this, iStaff));}
    public Time getTime(int x) {return times.getTime(x);}

    //-----------------------------Fmt----------------------------
    public static class Fmt extends ArrayList<Staff.Fmt> {
        public int maxH = 8; // Technically this should be calculated when you add new staff.
        public ArrayList<Integer> staffOffsets = new ArrayList<>();

        public void addNew(int yOff) {
            add(new Staff.Fmt());
            staffOffsets.add(yOff);
        }        

        public int height() {
            int last = size() - 1; // index of the last item in the sys
            return staffOffsets.get(last) + get(last).height(); // height of the last item plus its height
        }

        public void showAt(Graphics g, int y, Page page) {
            for (int i = 0; i < size(); i++) {
                get(i).showAt(g, y + staffOffsets.get(i), page);
            }
            int x1 = page.xMargin.lo, x2 = page.xMargin.hi, y2 = y + height();
            g.drawLine(x1, y, x1, y2);
            g.drawLine(x2, y, x2, y2);
        }
    }//-----------------------------Fmt----------------------------    
}
