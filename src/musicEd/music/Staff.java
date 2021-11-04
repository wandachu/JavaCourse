package musicEd.music;

import musicEd.reaction.Mass;

public class Staff extends Mass {
    public Sys sys;
    public int iStaff; // index in the system
    public Staff.Fmt fmt;

    public Staff(Sys sys, int iStaff) {
        super("BACK");
        this.sys = sys;
        this.iStaff = iStaff;
        fmt = sys.page.sysFmt.get(iStaff);
    }

    //-----------------------------Format----------------------------
    public static class Fmt{
        public int nLines = 5, H = 8;
    }//-----------------------------Format---------------------------- 
}
