
package com.sniperzciinema.mcinfected.Lobbys;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.sniperzciinema.mcinfected.McInfected;
import com.sniperzciinema.mcinfected.IPlayers.IPlayer;


public class LobbyManager {
	
	private ArrayList<Lobby>	lobbys;
	
	public LobbyManager()
	{
		this.lobbys = new ArrayList<Lobby>();
		
		loadLobbysFromFile();
		
	}
	
	/**
	 * Load all lobbys from the file
	 */
	public void loadLobbysFromFile() {
		if (McInfected.getFileManager().getLobbys().getConfigurationSection("") != null)
			for (String lobbyName : McInfected.getFileManager().getLobbys().getConfigurationSection("").getKeys(true))
				if (!lobbyName.contains("."))
				{
					McInfected.getPlugin().getLogger().log(Level.INFO, "Loaded Lobby: " + lobbyName);
					createLobby(lobbyName);
				}
	}
	
	/**
	 * Get all lobbys
	 * 
	 * @return
	 */
	public ArrayList<Lobby> getLobbys() {
		return this.lobbys;
	}
	
	public ArrayList<Lobby> getValidLobbys() {
		ArrayList<Lobby> list = new ArrayList<Lobby>();
		for (Lobby lobby : lobbys)
			if (lobby.getLocation() != null && !lobby.getArenaManager().getValidArenas().isEmpty())
				list.add(lobby);
		
		return list;
	}
	
	/**
	 * Is name a lobby
	 * 
	 * @param name
	 * @return
	 */
	public boolean isLobby(String name) {
		return getLobby(name) != null;
	}
	
	/**
	 * Get the lobby by name
	 * 
	 * @param name
	 * @return
	 */
	public Lobby getLobby(String name) {
		for (Lobby lobby : lobbys)
			
			if (lobby.getName().equalsIgnoreCase(name))
				return lobby;
		return null;
		
	}
	
	/**
	 * Get the lobby the players in
	 * 
	 * @param player
	 * @return
	 */
	public Lobby getLobby(Player player) {
		for (Lobby lobby : lobbys)
			
			if (lobby.isIPlayer(player))
				return lobby;
		return null;
	}
	
	public Lobby createLobby(String lobbyName) {
		Lobby lobby = new Lobby(lobbyName);
		this.lobbys.add(lobby);
		return lobby;
	}
	
	/**
	 * Is the player in a lobby
	 * 
	 * @param player
	 * @return
	 */
	public boolean isPlaying(Player player) {
		return getLobby(player) != null;
	}
	
	public IPlayer getIPlayer(Player player) {
		return getLobby(player).getIPlayer(player);
	}
	
}
