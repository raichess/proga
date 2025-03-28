package moves.physical;

import ru.ifmo.se.pokemon.*;


public class FeintAttack extends PhysicalMove {
    public FeintAttack() {
        super(Type.DARK, 60, Double.POSITIVE_INFINITY);
    }

    /* Feint Attack deals damage and ignores changes to the Accuracy and Evasion stats.*/
    @Override
    protected boolean checkAccuracy(Pokemon att, Pokemon def) {
        return true;
    }

    @Override
    public String describe() {
        return "Использует Feint Attack";
    }
}
