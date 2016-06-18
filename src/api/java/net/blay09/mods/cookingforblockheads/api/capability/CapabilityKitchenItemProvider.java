package net.blay09.mods.cookingforblockheads.api.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.concurrent.Callable;

public class CapabilityKitchenItemProvider {

	@CapabilityInject(IKitchenItemProvider.class)
	public static Capability<IKitchenItemProvider> CAPABILITY;

	public static void register() {
		CapabilityManager.INSTANCE.register(IKitchenItemProvider.class, new Capability.IStorage<IKitchenItemProvider>() {
			@Override
			public NBTBase writeNBT(Capability<IKitchenItemProvider> capability, IKitchenItemProvider instance, EnumFacing side) {
				return null;
			}

			@Override
			public void readNBT(Capability<IKitchenItemProvider> capability, IKitchenItemProvider instance, EnumFacing side, NBTBase nbt) {

			}
		}, new Callable<IKitchenItemProvider>() {
			@Override
			public IKitchenItemProvider call() throws Exception {
				return new KitchenItemProvider();
			}
		});
	}

}
