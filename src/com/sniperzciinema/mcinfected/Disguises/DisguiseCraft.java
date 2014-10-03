
package com.sniperzciinema.mcinfected.Disguises;

import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;
import pgDev.bukkit.DisguiseCraft.disguise.Disguise;
import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;

import com.sniperzciinema.mcinfected.IPlayers.IPlayer;
import com.sniperzciinema.mcinfected.Utils.StringUtil;


public class DisguiseCraft extends Disguises {
	
	private DisguiseCraftAPI	dcAPI	= pgDev.bukkit.DisguiseCraft.DisguiseCraft.getAPI();
	
	public DisguiseCraft()
	{
		super("Disguise Craft");
		for (DisguiseType disguise : DisguiseType.values())
			this.disguiseList.add(disguise.toString());
	}
	
	@Override
	public void disguise(IPlayer player) {
		
		if (!isDisguised(player))
			if (DisguiseType.fromString(StringUtil.getCapitalized(player.getKit(player.getTeam()).getDisguise())) != null)
			{
				Disguise disguise = new Disguise(this.dcAPI.newEntityID(),
						DisguiseType.fromString(StringUtil.getCapitalized(player.getKit(player.getTeam()).getDisguise()))).addSingleData("noarmor");
				this.dcAPI.disguisePlayer(player.getPlayer(), disguise);
			}
			else
				this.dcAPI.disguisePlayer(player.getPlayer(), new Disguise(this.dcAPI.newEntityID(), DisguiseType.Zombie).addSingleData("noarmor"));
		else
		{
			unDisguise(player);
			disguise(player);
		}
	}
	
	@Override
	public boolean isDisguised(IPlayer player) {
		return this.dcAPI.isDisguised(player.getPlayer());
	}
	
	@Override
	public void unDisguise(IPlayer player) {
		this.dcAPI.undisguisePlayer(player.getPlayer());
	}
}
