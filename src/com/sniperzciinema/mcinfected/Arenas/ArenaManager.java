
package com.sniperzciinema.mcinfected.Arenas;

import java.util.ArrayList;
import java.util.logging.Level;

import com.sniperzciinema.mcinfected.McInfected;
import com.sniperzciinema.mcinfected.Lobbys.Lobby;
import com.sniperzciinema.mcinfected.Utils.StringUtil;


public class ArenaManager {
	
	
	public Lobby getLobby() {
		return lobby;
	}

	private ArrayList<Arena>	arenas;
	private Lobby lobby;
	
	
	public ArenaManager(Lobby lobby)
	{
		this.lobby = lobby;
		this.arenas = new ArrayList<Arena>();
		
		loadArenasFromFile();
		
	}
	
	/**
	 * @param arena
	 *          the arena to be created
	 */
	public void createArena(String arena) {
		arena = StringUtil.getCapitalized(arena);
		this.arenas.add(new Arena(arena, this));
	}
	
	/**
	 * Unload and Remove the arena from the Arenas.yml
	 * 
	 * @param arena
	 */
	public void deleteArena(Arena arena) {
		unloadArena(arena);
		McInfected.getFileManager().getLobbys().set(arena.getName(), null);
		McInfected.getFileManager().saveLobbys();
	}
	
	/**
	 * unload and remove the arena from the arenas.yml
	 * 
	 * @param arena
	 */
	public void deleteArena(String arena) {
		deleteArena(getArena(arena));
	}
	
	/**
	 * @param arenaName
	 * @return the arena by the name
	 */
	public Arena getArena(String arenaName) {
		arenaName = StringUtil.getCapitalized(arenaName);
		for (Arena arena : this.arenas)
			if (arena.getName().equalsIgnoreCase(arenaName))
				return arena;
		
		return null;
	}
	
	/**
	 * @return the arenas
	 */
	public ArrayList<Arena> getArenas() {
		return this.arenas;
	}
	
	public ArrayList<Arena> getInValidArenas() {
		ArrayList<Arena> invalidArenas = new ArrayList<Arena>();
		
		for (Arena arena : this.arenas)
			if (arena.getSpawns().isEmpty())
				invalidArenas.add(arena);
		
		return invalidArenas;
	}
	
	/**
	 * @return the valid arenas
	 */
	public ArrayList<Arena> getValidArenas() {
		ArrayList<Arena> validArenas = new ArrayList<Arena>();
		
		for (Arena arena : this.arenas)
			if (!arena.getSpawns().isEmpty())
				validArenas.add(arena);
		
		return validArenas;
	}
	
	public boolean isArena(String arenaName) {
		arenaName = StringUtil.getCapitalized(arenaName);
		return getArena(arenaName) != null;
	}
	
	/**
	 * Load all the arenas from a the Config.yml
	 */
	public void loadArenasFromFile() {
		if (McInfected.getFileManager().getLobbys().getConfigurationSection(lobby.getName()+".Arenas") != null)
			for (String arenaName : McInfected.getFileManager().getLobbys().getConfigurationSection(lobby.getName()+".Arenas").getKeys(true))
				if (!arenaName.contains("."))
				{
					McInfected.getPlugin().getLogger().log(Level.INFO, lobby.getName()+" - Loaded Arena: " + arenaName);
					createArena(arenaName);
				}
		
	}
	
	/**
	 * Unload the arena from the array
	 * 
	 * @param arena
	 */
	public void unloadArena(Arena arena) {
		this.arenas.remove(arena);
	}
	
	/**
	 * Unload the arena from the array
	 * 
	 * @param arena
	 */
	public void unloadArena(String arena) {
		unloadArena(getArena(arena));
	}
	
}
