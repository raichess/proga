package items;

import persons.Person;

public class Ball extends Item {
    private int power;

    public Ball(String name, int power) {
        super(name);
        this.power = power;
    }

    public int getPower() {
        return power;
    }
    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public void use(Person person) {
        System.out.println(person.getName() + " подобрал мяч " );
    }
}
