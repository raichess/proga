package moves.physical;

import ru.ifmo.se.pokemon.*;

public class FurySwipes extends PhysicalMove {
    public FurySwipes() {
        super(Type.NORMAL, 18, 80);
    }

    /* Fury Swipes hits 2-5 times per turn used. The probability of each interval is shown in the table, with the total power after each hit.*/
    @Override
    protected double calcBaseDamage(Pokemon p1, Pokemon p2) {
        double totalDamage = this.power;
        for (int i = 0; i < 4; i++) {
            if (i < 2) {
                if (Math.random() < 3.0 / 8.0) totalDamage += 18;
            } else {
                if (Math.random() < 1.0 / 8.0) totalDamage += 18;
            }
        }
        return totalDamage;

    }

    @Override
    public String describe() {
        return "Использует Fury Swipes";
    }
}
