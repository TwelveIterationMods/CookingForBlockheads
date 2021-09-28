# Cooking for Blockheads

Minecraft Mod. Adds a cooking book and multiblock kitchens that only shows recipes you can make with what you currently have in your inventory.

See [the license](LICENSE) for modpack permissions etc.

This mod is available for both Forge and Fabric (starting Minecraft 1.17). This is a trial run to see if supporting both
platforms is feasible.

#### Forge

[![Versions](http://cf.way2muchnoise.eu/versions/231484_latest.svg)](https://www.curseforge.com/minecraft/mc-mods/cooking-for-blockheads)
[![Downloads](http://cf.way2muchnoise.eu/full_231484_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/cooking-for-blockheads)

#### Fabric

[![Versions](http://cf.way2muchnoise.eu/versions/_latest.svg)](https://www.curseforge.com/minecraft/mc-mods/cooking-for-blockheads-fabric)
[![Downloads](http://cf.way2muchnoise.eu/full__downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/cooking-for-blockheads-fabric)

## Contributing

If you're interested in contributing to the mod, you can check
out [issues labelled as "help wanted"](https://github.com/ModdingForBlockheads/CookingForBlockheads/issues?q=is%3Aopen+is%3Aissue+label%3A%22help+wanted%22)
. These should be ready to be implemented as they are.

If you need help, feel free to join us on [Discord](https://discord.gg/scGAfXC).

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
