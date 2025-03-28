package moves.special;

import ru.ifmo.se.pokemon.*;

public class ShadowBall extends SpecialMove {
    public ShadowBall() {
        super(Type.GHOST, 80, 100);
    }

    /* Shadow Ball deals damage and has a 20% chance of lowering the target's Special Defense by one stage.*/
    @Override
    protected void applyOppEffects(Pokemon p) {
        Effect e = new Effect().chance(0.2).stat(Stat.SPECIAL_DEFENSE, -1);
        p.setCondition(e); /*addeffect*/
    }

    @Override
    public String describe() {
        return "Использует Shadow Ball";
    }
}
