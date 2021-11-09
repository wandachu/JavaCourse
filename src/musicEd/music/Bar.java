package musicEd.music;

import musicEd.reaction.Mass;

import java.awt.Graphics;
import java.awt.Color;

public class Bar extends Mass {
    public Sys sys;
    public int x;
    public int barType = 0; // will have multiple types (double lines, repeating lines...)

    /*
    barType = 
    0 - normal thin line. 
    1 - double thin line.
    2 - thin, fat line, i.e. fine line.
    4-7 - fat, thin, dots, i.e. repeat to the right.
    8-11 - dots, thin, fat, i.e. repeat to the left.
    12 - dots, thin, fat, thin, dots, i.e. repeat both sides.
    */


    public Bar(Sys sys, int x) {
        super("BACK");
        this.sys = sys;
        this.x = x;
    }

    @Override
    public void show(Graphics g) {
        g.setColor(Color.RED);
        int y = sys.yTop(), N = sys.staffs.size();
        for (int i = 0; i < N; i++) {
            Staff staff = sys.staffs.get(i);
            g.drawLine(x, staff.yTop(), x, staff.yBot());
        }
    }
    
}
