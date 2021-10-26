package musicEd.reaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import musicEd.music.UC;
import musicEd.graphicsLib.G;

public class Shape implements Serializable {
    public String name;
    public static String filename = UC.pathToShapeDB; // must go before the DB initialization.
    public Prototype.List prototypes = new Prototype.List();
    public static HashMap<String, Shape> DB = loadShapeDB();
    public static Shape DOT = DB.get("DOT"); 
    public static Collection<Shape> SHAPES = DB.values(); //.values() is a function in the map class. Collection backed up by Map.

    public Shape(String name) {
        this.name = name;
    }

    public static HashMap<String, Shape> loadShapeDB() {
        HashMap<String, Shape> res = new HashMap<>();
        res.put("DOT", new Shape("DOT"));
        try {
            System.out.println("Attempting DB Load..." + filename);
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            res = (HashMap<String, Shape>) ois.readObject();
            System.out.println("Successfully Loaded. Found: " + res.keySet());
            ois.close();
        } catch (Exception e) {
            System.out.println("Load DB Failed.");
            System.out.println(e);
        }
        return res;
    }

    public static void saveShapeDB() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(DB);
            System.out.println("Successfully Saved: " + filename);
            oos.close();
        } catch (Exception e) {
            System.out.println("Save DB Failed.");
            System.out.println(e);
        }
    }

    public static Shape recognize(Ink ink) { // can return null.
        if (ink.vs.size.x < UC.dotThreshold && ink.vs.size.y < UC.dotThreshold) {return DOT;}
        Shape bestMatch = null; int bestSoFar = UC.noMatchedDist;
        for (Shape s : SHAPES) {
            int d = s.prototypes.bestDist(ink.norm);
            if (d < bestSoFar) {bestMatch = s; bestSoFar = d;}
        }
        return bestMatch;
    }

    //---------------------------Shape.Prototype--------------------------------
    public static class Prototype extends Ink.Norm {
        int nBlend = 1;

        public void blend(Ink.Norm norm) {
            blend(norm, nBlend++);
        }

        //---------------------------Shape.Prototype.List-------------------------
        public static class List extends ArrayList<Prototype> {
            private static int m = 10, w = 60; // m stands for margin
            private static G.VS showBox = new G.VS(m, m, w, w);
            public static Prototype bestMatch; // set as a side-effect in bestDist
            
            public void show(Graphics g) {
                g.setColor(Color.ORANGE);
                for (int i = 0; i < size(); i++) {
                    Prototype p = get(i);
                    int x = m + i * (m + w);
                    showBox.loc.set(x, m);
                    p.drawAt(g, showBox);
                    g.drawString("" + p.nBlend, x, 20);
                }
            }

            public int bestDist(Ink.Norm norm) {
                bestMatch = null;
                int bestSoFar = UC.noMatchedDist;
                for (Prototype p : this) {
                    int d = p.dist(norm);
                    if (d < bestSoFar) {
                        bestMatch = p;
                        bestSoFar = d;
                    }
                }
                return bestSoFar;
            }
        } //---------------------------Shape.Prototype.List-------------------------
    }//---------------------------Shape.Prototype--------------------------------
}
