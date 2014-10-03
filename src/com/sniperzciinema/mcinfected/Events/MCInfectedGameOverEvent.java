
package com.sniperzciinema.mcinfected.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class MCInfectedGameOverEvent extends Event {
	
	private static final HandlerList	handlers	= new HandlerList();
	
	public static HandlerList getHandlerList() {
		return MCInfectedGameOverEvent.handlers;
	}
	
	public MCInfectedGameOverEvent()
	{
	}
	
	@Override
	public HandlerList getHandlers() {
		return MCInfectedGameOverEvent.handlers;
	}
	
}
