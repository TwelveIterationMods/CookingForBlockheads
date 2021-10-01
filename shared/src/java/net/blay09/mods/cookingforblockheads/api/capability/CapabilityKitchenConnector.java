//package net.blay09.mods.cookingforblockheads.api.capability; TODO
//
//public class CapabilityKitchenConnector {
//
//    @CapabilityInject(IKitchenConnector.class)
//    public static Capability<IKitchenConnector> CAPABILITY;
//
//    public static void register() {
//        CapabilityManager.INSTANCE.register(IKitchenConnector.class, new Capability.IStorage<IKitchenConnector>() {
//            @Override
//            public INBT writeNBT(Capability<IKitchenConnector> capability, IKitchenConnector instance, Direction side) {
//                return null;
//            }
//
//            @Override
//            public void readNBT(Capability<IKitchenConnector> capability, IKitchenConnector instance, Direction side, INBT nbt) {
//
//            }
//        }, DefaultKitchenConnector::new);
//    }
//
//}
