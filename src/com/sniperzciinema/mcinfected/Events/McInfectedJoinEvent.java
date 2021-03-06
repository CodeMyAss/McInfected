
package com.sniperzciinema.mcinfected.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class McInfectedJoinEvent extends Event implements Cancellable {
	
	private static final HandlerList	handlers	= new HandlerList();
	
	public static HandlerList getHandlerList() {
		return McInfectedJoinEvent.handlers;
	}
	
	private boolean	canceled;
	
	private Player	player;
	
	public McInfectedJoinEvent(Player player)
	{
		this.player = player;
	}
	
	@Override
	public HandlerList getHandlers() {
		return McInfectedJoinEvent.handlers;
	}
	
	
	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return this.player;
	}
	
	@Override
	public boolean isCancelled() {
		return this.canceled;
	}
	
	@Override
	public void setCancelled(boolean canceled) {
		this.canceled = canceled;
	}
	
}
