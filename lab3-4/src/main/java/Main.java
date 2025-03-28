
import exeptions.WrongLocation;
import items.Ball;
import location.Home;
import location.Shop;
import location.Street;
import persons.Korotyshka;
import persons.Kozlik;
import persons.Neznaika;
import persons.Person;
import records.Job;

public class Main {
    public static void main(String[] args) throws WrongLocation {


        Kozlik kozlik = new Kozlik("Козлик", 100);
        Neznaika neznaika = new Neznaika("Незнайка", 100);
        Korotyshka kor = new Korotyshka("Коротышка", 100);
        Home home = new Home();
        Shop shop = new Shop();
        Street street = new Street();

        Ball[] balls = new Ball[5];
        int power = 10;
        for (int i = 0; i < 5; i++) {
            balls[i] = new Ball("Мяч", power);
            power += 5;
        }
        // Козлик поговорил с незнайкой и воодушевил (поменялось настроение)
        kozlik.Communicate(neznaika);
        // Козлик и Незнайка отправляются на улицу
        kozlik.move(street);
        neznaika.move(street);
        //Коротышка разговариает с незнайкой (поменялось настроение)
        kor.Communicate(neznaika);

        // Козлик и Незнайка бросают все мячи по очереди в Коротышку
        for (int i = 0; i < balls.length; i++) {
            System.out.println("Раунд " + (i + 1) + ":");
            kozlik.throwBall(balls[i], kor);
            neznaika.throwBall(balls[i], kor);
            kor.getHit();
        }
        neznaika.Communicate(kozlik);
        kor.runHome();
        home.recovery(kor);
        neznaika.move(shop);
        kozlik.move(shop);

        shop.update(kozlik, Job.EMPLOYED);
        shop.update(neznaika, Job.EMPLOYED);
        neznaika.earnMoney(20);
        kozlik.earnMoney(20);


    }
}

