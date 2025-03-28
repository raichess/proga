package items;

import persons.Person;

public class Money extends Item {
    private int amount;
    public Money(String name, int amount) {
        super(name);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void addAmount(int value) {
        amount += value;

    }
    @Override
    public void use(Person person) {
        System.out.println(person.getName() + " заработал " + amount);
    }

}
