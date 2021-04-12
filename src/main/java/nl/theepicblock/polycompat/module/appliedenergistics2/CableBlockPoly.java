package nl.theepicblock.polycompat.module.appliedenergistics2;

import io.github.theepicblock.polymc.api.wizard.Wizard;
import io.github.theepicblock.polymc.impl.poly.block.SimpleReplacementPoly;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class CableBlockPoly extends SimpleReplacementPoly {
    public CableBlockPoly() {
        super(Blocks.AIR);
    }

    @Override
    public boolean hasWizard() {
        return true;
    }

    @Override
    public Wizard createWizard(Vec3d pos, Wizard.WizardState state) {
        return new CableWizard(pos, state);
    }

    public static class CableWizard extends Wizard {

        public CableWizard(Vec3d position, WizardState state) {
            super(position, state);
        }

        @Override
        public void addPlayer(ServerPlayerEntity playerEntity) {

        }

        @Override
        public void removePlayer(ServerPlayerEntity playerEntity) {

        }

        @Override
        public void removeAllPlayers() {

        }
    }
}