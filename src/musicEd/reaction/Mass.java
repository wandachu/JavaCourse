package musicEd.reaction;

import musicEd.music.I;

public abstract class Mass extends Reaction.List implements I.Show {
    public Layer layer;

    public Mass(String layerName) {
        layer = Layer.byName.get(layerName);
        if (layer != null) {
            layer.add(this);
        } else {
            System.out.println("Bad layerName!" + layerName);
        }
    }

    public void delete() {clearAll(); layer.remove(this);}

    // Explain this later.
    public boolean equals(Object o) {return this == o;}
}
