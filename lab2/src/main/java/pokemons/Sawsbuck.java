package pokemons;

import moves.physical.FeintAttack;
import moves.physical.HornLeech;
import moves.special.ShadowBall;
import moves.status.WorkUp;
import ru.ifmo.se.pokemon.*;

public class Sawsbuck extends Pokemon {
    public Sawsbuck(String name, int level) {
        super(name, level);
        setType(Type.NORMAL, Type.GRASS);
        setStats(80, 100, 70, 60, 70, 95);
        setMove(new WorkUp(), new FeintAttack(), new ShadowBall(), new HornLeech());
    }
}
