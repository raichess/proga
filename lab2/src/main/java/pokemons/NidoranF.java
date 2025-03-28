package pokemons;

import moves.physical.DoubleKick;
import moves.special.SludgeBomb;
import ru.ifmo.se.pokemon.*;

public class NidoranF extends Pokemon {
    public NidoranF(String name, int level) {
        super(name, level);
        setType(Type.POISON);
        setStats(55, 47, 52, 40, 40, 41);
        setMove(new SludgeBomb(), new DoubleKick());
    }
}
