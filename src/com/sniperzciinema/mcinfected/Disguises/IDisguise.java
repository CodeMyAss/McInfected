
package com.sniperzciinema.mcinfected.Disguises;

import org.bukkit.Bukkit;

import com.sniperzciinema.mcinfected.IPlayers.IPlayer;

import de.robingrether.idisguise.iDisguise;
import de.robingrether.idisguise.api.DisguiseAPI;
import de.robingrether.idisguise.disguise.DisguiseType;
import de.robingrether.idisguise.disguise.MobDisguise;


public class IDisguise extends Disguises {
	
	private iDisguise		id		= ((iDisguise) Bukkit.getServer().getPluginManager().getPlugin("iDisguise"));
	public DisguiseAPI	idAPI	= this.id.getAPI();
	
	public IDisguise()
	{
		super("iDisguise");
		for (DisguiseType disguise : DisguiseType.values())
			this.disguiseList.add(disguise.toString());
	}
	
	@Override
	public void disguise(IPlayer player) {
		
		if (!isDisguised(player))
			if (DisguiseType.valueOf(player.getKit(player.getTeam()).getDisguise().toUpperCase()) != null)
			{
				MobDisguise disguise = new MobDisguise(DisguiseType.valueOf(player.getKit(player.getTeam()).getDisguise().toUpperCase()), true);
				this.idAPI.disguiseToAll(player.getPlayer(), disguise);
			}
			else
				this.idAPI.disguiseToAll(player.getPlayer(), new MobDisguise(DisguiseType.ZOMBIE, true));
		else
		{
			unDisguise(player);
			disguise(player);
		}
	}
	
	@Override
	public boolean isDisguised(IPlayer player) {
		return this.idAPI.isDisguised(player.getPlayer());
	}
	
	@Override
	public void unDisguise(IPlayer player) {
		this.idAPI.undisguiseToAll(player.getPlayer());
	}
	
}
