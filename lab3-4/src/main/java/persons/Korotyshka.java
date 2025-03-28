package persons;

import enums.Emotion;
import items.Ball;
import location.Home;
import location.Street;

public class Korotyshka extends Person {
    public Korotyshka(String name, int hp) {
        super(name, hp);
    }
    @Override
    public void Communicate(Person person) {
        this.setCurrentLocation(new Street());
        this.changeEmotion(Emotion.ENVY);
        person.changeEmotion(Emotion.ENVY);
        System.out.println(this.getName() + " " + this.getCurrentEmotion());
        System.out.println(person.getName() + " " + person.getCurrentEmotion());
    }
    public void getHit() {
        if (this.getHp() < 70) {
            this.setCurrentEmotion(Emotion.SAD);
            System.out.println(this.getName() + " " + this.getCurrentEmotion());
        }
        if (this.getHp() < 40) {
            this.setCurrentEmotion(Emotion.ANGRY);
            System.out.println(this.getName() + " "  + this.getCurrentEmotion());
        }
    }
    public void runHome() {
        this.move(new Home());
    }

}
