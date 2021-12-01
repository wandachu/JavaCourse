package musicEd.music;

import musicEd.reaction.Gesture;
import musicEd.reaction.Mass;
import musicEd.reaction.Reaction;

import java.awt.Graphics;
import java.util.ArrayList;

public class Head extends Mass {
    public Staff staff;
    public int line;  // line is the y coordinate regarding which line (more useful than just y)
    public Time time;
    public Stem stem = null;
    public boolean wrongSide = false;
    public Glyph forcedGlyph = null;

    public Head(Staff staff, int x, int y) {
        super("NOTE");
        this.staff = staff;
        time = staff.sys.getTime(x);
        line = staff.lineOfY(y);
        time.heads.add(this);

        // add reactions below
        addReaction(new Reaction("S-S") { // stem or unstem
            public int bid(Gesture gest) {
                int x = gest.vs.xM(), y1 = gest.vs.yL(), y2 = gest.vs.yH();
                int W = Head.this.W();
                if (y1 > y || y2 < y) {return UC.noBid;}
                int hL = Head.this.time.x, hR = hL + W;
                if (x < hL - W || x > hR + W) {return UC.noBid;}
                if (x < hL + W / 2) {return hL - x;}
                if (x > hR - W / 2) {return x - hR;}
                return UC.noBid;
            }

            public void act(Gesture gest) {
                int x = gest.vs.xM(), y1 = gest.vs.yL(), y2 = gest.vs.yH();
                int W = Head.this.W();
                Staff staff = Head.this.staff;
                Time t = Head.this.time;
                boolean up = (x > t.x + W / 2);
                if (Head.this.stem == null) {
                    t.stemHeads(staff, up, y1, y2);
                } else {
                    t.unStemHeads(y1, y2);
                }
            }
            
        });
    }

    public int W() {return 24 * staff.H() / 10;}
    public int Y() {return staff.yLine(line);}
    public int X() {
        int res = time.x;
        if (wrongSide) {
            res += (stem != null && stem.isUp) ? W() : -W();
        }
        return res;
    }

    public void delete() { // stub
        time.heads.remove(this);
    }

    public void unStem() {
        if (stem != null) {
            stem.heads.remove(this); // get off the old stem
            if (stem.heads.size() == 0) {stem.deleteStem();}
            stem = null;
            wrongSide = false;
        }
    }

    public void joinStem(Stem s) {
        if (stem != null) {unStem();}
        s.heads.add(this);
        stem = s;
    }

    public void show(Graphics g) {
        int H = staff.H();
        Glyph glyph = forcedGlyph != null ? forcedGlyph : normalGlyph();
        glyph.showAt(g, H, time.x, staff.yLine(line));
    }

    public Glyph normalGlyph() {
        return Glyph.HEAD_QU; // will fix this later.
    }

    //------------------------------List------------------------------------
    public static class List extends ArrayList<Head> {}
}
