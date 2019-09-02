package net.blay09.mods.cookingforblockheads.api.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityKitchenItemProvider {

    @CapabilityInject(IKitchenItemProvider.class)
    public static Capability<IKitchenItemProvider> CAPABILITY;

    public static void register() {
        CapabilityManager.INSTANCE.register(IKitchenItemProvider.class, new Capability.IStorage<IKitchenItemProvider>() {
            @Override
            public INBT writeNBT(Capability<IKitchenItemProvider> capability, IKitchenItemProvider instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<IKitchenItemProvider> capability, IKitchenItemProvider instance, Direction side, INBT nbt) {

            }
        }, KitchenItemProvider::new);
    }

}
