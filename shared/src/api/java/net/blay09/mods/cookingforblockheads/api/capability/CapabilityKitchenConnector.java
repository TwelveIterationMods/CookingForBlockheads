package net.blay09.mods.cookingforblockheads.api.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityKitchenConnector {

    @CapabilityInject(IKitchenConnector.class)
    public static Capability<IKitchenConnector> CAPABILITY;

    public static void register() {
        CapabilityManager.INSTANCE.register(IKitchenConnector.class, new Capability.IStorage<IKitchenConnector>() {
            @Override
            public INBT writeNBT(Capability<IKitchenConnector> capability, IKitchenConnector instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<IKitchenConnector> capability, IKitchenConnector instance, Direction side, INBT nbt) {

            }
        }, KitchenConnector::new);
    }

    public interface IKitchenConnector {

    }

    public static class KitchenConnector implements IKitchenConnector {

    }

}
