package musicEd.music;

import musicEd.reaction.Gesture;
import musicEd.reaction.Mass;
import musicEd.reaction.Reaction;
import musicEd.graphicsLib.*;

import java.awt.Graphics;
import java.awt.Color;

public class Staff extends Mass {
    public Sys sys;
    public int iStaff; // index in the system
    public Staff.Fmt fmt;

    public Staff(Sys sys, int iStaff) {
        super("BACK");
        this.sys = sys;
        this.iStaff = iStaff;
        fmt = sys.page.sysFmt.get(iStaff);

        // Reactions below
        addReaction(new Reaction("S-S") { // Create bar lines.
            public int bid(Gesture gest) {
                int x = gest.vs.xM(), y1 = gest.vs.yL(), y2 = gest.vs.yH();
                G.LoHi m = Page.PAGE.xMargin;
                if (x < m.lo || x > m.hi) return UC.noBid;
                int d = Math.abs(y1 - Staff.this.yTop()) + Math.abs(y2 - Staff.this.yBot());
                return (d < 30) ? d : UC.noBid;
            }

            public void act(Gesture gest) {
                new Bar(Staff.this.sys, gest.vs.xM());
            } 

        });
    }

    public int yTop() {return sys.yTop() + sysOff();}
    public int yBot() {return yTop() + fmt.height();}
    public int sysOff() {return sys.page.sysFmt.staffOffsets.get(iStaff);}

    //-----------------------------Format----------------------------
    public static class Fmt{
        public int nLines = 5, H = 8; // H is half the spacing between two lines.

        public int height() {return 2 * H * (nLines - 1);}

        public void showAt(Graphics g, int y, Page page) {
            g.setColor(Color.GRAY);
            int x1 = page.xMargin.lo, x2 = page.xMargin.hi, h = 2 * H;
            for (int i = 0; i < nLines; i++) {
                g.drawLine(x1, y, x2, y);
                y += h;
            }
        }
    }//-----------------------------Format----------------------------
}
