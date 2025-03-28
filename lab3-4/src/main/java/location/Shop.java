package location;

import exeptions.WrongLocation;
import persons.Person;
import records.Job;

public class Shop extends Location {
    public Shop() {
        super("Магазин");
    }
    public void update(Person person, Job job) throws WrongLocation {
        if (!person.getCurrentLocation().equals(this)) {
            throw new WrongLocation("персонаж не в магазине");
        }
        person.setJob(job);
        System.out.println(person.getName() + " устроился на работу ");
    }
}
