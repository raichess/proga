package pokemons;

import moves.physical.AquaTail;
import moves.physical.Psychic;
import moves.special.WaterGun;
import moves.status.DoubleTeam;
import ru.ifmo.se.pokemon.*;

public class Drampa extends Pokemon {
    public Drampa(String name, int level) {
        super(name, level);
        setType(Type.NORMAL, Type.DRAGON);
        setStats(78, 60, 85, 135, 91, 36);
        setMove(new AquaTail(), new DoubleTeam(), new WaterGun(), new Psychic());


    }
}