
import pokemons.*;
import ru.ifmo.se.pokemon.*;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        Pokemon p1 = new Drampa("Дракарис", 2);
        Pokemon p2 = new Deerling("Рандом", 3);
        Pokemon p3 = new Nidoqueen("Бараш", 1);
        b.addAlly(p1);
        b.addAlly(p2);
        b.addAlly(p3);
        Pokemon p4 = new NidoranF("Чел", 1);
        Pokemon p5 = new Nidorina("ДжонСноу", 2);
        Pokemon p6 = new Sawsbuck("Мими", 1);
        b.addFoe(p4);
        b.addFoe(p5);
        b.addFoe(p6);
        b.go();
    }
}
