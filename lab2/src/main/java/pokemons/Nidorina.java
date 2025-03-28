package pokemons;

import moves.physical.DoubleKick;
import moves.physical.FurySwipes;
import moves.special.SludgeBomb;
import ru.ifmo.se.pokemon.*;

public class Nidorina extends Pokemon {
    public Nidorina(String name, int level) {
        super(name, level);
        setType(Type.POISON);
        setStats(70, 62, 67, 55, 55, 56);
        setMove(new SludgeBomb(), new DoubleKick(), new FurySwipes());
    }
}
