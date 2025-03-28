package moves.status;

import ru.ifmo.se.pokemon.*;

public class WorkUp extends StatusMove {
    public WorkUp() {
        super(Type.NORMAL, 0, 0);
    }

    /* Work Up raises the user's Attack and Special Attack by one stage each.*/
    @Override
    protected void applySelfEffects(Pokemon p) {
        p.setMod(Stat.ATTACK, 1);
        p.setMod(Stat.SPECIAL_ATTACK, 1);

    }

    @Override
    public String describe() {
        return "Использует Work Up";
    }

}
