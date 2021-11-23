package musicEd.music;

import java.awt.Graphics;

import musicEd.reaction.Gesture;
import musicEd.reaction.Reaction;

public class Rest extends Duration {
    public Staff staff;
    public Time time;
    public int line = 4; // default is the center line.

    public Rest(Staff staff, Time time) {
        this.staff = staff;
        this.time = time;

        // Reactions below
        addReaction(new Reaction("E-E") { // add flag to rest.
            public int bid(Gesture gest) {
                int y = gest.vs.yM(), x1 = gest.vs.xL(), x2 = gest.vs.xH(), x = Rest.this.time.x;
                if (x1 > x || x2 < x) {return UC.noBid;}
                return Math.abs(y - Rest.this.staff.yLine(4));
            }

            public void act(Gesture gest) {
                Rest.this.incFlag();
            }
        });

        // Homework: write the S-S reaction to reduce the Flg.
        // addReaction(new Reaction("S-S") { // remove flag to rest.
        //     public int bid(Gesture gest) {
        //         int y = gest.vs.yM(), x1 = gest.vs.xL(), x2 = gest.vs.xH(), x = Rest.this.time.x;
        //         if (x1 > x || x2 < x) {return UC.noBid;}
        //         return Math.abs(y - Rest.this.staff.yLine(4));
        //     }

        //     public void act(Gesture gest) {
        //         Rest.this.decFlag();
        //     }
        // });
    }

    @Override
    public void show(Graphics g) {
        int H = staff.H(), y = staff.yLine(line);
        Glyph glyph = Glyph.REST_W;
        if (nFlag == -1) {
            glyph = Glyph.REST_H;
        } else if (nFlag == 0) {
            glyph = Glyph.REST_Q;
        } else if (nFlag == 1) {
            glyph = Glyph.REST_1F;
        } else if (nFlag == 2) {
            glyph = Glyph.REST_2F;
        } else if (nFlag == 3) {
            glyph = Glyph.REST_3F;
        } else if (nFlag == 4) {
            glyph = Glyph.REST_4F;
        }
        glyph.showAt(g, H, time.x, y);
    }


}
