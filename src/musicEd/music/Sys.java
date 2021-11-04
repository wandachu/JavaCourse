package musicEd.music;

import java.util.ArrayList;

import musicEd.reaction.Mass;

public class Sys extends Mass {
    public ArrayList<Staff> staffs = new ArrayList<>();
    public Page page;
    public int iSys;

    public Sys(Page page, int iSys) {
        super("BACK");
        this.page = page;
        this.iSys = iSys;
    }

    public void addNewStaff(int iStaff) {
        staffs.add(new Staff(this, iStaff));
    }

    //-----------------------------Format----------------------------
    public static class Fmt extends ArrayList<Staff.Fmt> {
        public ArrayList<Integer> staffOffsets = new ArrayList<>();

        public void addNew(int yOff) {
            add(new Staff.Fmt());
            staffOffsets.add(yOff);
        }        
    }//-----------------------------Format----------------------------    
}
