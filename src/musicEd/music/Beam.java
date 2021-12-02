package musicEd.music;

import musicEd.reaction.Mass;

public class Beam extends Mass {
    public Stem.List stems = new Stem.List();

    public Beam(Stem first, Stem last) {
        super("NOTE");
        stems.add(first);
        stems.add(last);
    }

    public Stem first() {return stems.get(0);}
    public Stem last() {return stems.get(stems.size() - 1);}
    public void deleteBeam() {
        for (Stem s : stems) {
            s.beam = null;
        }
        deleteMass();
    }
    public void addStem(Stem s) {
        if (s.beam == null) {
            stems.add(s);
            s.beam = this;
            s.nFlag = 1;
            stems.sort(); // will write this later
        }
    }
}
