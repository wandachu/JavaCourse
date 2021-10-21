package hello;
public class Hello {
    public static void main(String[] args) {
        Person p = new Person("Bob", "Smith", 15);
        System.out.println(p.name());

        // Person z = new Person("Bob", "Lee", 30);

        Person s = new Person("Sally", "Jones", 48);

        // Person[] b = {p, s, new Person("John", "Doe", 30)};

        for (Person person: Person.all) {
            System.out.println("Person: " + person.first);
        }

        System.out.println(Person.byName.get("Sally")); // If you get Bob, there are two Bob, but the second overrides the map

        Animal a = new Animal("Smoky", "Bear", "Howl");
        System.out.println(a.name());

        // Named x = a;
        // System.out.println(x.name());
        s.snogs(p);
        s.snogs(p);
    }
}
