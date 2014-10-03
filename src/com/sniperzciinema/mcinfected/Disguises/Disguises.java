
package com.sniperzciinema.mcinfected.Disguises;

import java.util.ArrayList;

import com.sniperzciinema.mcinfected.IPlayers.IPlayer;


public abstract class Disguises {
	
	public ArrayList<String>	disguiseList	= new ArrayList<String>();
	
	private String						name;
	
	public Disguises(String name)
	{
		this.name = name;
	}
	
	/**
	 * Disguise the player
	 * <p>
	 * Looks at class's disguise
	 * 
	 * @param player
	 */
	public abstract void disguise(IPlayer player);
	
	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @param player
	 * @return if the player is disguised
	 */
	public abstract boolean isDisguised(IPlayer player);
	
	/**
	 * Undisguises the player
	 * 
	 * @param player
	 */
	public abstract void unDisguise(IPlayer player);
	
}
