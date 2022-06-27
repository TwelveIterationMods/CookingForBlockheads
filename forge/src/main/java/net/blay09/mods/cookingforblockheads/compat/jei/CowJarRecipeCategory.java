package net.blay09.mods.cookingforblockheads.compat.jei;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CowJarRecipeCategory implements IRecipeCategory<CowJarRecipe> {

    private static final ResourceLocation texture = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/gui/jei_cow_jar.png");

    public static final RecipeType<CowJarRecipe> TYPE = RecipeType.create(CookingForBlockheads.MOD_ID, "cow_jar", CowJarRecipe.class);
    public static final ResourceLocation UID = new ResourceLocation("cookingforblockheads", "cow_jar");
    private final IDrawableStatic background;
    private final IDrawable icon;
    private final IDrawableStatic overlay;

    public CowJarRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 110);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.cowJar));
        this.overlay = guiHelper.createDrawable(texture, 0, 0, 64, 80);
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
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CowJarRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 65, 1)
                .addIngredients(VanillaTypes.ITEM_STACK, ImmutableList.of(new ItemStack(Items.ANVIL), new ItemStack(ModBlocks.milkJar)));
        builder.addSlot(RecipeIngredientRole.INPUT, 65, 77)
                .addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.cowJar));
    }

    @Override
    public void draw(CowJarRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        RenderSystem.enableBlend();
        overlay.draw(poseStack, 56, 20);
        RenderSystem.disableBlend();
    }

}
