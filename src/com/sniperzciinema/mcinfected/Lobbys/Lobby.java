
package com.sniperzciinema.mcinfected.Lobbys;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.entity.Player;

import com.sniperzciinema.mcinfected.McInfected;
import com.sniperzciinema.mcinfected.Timers;
import com.sniperzciinema.mcinfected.Arenas.Arena;
import com.sniperzciinema.mcinfected.Arenas.ArenaManager;
import com.sniperzciinema.mcinfected.IPlayers.IPlayer;
import com.sniperzciinema.mcinfected.IPlayers.IPlayer.Team;
import com.sniperzciinema.mcinfected.Utils.Coord;


public class Lobby {
	
	
	public ArenaManager getArenaManager() {
		return arenaManager;
	}

	public static enum GameState
	{
		Game, GameOver, Infecting, InLobby, PreGame, Voting;
	}
	
	private String							name;
	
	private GameState						gamestate;
	
	private Coord								location;
	
	private Coord								leaveLocation;
	
	private ArrayList<IPlayer>	players;
	
	private Arena								playingIn;
	private Timers							timers;
	private ArenaManager				arenaManager;
	
	public Lobby(String name)
	{
		this.name = name;
		this.arenaManager = new ArenaManager(this);
		
		if (McInfected.getFileManager().getLobbys().getString(getName() + ".Location") != null)
			this.location = new Coord(McInfected.getFileManager().getLobbys().getString(getName() + ".Location"));
		if (McInfected.getFileManager().getLobbys().getString(getName() + ".Leave Location") != null)
			this.leaveLocation = new Coord(McInfected.getFileManager().getLobbys().getString(getName() + ".Leave Location"));
		
		this.timers = new Timers(this);
		
		this.players = new ArrayList<IPlayer>();
		
		setGamestate(GameState.InLobby);
	}
	/**
	 * @param iPlayer
	 *          the iPlayer to add
	 */
	public void addIPlayer(IPlayer iPlayer) {
		this.players.add(iPlayer);
	}
	/**
	 * @param player
	 *          the player to add
	 */
	public IPlayer addIPlayer(Player player) {
		IPlayer iPlayer = new IPlayer(this, player);
		addIPlayer(iPlayer);
		return iPlayer;
	}
	
	public GameState getGamestate() {
		return gamestate;
	}
	
	/**
	 * @return the gamestate
	 */
	public GameState getGameState() {
		return this.gamestate;
	}
	
	/**
	 * @return a list of humans
	 */
	public ArrayList<IPlayer> getHumans() {
		ArrayList<IPlayer> humans = new ArrayList<IPlayer>();
		for (IPlayer player : this.players)
			if (player.getTeam() == Team.Human)
				humans.add(player);
		return humans;
	}
	
	/**
	 * @param player
	 * @return the iPlayer from the player
	 */
	public IPlayer getIPlayer(Player player) {
		for (IPlayer iPlayer : this.players)
			if (iPlayer.getPlayer() == player)
				return iPlayer;
		return null;
	}
	
	/**
	 * @return the iPlayers
	 */
	public ArrayList<IPlayer> getIPlayers() {
		return this.players;
	}
	
	/**
	 * @return the leave location
	 */
	public Coord getLeaveLocation() {
		return this.leaveLocation;
	}
	
	/**
	 * @return the location
	 */
	public Coord getLocation() {
		return this.location;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<IPlayer> getPlayers() {
		return players;
	}
	
	/**
	 * @return the playingIn
	 */
	public Arena getPlayingIn() {
		return this.playingIn;
	}
	
	/**
	 * @return the timers
	 */
	public Timers getTimers() {
		return this.timers;
	}
	
	/**
	 * @return a list of zombies
	 */
	public ArrayList<IPlayer> getZombies() {
		ArrayList<IPlayer> zombies = new ArrayList<IPlayer>();
		for (IPlayer player : this.players)
			if (player.getTeam() == Team.Zombie)
				zombies.add(player);
		return zombies;
	}
	
	/**
	 * @param player
	 * @return if the player is playing
	 */
	public boolean isIPlayer(Player player) {
		return getIPlayer(player) != null;
	}
	
	/**
	 * @return if gamestate is "Game", "GameOver", "Infecting"
	 */
	public boolean isStarted() {
		return (this.gamestate == GameState.Game) || (this.gamestate == GameState.GameOver) || (this.gamestate == GameState.Infecting);
	}
	
	/**
	 * @param iPlayer
	 *          the iPlayer to remove
	 */
	public void removeIPlayer(IPlayer iPlayer) {
		this.players.remove(iPlayer);
	}
	
	/**
	 * @param iPlayer
	 *          the player to remove
	 */
	public void removeIPlayer(Player player) {
		this.players.remove(getIPlayer(player));
	}
	
	/**
	 * @param gamestate
	 *          the gamestate to set
	 */
	public void setGamestate(GameState gamestate) {
		this.gamestate = gamestate;
	}
	
	/**
	 * Set the lobbys leave location
	 * 
	 * @param coord
	 */
	public void setLeaveLocation(Coord coord) {
		McInfected.getFileManager().getLobbys().set(getName()+".Leave Location", coord.asString());
		McInfected.getFileManager().saveLobbys();
		this.leaveLocation = coord;
	}
	
	/**
	 * Set the lobbys location
	 * 
	 * @param coord
	 */
	public void setLocation(Coord coord) {
		McInfected.getFileManager().getLobbys().set(getName()+".Location", coord.asString());
		McInfected.getFileManager().saveLobbys();
		this.location = coord;
	}
	
	/**
	 * @param playingIn
	 *          the playingIn to set
	 */
	public void setPlayingIn(Arena playingIn) {
		this.playingIn = playingIn;
	}
	
	/**
	 * Teleport the iPlayer to the lobby's location
	 * 
	 * @param iPlayer
	 */
	public void teleport(IPlayer iPlayer) {
		iPlayer.getPlayer().teleport(this.location.asLocation());
	}
	
	/**
	 * Teleport all iPlayers to the lobby's location
	 */
	public void teleportAll() {
		for (IPlayer iPlayer : getIPlayers())
			iPlayer.getPlayer().teleport(this.location.asLocation());
	}
	
	/**
	 * Teleport the player to the selected arena
	 * 
	 * @param player
	 * @param arena
	 */
	public void teleportToArena(IPlayer iPlayer, Arena arena) {
		Random r = new Random(System.currentTimeMillis());
		int i = r.nextInt(arena.getSpawns(iPlayer.getTeam()).size());
		iPlayer.getPlayer().teleport(arena.getSpawns(iPlayer.getTeam()).get(i).asLocation());
	}
	
}
