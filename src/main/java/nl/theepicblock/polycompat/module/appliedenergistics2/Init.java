package nl.theepicblock.polycompat.module.appliedenergistics2;

import appeng.core.Api;
import io.github.theepicblock.polymc.api.PolyRegistry;
import io.github.theepicblock.polymc.api.block.BlockStateManager;
import io.github.theepicblock.polymc.api.block.BlockStateProfile;
import io.github.theepicblock.polymc.impl.poly.block.UnusedBlockStatePoly;
import net.minecraft.block.Block;

public class Init {
    public static void RegisterPolys(PolyRegistry registry) {
        try {
            Block charger = Api.INSTANCE.definitions().blocks().charger().block();
            registry.registerBlockPoly(
                    charger,
                    new UnusedBlockStatePoly(charger, registry, BlockStateProfile.LEAVES_PROFILE));
        } catch (BlockStateManager.StateLimitReachedException ignored) {}

        Block wire = Api.INSTANCE.definitions().blocks().multiPart().block();
        registry.registerBlockPoly(wire, new CableBlockPoly());
    }
}
