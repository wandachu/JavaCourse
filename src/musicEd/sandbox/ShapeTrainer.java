package musicEd.sandbox;

import java.awt.*;
import java.awt.event.*;

import musicEd.graphicsLib.Window;
import musicEd.graphicsLib.G;
import musicEd.music.UC;

public class ShapeTrainer extends Window {
    public static String UNKNOWN = "This name is currently unknown";
    public static String ILLEGAL = "This is not a legal shape name";
    public static String KNOWN = "This is a known shape";
    public static String currName = "";
    public static String currState = ILLEGAL;

    public ShapeTrainer() {
        super("ShapeTrainer!", UC.windowWidth, UC.windowWidth);
    }

    public void setState() {
        currState = (currName.equals("") || currName.equals("DOT"))? ILLEGAL : UNKNOWN;
    }

    public void paintComponent(Graphics g) {
        G.fillBackground(g);
        g.setColor(Color.BLACK);
        g.drawString(currName, 600, 30);
        g.drawString(currState, 700, 30);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        System.out.println("typed: " + c);
        currName = (c == ' ' || c == 0x0D || c == 0x0A)? "" : currName + c; // space bar means to clear those. The other are the different return key for Unix and Windows
        setState();
        repaint();
    }
}
