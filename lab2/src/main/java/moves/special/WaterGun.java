package moves.special;

import ru.ifmo.se.pokemon.*;

public class WaterGun extends SpecialMove {
    public WaterGun() {
        super(Type.WATER, 40, 100);
    }

    @Override
    public String describe() {
        return "Использует Water Gun";
    }

}
