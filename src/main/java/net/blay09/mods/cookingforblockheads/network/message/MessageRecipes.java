package net.blay09.mods.cookingforblockheads.network.message;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.container.RecipeBookContainer;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeWithIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class MessageRecipes {

    private ItemStack outputItem;
    private List<FoodRecipeWithIngredients> recipeList;

    public MessageRecipes(ItemStack outputItem, List<FoodRecipeWithIngredients> recipeList) {
        this.outputItem = outputItem;
        this.recipeList = recipeList;
    }

    public static MessageRecipes decode(PacketBuffer buf) {
        ItemStack outputItem = buf.readItemStack();
        int recipeCount = buf.readInt();
        List<FoodRecipeWithIngredients> recipeList = Lists.newArrayListWithCapacity(recipeCount);
        for (int i = 0; i < recipeCount; i++) {
            recipeList.add(FoodRecipeWithIngredients.read(buf));
        }
        return new MessageRecipes(outputItem, recipeList);
    }

    public static void encode(MessageRecipes message, PacketBuffer buf) {
        buf.writeItemStack(message.outputItem);
        buf.writeInt(message.recipeList.size());
        for (FoodRecipeWithIngredients recipe : message.recipeList) {
            recipe.write(buf);
        }
    }

    public static void handle(MessageRecipes message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Container container = Minecraft.getInstance().player.openContainer;
            if (container instanceof RecipeBookContainer) {
                ((RecipeBookContainer) container).setRecipeList(message.outputItem, message.recipeList);
            }
        });
    }
}
