//package net.blay09.mods.cookingforblockheads.api.capability; TODO
//
//import net.minecraft.nbt.INBT;
//import net.minecraft.util.Direction;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.CapabilityInject;
//import net.minecraftforge.common.capabilities.CapabilityManager;
//
//public class CapabilityKitchenSmeltingProvider {
//
//    @CapabilityInject(IKitchenSmeltingProvider.class)
//    public static Capability<IKitchenSmeltingProvider> CAPABILITY;
//
//    public static void register() {
//        CapabilityManager.INSTANCE.register(IKitchenSmeltingProvider.class, new Capability.IStorage<IKitchenSmeltingProvider>() {
//            @Override
//            public INBT writeNBT(Capability<IKitchenSmeltingProvider> capability, IKitchenSmeltingProvider instance, Direction side) {
//                return null;
//            }
//
//            @Override
//            public void readNBT(Capability<IKitchenSmeltingProvider> capability, IKitchenSmeltingProvider instance, Direction side, INBT nbt) {
//
//            }
//        }, DefaultKitchenSmeltingProvider::new);
//    }
//
//}
