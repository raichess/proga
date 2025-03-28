package moves.physical;

import ru.ifmo.se.pokemon.*;

public class Psychic extends PhysicalMove {
    public Psychic() {
        super(Type.PSYCHIC, 90, 100);
    }

    /* Psychic deals damage and has a 10% chance of lowering the target's Special Defense by one stage.*/
    @Override
    protected void applyOppEffects(Pokemon p) {
        Effect e = new Effect().chance(0.1).stat(Stat.SPECIAL_DEFENSE, -1);
        p.setCondition(e);
    }

    @Override
    public String describe() {
        return "Использует Psychic";
    }
}
