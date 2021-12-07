package musicEd.music;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import musicEd.reaction.Gesture;
import musicEd.reaction.Reaction;

public class Stem extends Duration implements Comparable<Stem> {
    public Staff staff;
    public Head.List heads = new Head.List();
    public boolean isUp = true;
    public Beam beam = null;

    public Stem(Staff staff, boolean isUp) {
        this.staff = staff;
        this.isUp = isUp;
        
        // Reactions below
        addReaction(new Reaction("E-E") { //increment flag
            public int bid(Gesture gest) {
                int y = gest.vs.yM(), x1 = gest.vs.xL(), x2 = gest.vs.xH();
                int xS = Stem.this.heads.get(0).time.x;
                if (x1 > xS || x2 < xS) {return UC.noBid;}
                int y1 = Stem.this.yLow(), y2 = Stem.this.yHigh();
                if (y < y1 || y > y2) {return UC.noBid;}
                return Math.abs(y - (y1 + y2) / 2);
            }

            public void act(Gesture gest) {
                Stem.this.incFlag();
            }
        });

        addReaction(new Reaction("W-W") { //decrement flag
            public int bid(Gesture gest) { // better refactor this since we copy it from above.
                int y = gest.vs.yM(), x1 = gest.vs.xL(), x2 = gest.vs.xH();
                int xS = Stem.this.heads.get(0).time.x;
                if (x1 > xS || x2 < xS) {return UC.noBid;}
                int y1 = Stem.this.yLow(), y2 = Stem.this.yHigh();
                if (y < y1 || y > y2) {return UC.noBid;}
                return Math.abs(y - (y1 + y2) / 2);
            }

            public void act(Gesture gest) {
                Stem.this.decFlag();
            }
        });
    }

    public void show(Graphics g) {
        if (nFlag >= - 1 && heads.size() > 0) {
            int x = X(), h = staff.H(), yH = yFirstHead(), yB = yBeamEnd();
            g.drawLine(x, yH, x, yB);
            if (nFlag > 0) {
                (isUp ? Glyph.dnFlags : Glyph.upFlags)[nFlag - 1].showAt(g, h, x, yB); //This is correct: if isUp, flags should be pointing down.
            }
        }
    }

    public Head firstHead() {return heads.get(isUp ? heads.size() - 1 : 0);}
    public Head lastHead() {return heads.get(isUp ? 0 : heads.size() - 1);}
    public int yFirstHead() {
        Head h = firstHead();
        return h.staff.yLine(h.line);
    }
    public int yBeamEnd() {
        Head h = lastHead();
        int line = h.line;
        line += isUp ? -7 : 7; // Default length of 7
        int flagInc = nFlag > 2 ? 2 * (nFlag - 2) : 0;
        line += isUp ? -flagInc : flagInc;
        if ((isUp && line > 4) || (!isUp && line < 4)) {
            line = 4;
        }
        return h.staff.yLine(line);
    }
    public int yLow() {return isUp ? yBeamEnd() : yFirstHead();}
    public int yHigh() {return isUp ? yFirstHead() : yBeamEnd();}
    public int X() {
        Head h = firstHead();
        return h.time.x + (isUp ? h.W() : 0);
    }

    public void deleteStem() {this.staff.sys.stems.remove(this); deleteMass();}
    public void setWrongSides() {
        Collections.sort(heads);
        int i, last, next;
        if (isUp) {
            i = heads.size() - 1;
            last = 0;
            next = -1;
        } else {
            i = 0;
            last = heads.size() - 1;
            next = 1;
        }
        Head ph = heads.get(i); // take care of the first one
        ph.wrongSide = false;
        while (i != last) {  // take care of the second from the last
            i += next;
            Head nh = heads.get(i);
            nh.wrongSide = (ph.staff == nh.staff && Math.abs(nh.line - ph.line) <= 1 && !ph.wrongSide);
            ph = nh;
        }
    }

    @Override
    public int compareTo(Stem s) {return this.X() - s.X();}
    
    //-------------------------------List---------------------------------
    public static class List extends ArrayList<Stem> {
        public void sort() {Collections.sort(this);}
    }
}
