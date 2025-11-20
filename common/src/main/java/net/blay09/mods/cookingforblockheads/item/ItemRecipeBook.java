package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.world.BalmMenuProvider;
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
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemRecipeBook extends Item {

    public enum RecipeBookEdition {
        NO_FILTER("no_filter_edition", () -> ModMenus.noFilterBook.value()),
        RECIPE("recipe_book", () -> ModMenus.recipeBook.value()),
        CRAFTING("crafting_book", () -> ModMenus.craftingBook.value());

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

    public static ItemRecipeBook recipeBook(Item.Properties properties) {
        return new ItemRecipeBook(RecipeBookEdition.RECIPE, properties);
    }

    public static ItemRecipeBook craftingBook(Item.Properties properties) {
        return new ItemRecipeBook(RecipeBookEdition.CRAFTING, properties);
    }

    public static ItemRecipeBook noFilterBook(Item.Properties properties) {
        return new ItemRecipeBook(RecipeBookEdition.NO_FILTER, properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            final var itemStack = player.getItemInHand(hand);
            Balm.networking().openMenu(player, new BalmMenuProvider<ItemStack>() {
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
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
        final var editionName = edition.getName();
        consumer.accept(Component.translatable("tooltip.cookingforblockheads." + editionName).withStyle(ChatFormatting.YELLOW));
        consumer.accept(Component.translatable("tooltip.cookingforblockheads." + editionName + ".description").withStyle(ChatFormatting.GRAY));
    }

}
