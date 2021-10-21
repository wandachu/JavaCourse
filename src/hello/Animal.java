package hello;
public class Animal implements Named {
    public String name, type, sound;
    
    public Animal(String name, String type, String sound) {
        this.name = name;
        this.type = type;
        this.sound = sound;
    }

    @Override
    public String name() {
        return name + " the " + type;
    }
}
