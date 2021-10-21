package musicEd.sandbox;

import musicEd.graphicsLib.G;
import musicEd.graphicsLib.Window;
import musicEd.music.UC;
import musicEd.reaction.*;
import java.awt.*;
import java.awt.event.*;

public class PaintInk extends Window {
    public static Ink.List inkList = new Ink.List();

    // Test
    // static {inkList.add(new Ink());}

    public PaintInk() {super("PaintInk!", UC.windowWidth, UC.windowHeight);}

    @Override
    public void paintComponent(Graphics g) {
        G.fillBackground(g);
        g.setColor(Color.BLUE);
        // g.fillRect(100, 100, 100, 100);
        inkList.show(g);
        g.setColor(Color.RED);
        Ink.BUFFER.show(g);
        g.drawString("count: " + Ink.BUFFER.n, 100, 100);
    }

    public void mousePressed(MouseEvent me) {
        Ink.BUFFER.dn(me.getX(), me.getY());
        repaint();
    }

    public void mouseDragged(MouseEvent me) {
        Ink.BUFFER.drag(me.getX(), me.getY());
        repaint();
    }

    public void mouseReleased(MouseEvent me) {
        inkList.add(new Ink());
        repaint();
    }

}
