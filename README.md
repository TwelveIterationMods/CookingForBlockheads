# Cooking for Blockheads

Minecraft Mod. Adds a cooking book and multiblock kitchens that only shows recipes you can make with what you currently have in your inventory.

[![Versions](http://cf.way2muchnoise.eu/versions/cooking-for-blockheads.svg)](https://minecraft.curseforge.com/projects/cooking-for-blockheads) [![Downloads](http://cf.way2muchnoise.eu/full_cooking-for-blockheads_downloads.svg)](https://minecraft.curseforge.com/projects/cooking-for-blockheads)

## IMC API

The below is a list of IMC message keys handled by Cooking for Blockheads.

* **RegisterTool** (ItemStack)
* **RegisterWaterItem** (ItemStack)
* **RegisterMilkItem** (ItemStack)
* **RegisterToast** (TagCompound : {Input : ItemStack, Output : ItemStack})
* **RegisterToastError** (TagCompound : {Input : ItemStack, Message : String})
* **RegisterOvenFuel** (TagCompound : {Input : ItemStack, FuelValue : Numeric})
* **RegisterOvenRecipe** (TagCompound : {Input : ItemStack, Output : ItemStack})
* **RegisterNonFoodRecipe** (ItemStack)
* **RegisterCowClass** (EntityCowClassName : String) - this will cause the class to be loaded if it isn't already!

## Java API

If the IMC API is not enough for you, you can build against Cooking for Blockheads' Java API. I suggest using the CurseForge Maven to grab it as a dependency. For info on how to do that, you can check Cooking for Blockheads' own Gradle files.

The Java API allows everything the IMC API does, and certain tasks can only be achieved via the Java API.
However, if you don't need that extra control, it is recommended to use the IMC API.
