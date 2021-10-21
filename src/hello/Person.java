package hello;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Person implements Named {
    public String first;
    public String last;
    public int age;
    public boolean isCougar = false;
    public static ArrayList<Person> all = new ArrayList<>();
    public static Map<String, ArrayList<Person>> byName = new HashMap<>();

    public Person(String first, String last, int age) {
        this.first = first;
        this.last = last;
        this.age = age;
        addToByName(this);        
    }

    private void addToByName(Person p) {
        all.add(p); //we don't copy, we just add the reference (pointer)
        ArrayList<Person> list = byName.get(p.first);
        if (list == null) {
            list = new ArrayList<>();
            byName.put(p.first, list);
        }
        list.add(p);
    }

    public void meets(Person p) {
        System.out.println(first + " meets " + p.first);
    }

    @Override
    public String toString() {
        return last + ", " + first + " who is " + age + " years old.";
    }

    @Override
    public String name() {
        String res = first + " " + last + (isCougar ? ", a known Cougar," : "");
        // if (isCougar) {
        //     res += ", a known Cougar";
        // }
        return res;
    }

    public void snogs(Person p) {
        System.out.println("Holy Smoke! " + this.name() + " just snogged " + p.name() + ".");
        if (!this.isCougar && this.age > p.age + 20) {
            isCougar = true;
            System.out.println("Cougar detected!");
        }
    }
}