package moves.physical;

import ru.ifmo.se.pokemon.*;

public class AquaTail extends PhysicalMove {
    public AquaTail() {
        super(Type.WATER, 90, 90);

    }

    @Override
    public String describe() {
        return "Использует Aqua Tail";

    }
}