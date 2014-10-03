
package com.sniperzciinema.mcinfected.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class McInfectedInfectingEvent extends Event {
	
	private static final HandlerList	handlers	= new HandlerList();
	
	public static HandlerList getHandlerList() {
		return McInfectedInfectingEvent.handlers;
	}
	
	public McInfectedInfectingEvent()
	{
	}
	
	@Override
	public HandlerList getHandlers() {
		return McInfectedInfectingEvent.handlers;
	}
	
}
