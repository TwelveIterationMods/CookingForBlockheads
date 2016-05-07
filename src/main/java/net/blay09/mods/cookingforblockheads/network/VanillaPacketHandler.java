package net.blay09.mods.cookingforblockheads.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class VanillaPacketHandler {

	public static void sendTileEntityUpdate(TileEntity tileEntity) {
		if(tileEntity.getWorld().isRemote) {
			return;
		}
		List<EntityPlayer> playerList = tileEntity.getWorld().playerEntities;
		for(EntityPlayer entityPlayer : playerList) {
			EntityPlayerMP entityPlayerMP = (EntityPlayerMP) entityPlayer;
			if (Math.hypot(entityPlayerMP.posX - tileEntity.getPos().getX() + 0.5, entityPlayerMP.posZ - tileEntity.getPos().getZ() + 0.5) < 64) {
				entityPlayerMP.playerNetServerHandler.sendPacket(tileEntity.getDescriptionPacket());
			}
		}
	}

}
