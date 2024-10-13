package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.cookingforblockheads.crafting.KitchenImpl;
import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.blay09.mods.cookingforblockheads.menu.ModMenus;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

public class ItemRecipeBook extends Item {

    public enum RecipeBookEdition {
        NO_FILTER("no_filter_edition", () -> ModMenus.noFilterBook.get()),
        RECIPE("recipe_book", () -> ModMenus.recipeBook.get()),
        CRAFTING("crafting_book", () -> ModMenus.craftingBook.get());

        private final String name;
        private final Supplier<MenuType<KitchenMenu>> containerTypeSupplier;

        RecipeBookEdition(String name, Supplier<MenuType<KitchenMenu>> containerTypeSupplier) {
            this.name = name;
            this.containerTypeSupplier = containerTypeSupplier;
        }

        public String getName() {
            return name;
        }

        public Supplier<MenuType<KitchenMenu>> getMenuTypeSupplier() {
            return containerTypeSupplier;
        }
    }

    private final RecipeBookEdition edition;

    public ItemRecipeBook(RecipeBookEdition edition, Item.Properties properties) {
        super(properties.stacksTo(1));
        this.edition = edition;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            final var itemStack = player.getItemInHand(hand);
            Balm.getNetworking().openGui(player, new BalmMenuProvider<ItemStack>() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("container.cookingforblockheads." + edition.getName());
                }

                @Override
                public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
                    return new KitchenMenu(edition.getMenuTypeSupplier().get(), i, playerEntity, new KitchenImpl(itemStack));
                }

                @Override
                public ItemStack getScreenOpeningData(ServerPlayer serverPlayer) {
                    return itemStack;
                }

                @Override
                public StreamCodec<RegistryFriendlyByteBuf, ItemStack> getScreenStreamCodec() {
                    return ItemStack.STREAM_CODEC.cast();
                }
            });
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemStack, context, tooltip, flag);

        final var editionName = edition.getName();
        tooltip.add(Component.translatable("tooltip.cookingforblockheads." + editionName).withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.translatable("tooltip.cookingforblockheads." + editionName + ".description").withStyle(ChatFormatting.GRAY));
    }

}
