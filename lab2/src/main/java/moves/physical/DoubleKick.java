package moves.physical;

import ru.ifmo.se.pokemon.*;

public class DoubleKick extends PhysicalMove {
    public DoubleKick() {
        super(Type.FIGHTING, 30, 100);
    }

    /*Double Kick deals damage and will strike twice (with 30 base power each time).*/
    @Override
    protected void applyOppDamage(Pokemon p, double damage) {
        super.applyOppDamage(p, damage);
        super.applyOppDamage(p, damage);
    }

    @Override
    public String describe() {
        return "Использует Double Kick";
    }
}
