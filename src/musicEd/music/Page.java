package musicEd.music;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

import musicEd.graphicsLib.G;
import musicEd.reaction.Mass;

public class Page extends Mass {
    public static Page PAGE; // singleton

    public G.LoHi xMargin, yMargin;
    public int sysGap;
    public Sys.Fmt sysFmt = new Sys.Fmt();
    public ArrayList<Sys> sysList = new ArrayList<>();

    public Page(int y) {
        super("BACK");
        PAGE = this;
        int MM = 50;
        xMargin = new G.LoHi(MM, UC.windowWidth - MM);
        yMargin = new G.LoHi(y, UC.windowHeight - y);
    }

    @Override
    public void show(Graphics g) {
        g.setColor(Color.RED);
        g.drawLine(0, yMargin.lo, 30, yMargin.lo);
    }
}
