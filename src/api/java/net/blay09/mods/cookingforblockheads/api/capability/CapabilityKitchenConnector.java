package net.blay09.mods.cookingforblockheads.api.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityKitchenConnector {

	@CapabilityInject(IKitchenConnector.class)
	public static Capability<IKitchenConnector> CAPABILITY;

	public static void register() {
		CapabilityManager.INSTANCE.register(IKitchenConnector.class, new Capability.IStorage<IKitchenConnector>() {
			@Override
			public NBTBase writeNBT(Capability<IKitchenConnector> capability, IKitchenConnector instance, EnumFacing side) {
				return null;
			}

			@Override
			public void readNBT(Capability<IKitchenConnector> capability, IKitchenConnector instance, EnumFacing side, NBTBase nbt) {

			}
		}, KitchenConnector::new);
	}

	public interface IKitchenConnector {

	}

	public static class KitchenConnector implements IKitchenConnector {

	}

}
