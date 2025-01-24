package net.blay09.mods.cookingforblockheads.compat.jei;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CowJarRecipeCategory implements IRecipeCategory<CowJarRecipe> {

    private static final ResourceLocation texture = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/gui/jei_cow_jar.png");

    public static final RecipeType<CowJarRecipe> TYPE = RecipeType.create(CookingForBlockheads.MOD_ID, "cow_jar", CowJarRecipe.class);
    private final IDrawable icon;
    private final IDrawableStatic background;

    public CowJarRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.cowJar));
        this.background = guiHelper.createDrawable(texture, 0, 0, 150, 110);
    }

    @Override
    public RecipeType<CowJarRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.cookingforblockheads:cow_jar");
    }

    @Override
    public int getWidth() {
        return 150;
    }

    @Override
    public int getHeight() {
        return 110;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CowJarRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 65, 1)
                .addIngredients(VanillaTypes.ITEM_STACK, ImmutableList.of(new ItemStack(Items.ANVIL)));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 65, 77).setStandardSlotBackground()
                .addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.milkJar));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 123, 77).setOutputSlotBackground()
                .addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.cowJar));
    }

    @Override
    public void draw(CowJarRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        RenderSystem.enableBlend();
        background.draw(guiGraphics);
        RenderSystem.disableBlend();
    }

}
