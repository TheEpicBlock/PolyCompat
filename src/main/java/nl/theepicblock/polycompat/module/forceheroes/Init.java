package nl.theepicblock.polycompat.module.forceheroes;

import io.github.theepicblock.polymc.api.register.PolyRegistry;

public class Init {
    public static void registerPolys(PolyRegistry polyRegistry) {
        System.out.println("test polys");
    }

    public static void onInitialize() {
        System.out.println("test init");
    }
}
