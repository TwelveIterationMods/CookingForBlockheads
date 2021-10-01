//package net.blay09.mods.cookingforblockheads.compat.jei; TODO
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.matrix.MatrixStack;
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.PoseStack;
//import mezz.jei.api.constants.VanillaTypes;
//import mezz.jei.api.gui.IRecipeLayout;
//import mezz.jei.api.gui.drawable.IDrawable;
//import mezz.jei.api.gui.drawable.IDrawableStatic;
//import mezz.jei.api.helpers.IGuiHelper;
//import mezz.jei.api.ingredients.IIngredients;
//import mezz.jei.api.recipe.category.IRecipeCategory;
//import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
//import net.blay09.mods.cookingforblockheads.block.ModBlocks;
//import net.minecraft.client.resources.I18n;
//import net.minecraft.client.resources.language.I18n;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.Items;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//
//import javax.annotation.Nonnull;
//
//public class CowJarRecipeCategory implements IRecipeCategory<CowJarRecipe> {
//
//    private static final ResourceLocation texture = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/gui/jei_cow_jar.png");
//
//    public static final ResourceLocation UID = new ResourceLocation("cookingforblockheads", "cow_jar");
//    private final IDrawableStatic background;
//    private final IDrawable icon;
//    private final String title;
//    private final IDrawableStatic overlay;
//
//    public CowJarRecipeCategory(IGuiHelper guiHelper) {
//        this.background = guiHelper.createBlankDrawable(150, 110);
//        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.cowJar));
//        this.title = I18n.get("jei.cookingforblockheads:cow_jar");
//        this.overlay = guiHelper.createDrawable(texture, 0, 0, 64, 80);
//    }
//
//    @Override
//    public ResourceLocation getUid() {
//        return UID;
//    }
//
//    @Override
//    public Class<? extends CowJarRecipe> getRecipeClass() {
//        return CowJarRecipe.class;
//    }
//
//    @Override
//    public String getTitle() {
//        return title;
//    }
//
//    @Override
//    public IDrawable getBackground() {
//        return background;
//    }
//
//    @Override
//    public IDrawable getIcon() {
//        return icon;
//    }
//
//    @Override
//    public void setIngredients(CowJarRecipe cowJarRecipe, IIngredients ingredients) {
//        ingredients.setInputs(VanillaTypes.ITEM, ImmutableList.of(new ItemStack(Items.ANVIL), new ItemStack(ModBlocks.milkJar)));
//        ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(ModBlocks.cowJar));
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout recipeLayout, CowJarRecipe recipeWrapper, IIngredients ingredients) {
//        recipeLayout.getItemStacks().init(0, true, 64, 0);
//        recipeLayout.getItemStacks().init(1, true, 64, 76);
//        recipeLayout.getItemStacks().set(ingredients);
//    }
//
//    @Override
//    public void draw(CowJarRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
//        RenderSystem.enableAlphaTest();
//        RenderSystem.enableBlend();
//        overlay.draw(poseStack, 56, 20);
//        RenderSystem.disableBlend();
//        RenderSystem.disableAlphaTest();
//    }
//
//}
