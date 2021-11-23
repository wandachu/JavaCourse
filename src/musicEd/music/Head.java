package musicEd.music;

import musicEd.reaction.Mass;

import java.awt.Graphics;

public class Head extends Mass {
    public Staff staff;
    public int line;  // line is the y coordinate regarding which line (more useful than just y)
    public Time time;

    public Head(Staff staff, int x, int y) {
        super("NOTE");
        this.staff = staff;
        time = staff.sys.getTime(x);
        line = staff.lineOfY(y); 
    }

    public void show(Graphics g) {
        int H = staff.H();
        Glyph.HEAD_QU.showAt(g, H, time.x, staff.yLine(line));
    }
}
