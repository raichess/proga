package pokemons;

import moves.physical.DoubleKick;
import moves.physical.FurySwipes;
import moves.physical.RockTomb;
import moves.special.SludgeBomb;
import ru.ifmo.se.pokemon.*;

public class Nidoqueen extends Pokemon {
    public Nidoqueen(String name, int level) {
        super(name, level);
        setType(Type.POISON, Type.GROUND);
        setStats(90, 92, 87, 75, 85, 76);
        setMove(new SludgeBomb(), new DoubleKick(), new FurySwipes(), new RockTomb());
    }
}
