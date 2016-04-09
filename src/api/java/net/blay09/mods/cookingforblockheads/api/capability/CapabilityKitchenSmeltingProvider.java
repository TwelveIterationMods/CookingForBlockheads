package net.blay09.mods.cookingforblockheads.api.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.concurrent.Callable;

public class CapabilityKitchenSmeltingProvider {

	@CapabilityInject(IKitchenSmeltingProvider.class)
	public static Capability<IKitchenSmeltingProvider> KITCHEN_SMELTING_PROVIDER_CAPABILITY;

	public static void register() {
		CapabilityManager.INSTANCE.register(IKitchenSmeltingProvider.class, new Capability.IStorage<IKitchenSmeltingProvider>() {
			@Override
			public NBTBase writeNBT(Capability<IKitchenSmeltingProvider> capability, IKitchenSmeltingProvider instance, EnumFacing side) {
				return null;
			}

			@Override
			public void readNBT(Capability<IKitchenSmeltingProvider> capability, IKitchenSmeltingProvider instance, EnumFacing side, NBTBase nbt) {

			}
		}, new Callable<IKitchenSmeltingProvider>() {
			@Override
			public IKitchenSmeltingProvider call() throws Exception {
				return new KitchenSmeltingProvider();
			}
		});
	}

}
