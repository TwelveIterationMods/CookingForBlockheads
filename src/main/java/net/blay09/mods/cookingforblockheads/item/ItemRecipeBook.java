package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.container.ModContainers;
import net.blay09.mods.cookingforblockheads.container.RecipeBookContainer;
import net.blay09.mods.cookingforblockheads.util.TextUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ItemRecipeBook extends Item {

    public enum RecipeBookEdition {
        NOFILTER(() -> ModContainers.noFilterBook),
        RECIPE(() -> ModContainers.recipeBook),
        CRAFTING(() -> ModContainers.craftingBook);

        private final Supplier<ContainerType<RecipeBookContainer>> containerTypeSupplier;

        RecipeBookEdition(Supplier<ContainerType<RecipeBookContainer>> containerTypeSupplier) {
            this.containerTypeSupplier = containerTypeSupplier;
        }

        public Supplier<ContainerType<RecipeBookContainer>> getContainerTypeSupplier() {
            return containerTypeSupplier;
        }
    }

    private final RecipeBookEdition edition;

    public ItemRecipeBook(RecipeBookEdition edition) {
        super(new Item.Properties().group(CookingForBlockheads.itemGroup).maxStackSize(1));
        this.edition = edition;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (world.isRemote) {
            NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent(Objects.requireNonNull(getRegistryName()).toString());
                }

                @Override
                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    RecipeBookContainer container = new RecipeBookContainer(edition.getContainerTypeSupplier().get(), i, playerEntity);
                    if (edition == RecipeBookEdition.NOFILTER) {
                        container.setNoFilter();
                    } else if (edition == RecipeBookEdition.CRAFTING) {
                        container.allowCrafting();
                    }
                    return container;
                }
            });
        }

        return ActionResult.newResult(ActionResultType.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(itemStack, world, tooltip, flag);

        String registryName = Objects.requireNonNull(getRegistryName()).toString();
        tooltip.add(TextUtils.coloredTextComponent("tooltip." + registryName, TextFormatting.YELLOW));
        for (String s : I18n.format("tooltip." + registryName + ".description").split("\\\\n")) {
            tooltip.add(TextUtils.coloredTextComponent(s, TextFormatting.GRAY));
        }
    }

}
