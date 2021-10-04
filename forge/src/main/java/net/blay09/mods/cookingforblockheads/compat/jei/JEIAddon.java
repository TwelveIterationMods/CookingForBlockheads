package net.blay09.mods.cookingforblockheads.compat.jei;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.client.gui.screen.RecipeBookScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@JeiPlugin
public class JEIAddon implements IModPlugin {

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.cowJar), CowJarRecipeCategory.UID);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (CookingForBlockheadsConfig.getActive().cowJarEnabled) {
            registration.addRecipes(ImmutableList.of(new CowJarRecipe()), CowJarRecipeCategory.UID);
        }
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(RecipeBookScreen.class, new IGuiContainerHandler<RecipeBookScreen>() {
            @Override
            public List<Rect2i> getGuiExtraAreas(RecipeBookScreen containerScreen) {
                List<Rect2i> list = Lists.newArrayList();
                for (Button button : containerScreen.getSortingButtons()) {
                    list.add(new Rect2i(button.x, button.y, button.getWidth(), button.getHeight()));
                }

                return list;
            }
        });
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("cookingforblockheads", "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CowJarRecipeCategory(guiHelper));
    }

}
