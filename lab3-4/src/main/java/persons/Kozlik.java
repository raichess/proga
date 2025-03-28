package persons;

import enums.Emotion;
import items.Ball;

public class Kozlik extends Person {
    public Kozlik(String name, int hp) {
        super(name, hp);
    }

    @Override
    public void Communicate(Person person) {
        person.changeEmotion(Emotion.HOPE);
        this.changeEmotion(Emotion.HOPE);
        System.out.println(this.getName() + " " + this.getCurrentEmotion());
        System.out.println(person.getName() + " " + person.getCurrentEmotion());
    }

    public void throwBall(Ball ball, Person person) {
        this.interact(ball);
        person.minusHP(ball.getPower());
        System.out.println(this.getName() + " бросил мяч");
        System.out.println(person.getName() + " здоровье " + person.getHp());
    }

}