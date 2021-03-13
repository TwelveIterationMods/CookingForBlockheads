package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.network.VanillaPacketHandler;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

public class CowJarTileEntity extends MilkJarTileEntity implements ITickableTileEntity, IMutableNameable {

    private static final int UPDATE_INTERVAL = 20;

    private boolean isDirty;
    private int ticksSinceUpdate;

    private ITextComponent customName;
    private boolean compressedCow;

    public CowJarTileEntity() {
        super(ModTileEntities.cowJar);
    }

    @Override
    public void read(BlockState state, CompoundNBT tagCompound) {
        super.read(state, tagCompound);

        if (tagCompound.contains("CustomName", Constants.NBT.TAG_STRING)) {
            customName = ITextComponent.Serializer.getComponentFromJson(tagCompound.getString("CustomName"));
        }

        compressedCow = tagCompound.getBoolean("CompressedCow");
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        if (customName != null) {
            tagCompound.putString("CustomName", ITextComponent.Serializer.toJson(customName));
        }

        tagCompound.putBoolean("CompressedCow", compressedCow);

        return super.write(tagCompound);
    }

    @Override
    public void tick() {
        if (milkAmount < MILK_CAPACITY) {
            double milkToAdd = CookingForBlockheadsConfig.COMMON.cowJarMilkPerTick.get();
            if (compressedCow) {
                milkToAdd *= CookingForBlockheadsConfig.COMMON.compressedCowJarMilkMultiplier.get();;
            }

            milkAmount += milkToAdd;
            isDirty = true;
        }

        ticksSinceUpdate++;
        if (isDirty && ticksSinceUpdate > UPDATE_INTERVAL) {
            VanillaPacketHandler.sendTileEntityUpdate(this);
            ticksSinceUpdate = 0;
            isDirty = false;
        }
    }

    public boolean isCompressedCow() {
        return compressedCow;
    }

    public void setCompressedCow(boolean compressedCow) {
        this.compressedCow = compressedCow;
    }

    @Override
    public void setCustomName(ITextComponent customName) {
        this.customName = customName;
        markDirty();
    }

    @Override
    public ITextComponent getCustomName() {
        return customName;
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }

    @Override
    public ITextComponent getDisplayName() {
        return getName();
    }

    @Override
    public ITextComponent getName() {
        return customName != null ? customName : getDefaultName();
    }

    @Override
    public ITextComponent getDefaultName() {
        if (compressedCow) {
            return new TranslationTextComponent("container.cookingforblockheads.cow_jar_compressed");
        }

        return new TranslationTextComponent("container.cookingforblockheads.cow_jar");
    }
}
