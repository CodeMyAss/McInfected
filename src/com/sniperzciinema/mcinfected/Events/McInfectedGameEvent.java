
package com.sniperzciinema.mcinfected.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class McInfectedGameEvent extends Event {
	
	private static final HandlerList	handlers	= new HandlerList();
	
	public static HandlerList getHandlerList() {
		return McInfectedGameEvent.handlers;
	}
	
	public McInfectedGameEvent()
	{
	}
	
	@Override
	public HandlerList getHandlers() {
		return McInfectedGameEvent.handlers;
	}
	
	
}
