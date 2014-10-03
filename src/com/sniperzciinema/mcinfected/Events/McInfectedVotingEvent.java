
package com.sniperzciinema.mcinfected.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class McInfectedVotingEvent extends Event {
	
	private static final HandlerList	handlers	= new HandlerList();
	
	public static HandlerList getHandlerList() {
		return McInfectedVotingEvent.handlers;
	}
	
	public McInfectedVotingEvent()
	{
	}
	
	@Override
	public HandlerList getHandlers() {
		return McInfectedVotingEvent.handlers;
	}
	
	
}
