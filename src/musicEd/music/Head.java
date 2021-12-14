package musicEd.music;

import musicEd.reaction.Gesture;
import musicEd.reaction.Mass;
import musicEd.reaction.Reaction;

import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;

public class Head extends Mass implements Comparable<Head> {
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

        // Need to change capital method name to lower case.....

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
                    // t.stemHeads(staff, up, y1, y2);
                    Stem.getStem(staff, time, y1, y2, up);
                } else {
                    t.unStemHeads(y1, y2);
                }
            }            
        });

        addReaction(new Reaction("DOT") { // Add dot
            public int bid(Gesture gest) {
                int xH = Head.this.X(), yH = Head.this.Y(), h = Head.this.staff.H(), w = Head.this.W();
                int x = gest.vs.xM(), y = gest.vs.yM();
                if (x < xH || x > xH + 2 * w || y < yH - h || y > yH + h) {return UC.noBid;}
                return Math.abs(xH + w - x) + Math.abs(yH - y);
            }

            public void act(Gesture gest) {
                if (Head.this.stem != null) {
                    Head.this.stem.cycleDot();
                }
            }
        });

        addReaction(new Reaction("S-N") { // Delete head
            public int bid(Gesture gest) {
                int w2 = 2 * W(), h = staff.H(), x = gest.vs.xM();
                int xHead = X(), dX = Math.abs(x - xHead);
                if (dX > w2) {return UC.noBid;}
                int y = gest.vs.yL(), yHead = Y(), dY = Math.abs(y - yHead);
                if (dY > 2 * h) {return UC.noBid;}
                return dX + dY;
            }

            public void act(Gesture gest) {
                Head.this.deleteHead();
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

    // public void delete() { // stub
    //     time.heads.remove(this);
    // }

    public void deleteHead() {
        unStem();
        time.heads.remove(this);
        deleteMass();
    }

    public void unStem() {
        if (stem != null) {
            stem.heads.remove(this); // get off the old stem
            if (stem.heads.size() == 0) {stem.deleteStem();}
            else {stem.setWrongSides();}
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
        g.setColor(Color.BLACK);
        int H = staff.H();
        Glyph glyph = forcedGlyph != null ? forcedGlyph : normalGlyph();
        g.setColor(stem == null ? Color.ORANGE : Color.BLACK); // color heads with no stems to orange
        glyph.showAt(g, H, X(), staff.yLine(line));
        g.setColor(Color.BLACK);
        if (stem != null) {
            int off = UC.restAugDotOffset, sp = UC.augDotSpacing;
            for (int i = 0; i < stem.nDot; i++) {
                g.fillOval(time.x + off + i * sp, Y() - 3 * H / 2, 2 * H / 3, 2 * H / 3);
            }
        }
    }

    public Glyph normalGlyph() {
        if (stem == null) {return Glyph.HEAD_QU;}
        if (stem.nFlag == -1) {return Glyph.HEAD_HALF;}
        if (stem.nFlag == -2) {return Glyph.HEAD_WH;}
        return Glyph.HEAD_QU; // normally this should never be reached. just for compiler
    }

    @Override
    public int compareTo(Head h) {return (staff.iStaff != h.staff.iStaff) ? staff.iStaff - h.staff.iStaff : line - h.line;}

    //------------------------------List------------------------------------
    public static class List extends ArrayList<Head> {}
}
