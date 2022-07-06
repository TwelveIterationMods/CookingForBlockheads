package net.blay09.mods.cookingforblockheads.tile.util;

import net.blay09.mods.cookingforblockheads.menu.IContainerWithDoor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

public class DoorAnimator {

    private final BlockEntity blockEntity;
    private final int eventNumPlayers;
    private final int eventForcedOpen;
    private float angle;
    private float prevAngle;
    private int numPlayersUsing;
    private int ticksSinceSync;
    private SoundEvent soundEventOpen;
    private SoundEvent soundEventClose;
    private float openRadius = 2.5f;
    private boolean isForcedOpen;

    public DoorAnimator(BlockEntity tileEntity, int eventNumPlayers, int eventForcedOpen) {
        this.blockEntity = tileEntity;
        this.eventNumPlayers = eventNumPlayers;
        this.eventForcedOpen = eventForcedOpen;
    }

    public void setSoundEventOpen(SoundEvent soundEventOpen) {
        this.soundEventOpen = soundEventOpen;
    }

    public void setSoundEventClose(SoundEvent soundEventClose) {
        this.soundEventClose = soundEventClose;
    }

    public void setOpenRadius(float openRadius) {
        this.openRadius = openRadius;
    }

    public void update() {
        ticksSinceSync++;
        int x = blockEntity.getBlockPos().getX();
        int y = blockEntity.getBlockPos().getY();
        int z = blockEntity.getBlockPos().getZ();
        if (!blockEntity.getLevel().isClientSide && numPlayersUsing != 0 && (ticksSinceSync + x + y + z) % 200 == 0) {
            // This is Mojang's bad fix for chests staying open. Because it makes so much more sense to do this than to ensure onContainerClosed is always called properly.
            numPlayersUsing = 0;
            float range = 5f;
            for (Player player : blockEntity.getLevel().getEntitiesOfClass(Player.class, new AABB(x - range, y - range, z - range, x + 1 + range, y + 1 + range, z + 1 + range))) {
                if (player.containerMenu instanceof IContainerWithDoor) {
                    if (((IContainerWithDoor) player.containerMenu).isTileEntity(blockEntity)) {
                        numPlayersUsing++;
                    }
                }
            }
        }

        prevAngle = angle;

        if ((isForcedOpen || numPlayersUsing > 0) && angle == 0f && soundEventOpen != null) {
            blockEntity.getLevel().playLocalSound(x, y, z, soundEventOpen, SoundSource.BLOCKS, 0.5f, blockEntity.getLevel().random.nextFloat() * 0.1f + 0.9f, false);
        }

        float angleSpeed = 0.1f;
        if (((numPlayersUsing == 0 || !isForcedOpen) && angle > 0f) || ((isForcedOpen || numPlayersUsing > 0) && angle < 1f)) {
            float angleBefore = angle;
            if (numPlayersUsing > 0 || isForcedOpen) {
                angle += angleSpeed;
            } else {
                angle -= angleSpeed;
            }
            angle = Math.min(angle, 1f);
            float playCloseSound = 0.5f;
            if (angle < playCloseSound && angleBefore >= playCloseSound && soundEventClose != null) {
                blockEntity.getLevel().playLocalSound(x, y, z, soundEventClose, SoundSource.BLOCKS, 0.5f, blockEntity.getLevel().random.nextFloat() * 0.1f + 0.9f, false);
            }
            angle = Math.max(angle, 0f);
        }
    }

    public void toggleForcedOpen() {
        setForcedOpen(!isForcedOpen);
    }

    public boolean isForcedOpen() {
        return isForcedOpen;
    }

    public void setForcedOpen(boolean isForcedOpen) {
        this.isForcedOpen = isForcedOpen;
        Level level = blockEntity.getLevel();
        if (level != null) {
            level.blockEvent(blockEntity.getBlockPos(), blockEntity.getBlockState().getBlock(), 2, isForcedOpen ? 1 : 0);
        }
    }

    public boolean receiveClientEvent(int id, int type) {
        if (id == eventNumPlayers) {
            numPlayersUsing = type;
            return true;
        } else if (id == eventForcedOpen) {
            isForcedOpen = type == 1;
            return true;
        }
        return false;
    }

    public void openContainer(Player player) {
        if (!player.isSpectator()) {
            numPlayersUsing = Math.max(0, numPlayersUsing + 1);
            fireBlockEvent();
        }
    }

    public void closeContainer(Player player) {
        if (!player.isSpectator()) {
            numPlayersUsing--;
            fireBlockEvent();
        }
    }

    private void fireBlockEvent() {
        Level level = blockEntity.getLevel();
        Block block = blockEntity.getBlockState().getBlock();
        if (level != null) {
            level.blockEvent(blockEntity.getBlockPos(), block, eventNumPlayers, numPlayersUsing);
            level.updateNeighborsAt(blockEntity.getBlockPos(), block);
            level.updateNeighborsAt(blockEntity.getBlockPos().below(), block);
        }
    }

    public float getRenderAngle(float partialTicks) {
        float renderAngle = prevAngle + (angle - prevAngle) * partialTicks;
        renderAngle = 1f - renderAngle;
        renderAngle = 1f - renderAngle * renderAngle * renderAngle;
//		renderAngle = 1f;
        return (float) ((Math.PI / openRadius) * renderAngle);
    }

    public int getNumPlayersUsing() {
        return numPlayersUsing;
    }

    public void setNumPlayersUsing(int numPlayersUsing) {
        this.numPlayersUsing = numPlayersUsing;
    }
}
