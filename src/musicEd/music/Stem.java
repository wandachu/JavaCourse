package musicEd.music;

import musicEd.reaction.Gesture;
import musicEd.reaction.Reaction;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;


public class Stem extends Duration implements Comparable<Stem> {
    public Staff staff;
    public Head.List heads = new Head.List();
    public boolean isUp = true;
    public Beam beam = null;

    public Stem(Staff staff, Head.List heads, boolean isUp) {
        this.staff = staff;
        this.isUp = isUp;
        this.heads = heads;
        for (Head h : heads) {
            h.unStem();
            h.stem = this;
        }
        staff.sys.stems.add(this);
        setWrongSides();
        
        // Reactions below
        addReaction(new Reaction("E-E") { //increment flag
            public int bid(Gesture gest) {
                int y = gest.vs.yM(), x1 = gest.vs.xL(), x2 = gest.vs.xH();
                int xS = Stem.this.heads.get(0).time.x;
                if (x1 > xS || x2 < xS) {return UC.noBid;}
                int y1 = Stem.this.yLow(), y2 = Stem.this.yHigh();
                if (y < y1 || y > y2) {return UC.noBid;}
                return Math.abs(y - (y1 + y2) / 2) + 55; // 55: bias allows sys E-E to outbid this.
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
                return Math.abs(y - (y1 + y2) / 2) + 55; // 55: bias allows sys W-W to outbid this.
            }

            public void act(Gesture gest) {
                Stem.this.decFlag();
            }
        });
    }

    public void show(Graphics g) {
        if (nFlag >= - 1 && heads.size() > 0) {
            int x = x(), h = staff.H(), yH = yFirstHead(), yB = yBeamEnd();
            g.drawLine(x, yH, x, yB);
            if (nFlag > 0 && beam == null) {
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
        if (beam != null && beam.stems.size() > 1 && beam.first() != this && beam.last() != this) { // test if this is an internal stem
            Stem b = beam.first(), e = beam.last(); // beginning and ending stem
            return Beam.yOfX(x(), b.x(), b.yBeamEnd(), e.x(), e.yBeamEnd());
        }
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
    public int x() {
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
    public int compareTo(Stem s) {return this.x() - s.x();}

    public static Stem getStem(Staff staff, Time time, int y1, int y2, boolean up) { // factory method.
        Head.List heads = new Head.List();
        for (Head h : time.heads) {
            int yH = h.Y();
            if (yH > y1 && yH < y2) {heads.add(h);}
        }
        if (heads.size() == 0) {return null;}
        Beam beam = internalStem(staff.sys, time.x, y1, y2);
        Stem res = new Stem(staff, heads, up);
        if (beam != null) {beam.addStem(res);}
        return res;
    }

    public static Beam internalStem(Sys sys, int x, int y1, int y2) {
        for (Stem s : sys.stems) {
            if (s.beam != null && s.x() < x && s.yLow() < y2 && s.yHigh() > y1) {
                int bX = s.beam.first().x(), bY = s.beam.first().yBeamEnd();
                int eX = s.beam.last().x(), eY = s.beam.last().yBeamEnd();
                if (Beam.verticalLineCrossesSegments(x, y1, y2, bX, bY, eX, eY)) {return s.beam;}
            }
        }
        return null;
    }
    
    //-------------------------------List---------------------------------
    public static class List extends ArrayList<Stem> {
        public void sort() {Collections.sort(this);}
        public Stem.List allIntersectors(int x1, int y1, int x2, int y2) {
            Stem.List res = new Stem.List();
            for (Stem s : this) {
                int x = s.x(), y = Beam.yOfX(x, x1, y1, x2, y2);
                if (x > x1 && x < x2 && y > s.yLow() && y < s.yHigh()) {res.add(s);}
            }
            return res;
        }
    }
}
