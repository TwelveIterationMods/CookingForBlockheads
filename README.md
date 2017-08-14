# Cooking for Blockheads

Minecraft Mod. Adds a cooking book and multiblock kitchens that only shows recipes you can make with what you currently have in your inventory.

## Development Builds
Potentially unstable in-development releases built straight from the latest code in this repository are available [on my Jenkins](http://jenkins.blay09.net).
They may contain unfinished and broken features and no support is provided for these builds.

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

If the IMC API is not enough for you, you can build against Cooking for Blockheads' Java API.
The Java API allows everything the IMC API does, and certain tasks can only be achieved via the Java API.
However, if you don't need that extra control, it is recommended to use the IMC API.

### Adding the dependency to your build.gradle
```
repositories {
    maven {
        url "http://blay09.net:8081/artifactory/jenkins-maven/"
    }
}

dependencies {
    compile "net.blay09.mods:CookingForBlockheads:3.1.17"
}
```