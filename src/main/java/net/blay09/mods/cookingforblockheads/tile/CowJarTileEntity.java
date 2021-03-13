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

    public CowJarTileEntity() {
        super(ModTileEntities.cowJar);
    }

    @Override
    public void read(BlockState state, CompoundNBT tagCompound) {
        super.read(state, tagCompound);

        if (tagCompound.contains("CustomName", Constants.NBT.TAG_STRING)) {
            customName = ITextComponent.Serializer.getComponentFromJson(tagCompound.getString("CustomName"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        if (customName != null) {
            tagCompound.putString("CustomName", ITextComponent.Serializer.toJson(customName));
        }

        return super.write(tagCompound);
    }

    @Override
    public void tick() {
        if (milkAmount < MILK_CAPACITY) {
            milkAmount += CookingForBlockheadsConfig.COMMON.cowJarMilkPerTick.get();
            isDirty = true;
        }

        ticksSinceUpdate++;
        if (isDirty && ticksSinceUpdate > UPDATE_INTERVAL) {
            VanillaPacketHandler.sendTileEntityUpdate(this);
            ticksSinceUpdate = 0;
            isDirty = false;
        }
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
        return new TranslationTextComponent("container.cookingforblockheads.cow_jar");
    }
}
