package musicEd.reaction;

import java.util.ArrayList;
import java.awt.*;

import musicEd.music.UC;
import musicEd.graphicsLib.G;

public class Shape {
    public String name;
    public Prototype.List prototypes = new Prototype.List();

    public Shape(String name) {
        this.name = name;
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
