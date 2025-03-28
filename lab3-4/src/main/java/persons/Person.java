package persons;

import enums.Emotion;
import exeptions.MaxMoneyException;
import interfaces.Emotional;
import interfaces.Interactable;
import interfaces.Movable;
import items.Item;
import items.Money;
import location.Home;
import location.Location;
import records.Job;

public abstract class Person implements Emotional, Interactable, Movable {
    private String name;
    private Emotion currentEmotion;
    private Money currentMoney;
    private Location currentLocation;
    private Job currentJob;
    private int hp;

    public Person(String name, int hp) {
        this.name = name;
        this.hp = hp;
        this.currentEmotion = Emotion.NETUTRAL;
        this.currentMoney = new Money("Деньги", 0);
        this.currentLocation = new Home();
        this.currentJob = Job.EMPLOYED;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public void setCurrentEmotion(Emotion currentEmotion) {
        this.currentEmotion = currentEmotion;
    }

    public Emotion getCurrentEmotion() {
        return currentEmotion;
    }

    public void setCurrentMoney(Money currentMoney) {
        this.currentMoney = currentMoney;
    }

    public Money getCurrentMoney() {
        return currentMoney;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setJob(Job currentJob)  {
        this.currentJob = currentJob;
    }

    public Job getJob() {
        return currentJob;
    }

    public abstract void Communicate(Person person);

    public void changeEmotion(Emotion emotion) {
        this.currentEmotion = emotion;
    }

    public void interact(Item item) {
        item.use(this);
    }

    public void move(Location location) {
        this.currentLocation = location;
        System.out.println(this.getName() + " переместился в " + this.currentLocation);
    }

    public void minusHP(int amount) {
        this.hp -= amount;
        if (this.hp <= 0) this.hp = 0;
    }
    public void earnMoney(int amount) {
        if (this.currentMoney.getAmount() >= 100) {
            throw new MaxMoneyException(this.getName() + " не может заработать, потому что у него уже есть 100 рублей");
        }
        while (this.currentMoney.getAmount() < 100) {
            this.currentMoney.addAmount(amount);
            this.interact(this.currentMoney);
        }
        System.out.println(this.getName() + " достиг 100 рублей");
        this.changeEmotion(Emotion.HAPPY);
        System.out.println(this.getName() + " " + this.getCurrentEmotion());
    }


    }

