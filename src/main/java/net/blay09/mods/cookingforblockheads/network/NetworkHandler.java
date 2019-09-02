package net.blay09.mods.cookingforblockheads.network;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.message.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {

    private static final String version = "1.0";

    public static final SimpleChannel channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(CookingForBlockheads.MOD_ID, "network"), () -> version, it -> it.equals(version), it -> it.equals(version));

    public static void init() {
        channel.registerMessage(0, MessageItemList.class, MessageItemList::encode, MessageItemList::decode, MessageItemList::handle);
        channel.registerMessage(1, MessageCraftRecipe.class, MessageCraftRecipe::encode, MessageCraftRecipe::decode, MessageCraftRecipe::handle);
        channel.registerMessage(2, MessageSyncedEffect.class, MessageSyncedEffect::encode, MessageSyncedEffect::decode, MessageSyncedEffect::handle);
        channel.registerMessage(3, MessageRequestRecipes.class, MessageRequestRecipes::encode, MessageRequestRecipes::decode, MessageRequestRecipes::handle);
        channel.registerMessage(4, MessageRecipes.class, MessageRecipes::encode, MessageRecipes::decode, MessageRecipes::handle);
    }

}
