package moves.physical;

import ru.ifmo.se.pokemon.*;

public class HornLeech extends PhysicalMove {
    public HornLeech() {
        super(Type.GRASS, 75, 100);
    }

    /* Horn Leech deals damage and the user will recover 50% of the HP drained. */
    @Override
    protected void applySelfEffects(Pokemon p) {
        double damageTaken = p.getStat(Stat.HP) - p.getHP();
        p.setMod(Stat.HP, (int) Math.round(damageTaken * 0.5));

    }

    @Override
    public String describe() {
        return "Использует Horn Leech";
    }
}
