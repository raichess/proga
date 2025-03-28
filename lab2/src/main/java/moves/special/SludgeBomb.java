package moves.special;

import ru.ifmo.se.pokemon.*;

public class SludgeBomb extends SpecialMove {
    public SludgeBomb() {
        super(Type.POISON, 90, 100);
    }

    /*Sludge Bomb deals damage and has a 30% chance of poisoning the target.*/
    @Override
    protected void applyOppEffects(Pokemon p) {
        if (Math.random() <= 0.3) Effect.poison(p);
    }

    @Override
    public String describe() {
        return "Использует Sludge Bomb";
    }
}
