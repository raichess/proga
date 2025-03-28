package pokemons;

import moves.physical.FeintAttack;
import moves.special.ShadowBall;
import moves.status.WorkUp;
import ru.ifmo.se.pokemon.*;

public class Deerling extends Pokemon {
    public Deerling(String name, int level) {
        super(name, level);
        setType(Type.NORMAL, Type.GRASS);
        setStats(230, 60, 50, 40, 50, 75);
        setMove(new WorkUp(), new FeintAttack(), new ShadowBall());
    }



}
