
package com.sniperzciinema.mcinfected.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class McInfectedPreGameEvent extends Event {
	
	private static final HandlerList	handlers	= new HandlerList();
	
	public static HandlerList getHandlerList() {
		return McInfectedPreGameEvent.handlers;
	}
	
	public McInfectedPreGameEvent()
	{
	}
	
	@Override
	public HandlerList getHandlers() {
		return McInfectedPreGameEvent.handlers;
	}
	
}
