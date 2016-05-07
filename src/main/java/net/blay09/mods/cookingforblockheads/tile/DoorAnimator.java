package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.container.IContainerWithDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class DoorAnimator {

	private final TileEntity tileEntity;
	private final int eventNumPlayers;
	private final int eventForcedOpen;
	private float angle;
	private float prevAngle;
	private int numPlayersUsing;
	private int ticksSinceSync;
	private float openRadius = 2.5f;
	private boolean isForcedOpen;

	public DoorAnimator(TileEntity tileEntity, int eventNumPlayers, int eventForcedOpen) {
		this.tileEntity = tileEntity;
		this.eventNumPlayers = eventNumPlayers;
		this.eventForcedOpen = eventForcedOpen;
	}

	public void setOpenRadius(float openRadius) {
		this.openRadius = openRadius;
	}

	public void update() {
		ticksSinceSync++;
		int x = tileEntity.getPos().getX();
		int y = tileEntity.getPos().getY();
		int z = tileEntity.getPos().getZ();
		if (!tileEntity.getWorld().isRemote && numPlayersUsing != 0 && (ticksSinceSync + x + y + z) % 200 == 0) {
			// This is Mojang's bad fix for chests staying open. Because it makes so much more sense to do this than to ensure onContainerClosed is always called properly.
			numPlayersUsing = 0;
			float range = 5f;
			for (EntityPlayer entityplayer : tileEntity.getWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(x - range, y - range, z - range, x + 1 + range, y + 1 + range, z + 1 + range))) {
				if (entityplayer.openContainer instanceof IContainerWithDoor) {
					if (((IContainerWithDoor) entityplayer.openContainer).isTileEntity(tileEntity)) {
						numPlayersUsing++;
					}
				}
			}
		}

		prevAngle = angle;

		float angleSpeed = 0.1f;
		if (((numPlayersUsing == 0 || !isForcedOpen) && angle > 0f) || ((isForcedOpen || numPlayersUsing > 0) && angle < 1f)) {
			if (numPlayersUsing > 0 || isForcedOpen) {
				angle += angleSpeed;
			} else {
				angle -= angleSpeed;
			}
			angle = Math.min(angle, 1f);
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
		tileEntity.getWorld().addBlockEvent(tileEntity.getPos(), tileEntity.getBlockType(), 2, isForcedOpen ? 1 : 0);
	}

	public boolean receiveClientEvent(int id, int type) {
		if (id == eventNumPlayers) {
			numPlayersUsing = type;
			return true;
		} else if(id == eventForcedOpen) {
			isForcedOpen = type == 1;
			return true;
		}
		return false;
	}

	public void openContainer(EntityPlayer player) {
		if (!player.isSpectator()) {
			numPlayersUsing = Math.max(0, numPlayersUsing + 1);
			tileEntity.getWorld().addBlockEvent(tileEntity.getPos(), tileEntity.getBlockType(), eventNumPlayers, numPlayersUsing);
			tileEntity.getWorld().notifyNeighborsOfStateChange(tileEntity.getPos(), tileEntity.getBlockType());
			tileEntity.getWorld().notifyNeighborsOfStateChange(tileEntity.getPos().down(), tileEntity.getBlockType());
		}
	}

	public void closeContainer(EntityPlayer player) {
		if (!player.isSpectator()) {
			numPlayersUsing--;
			tileEntity.getWorld().addBlockEvent(tileEntity.getPos(), tileEntity.getBlockType(), eventNumPlayers, numPlayersUsing);
			tileEntity.getWorld().notifyNeighborsOfStateChange(tileEntity.getPos(), tileEntity.getBlockType());
			tileEntity.getWorld().notifyNeighborsOfStateChange(tileEntity.getPos().down(), tileEntity.getBlockType());
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
