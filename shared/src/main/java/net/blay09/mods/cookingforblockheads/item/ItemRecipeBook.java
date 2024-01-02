package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.cookingforblockheads.menu.ModMenus;
import net.blay09.mods.cookingforblockheads.menu.RecipeBookMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ItemRecipeBook extends Item {

    public enum RecipeBookEdition {
        NO_FILTER("no_filter_edition", () -> ModMenus.noFilterBook.get()),
        RECIPE("recipe_book", () -> ModMenus.recipeBook.get()),
        CRAFTING("crafting_book", () -> ModMenus.craftingBook.get());

        private final String name;
        private final Supplier<MenuType<RecipeBookMenu>> containerTypeSupplier;

        RecipeBookEdition(String name, Supplier<MenuType<RecipeBookMenu>> containerTypeSupplier) {
            this.name = name;
            this.containerTypeSupplier = containerTypeSupplier;
        }

        public String getName() {
            return name;
        }

        public Supplier<MenuType<RecipeBookMenu>> getContainerTypeSupplier() {
            return containerTypeSupplier;
        }
    }

    private final RecipeBookEdition edition;

    public ItemRecipeBook(RecipeBookEdition edition) {
        super(new Item.Properties().stacksTo(1));
        this.edition = edition;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            Balm.getNetworking().openGui(player, new BalmMenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("container.cookingforblockheads." + edition.getName());
                }

                @Override
                public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
                    RecipeBookMenu container = new RecipeBookMenu(edition.getContainerTypeSupplier().get(), i, playerEntity);
                    if (edition == RecipeBookEdition.NO_FILTER) {
                        container.setNoFilter();
                    } else if (edition == RecipeBookEdition.CRAFTING) {
                        container.allowCrafting();
                    }
                    return container;
                }
            });
        }

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, tooltip, flag);

        final var editionName = edition.getName();
        tooltip.add(Component.translatable("tooltip.cookingforblockheads." + editionName).withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.translatable("tooltip.cookingforblockheads." + editionName + ".description").withStyle(ChatFormatting.GRAY));
    }

}
