
package com.sniperzciinema.mcinfected.Disguises;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;

import com.sniperzciinema.mcinfected.IPlayers.IPlayer;


public class LibsDisguises extends Disguises {
	
	public LibsDisguises()
	{
		super("LibsDisguises");
		for (DisguiseType disguise : DisguiseType.values())
			this.disguiseList.add(disguise.toString());
	}
	
	@Override
	public void disguise(IPlayer player) {
		
		if (!isDisguised(player))
			if (DisguiseType.valueOf(player.getKit(player.getTeam()).getDisguise().toUpperCase()) != null)
			{
				MobDisguise disguise = new MobDisguise(DisguiseType.valueOf(player.getKit(player.getTeam()).getDisguise().toUpperCase()), true);
				DisguiseAPI.disguiseToAll(player.getPlayer(), disguise);
			}
			else
				DisguiseAPI.disguiseToAll(player.getPlayer(), new MobDisguise(DisguiseType.ZOMBIE, true));
		else
		{
			unDisguise(player);
			disguise(player);
		}
	}
	
	@Override
	public boolean isDisguised(IPlayer player) {
		return DisguiseAPI.isDisguised(player.getPlayer());
	}
	
	@Override
	public void unDisguise(IPlayer player) {
		DisguiseAPI.undisguiseToAll(player.getPlayer());
	}
	
}
