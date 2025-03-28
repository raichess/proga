package moves.status;

import ru.ifmo.se.pokemon.*;

public class DoubleTeam extends StatusMove {
    public DoubleTeam() {
        super(Type.NORMAL, 0, 0);

    }

    /* Double Team raises the user's Evasiveness by one stage, thus making the user more difficult to hit.*/
    @Override
    public String describe() {
        return "Использует Double Team";
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        p.setMod(Stat.EVASION, 1);
    }
}