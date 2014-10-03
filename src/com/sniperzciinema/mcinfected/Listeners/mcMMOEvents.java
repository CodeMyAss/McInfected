
package com.sniperzciinema.mcinfected.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.gmail.nossr50.events.fake.FakeEntityDamageByEntityEvent;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityActivateEvent;
import com.gmail.nossr50.events.skills.unarmed.McMMOPlayerDisarmEvent;
import com.sniperzciinema.mcinfected.McInfected;
import com.sniperzciinema.mcinfected.Lobbys.Lobby;


/**
 * The mcMMO Api Listener
 */
public class mcMMOEvents implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void mcMMOAbilityActivate(McMMOPlayerAbilityActivateEvent e) {
		if (McInfected.getLobbyManager().isPlaying(e.getPlayer()))
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void mcMMODisarm(McMMOPlayerDisarmEvent e) {
		if (McInfected.getLobbyManager().isPlaying(e.getDefender()))
		{
			Lobby lobby = McInfected.getLobbyManager().getLobby(e.getDefender());
			if (lobby.isIPlayer(e.getPlayer()) && lobby.isIPlayer(e.getDefender()))
				e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void mcMMOExtraDamage(FakeEntityDamageByEntityEvent e) {
		if ((e.getEntity() instanceof Player) && (e.getDamager() instanceof Player))
			if (McInfected.getLobbyManager().isPlaying((Player)e.getEntity()))
			{
				Lobby lobby = McInfected.getLobbyManager().getLobby((Player)e.getEntity());
				if (lobby.isIPlayer((Player) e.getEntity()) && lobby.isIPlayer((Player) e.getDamager()))
				{
					e.setDamage(0);
					e.setCancelled(true);
				}
			}
		
	}
	
}
