package location;

import persons.Person;

public class Home extends Location{

    public Home() {
        super("Дом");
    }
    public void recovery(Person person) {
        person.setHp(100);
        System.out.println(person.getName() + " здоровье " + person.getHp());
    }
}
