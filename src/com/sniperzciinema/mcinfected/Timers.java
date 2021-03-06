
package com.sniperzciinema.mcinfected;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;

import com.sniperzciinema.mcinfected.Messanger.Messages;
import com.sniperzciinema.mcinfected.Arenas.Arena;
import com.sniperzciinema.mcinfected.Events.MCInfectedGameOverEvent;
import com.sniperzciinema.mcinfected.Events.McInfectedGameEvent;
import com.sniperzciinema.mcinfected.Events.McInfectedInfectEvent;
import com.sniperzciinema.mcinfected.Events.McInfectedInfectingEvent;
import com.sniperzciinema.mcinfected.Events.McInfectedPreGameEvent;
import com.sniperzciinema.mcinfected.Events.McInfectedVotingEvent;
import com.sniperzciinema.mcinfected.IPlayers.IPlayer;
import com.sniperzciinema.mcinfected.IPlayers.IPlayer.Team;
import com.sniperzciinema.mcinfected.Lobbys.Lobby;
import com.sniperzciinema.mcinfected.Lobbys.Lobby.GameState;
import com.sniperzciinema.mcinfected.Utils.PictureUtil;
import com.sniperzciinema.mcinfected.Utils.StringUtil;


public class Timers {
	
	private int		timeLeft, timeLimit;
	private int		voting, pregame, infecting, game, gameover;
	private Lobby lobby;
	
	public Timers(Lobby lobby)
	{
		this.lobby = lobby;
	}
	public void chooseAlphas(int toInfect) {
		if (toInfect == 0)
			toInfect = 1;
		
		// y is just a safety net
		int y = 0;
		
		for (int i = 0; i != toInfect; i++)
		{
			IPlayer newZombie;
			do
			{
				Random random = new Random(System.currentTimeMillis());
				int x = random.nextInt(Timers.this.lobby.getIPlayers().size());
				newZombie = Timers.this.lobby.getIPlayers().get(x);
				
				// Have a safety just incase something goes horribly wrong
				y++;
				if (y == 50)
				{
					newZombie = null;
					break;
				}
				
			} while (newZombie.getTeam() == Team.Zombie);
			if (newZombie != null)
			{
				McInfectedInfectEvent event = new McInfectedInfectEvent(newZombie.getPlayer());
				Bukkit.getPluginManager().callEvent(event);
				if (!event.isCancelled())
				{
					
					for (IPlayer iPlayer : Timers.this.lobby.getIPlayers())
						if (iPlayer != newZombie)
							iPlayer.getPlayer().sendMessage(McInfected.getMessanger().getMessage(true, Messages.Game__Infected, "<player>", newZombie.getPlayer().getName()));
					
					newZombie.infect();
					
					String[] face = PictureUtil.getZombie();
					face[2] = face[2] + "     " + McInfected.getMessanger().getMessage(false, Messages.Picture__Infected__You);
					face[3] = face[3] + "     " + McInfected.getMessanger().getMessage(false, Messages.Picture__Infected__To_Win);
					newZombie.getPlayer().sendMessage(face);
				}
			}
			else
				i--;
		}
	}
	
	/**
	 * @return the arena with the most votes
	 */
	private Arena getMostVotedArena() {
		
		Arena mostVoted = null;
		int mostVotes = 0;
		
		for (Arena arena : lobby.getArenaManager().getValidArenas())
			if (arena.getVotes() > mostVotes)
			{
				mostVotes = arena.getVotes();
				mostVoted = arena;
			}
		
		if (mostVoted == null)
		{
			Random r = new Random(System.currentTimeMillis());
			int i = r.nextInt(lobby.getArenaManager().getValidArenas().size());
			mostVoted = lobby.getArenaManager().getValidArenas().get(i);
		}
		return mostVoted;
	}
	
	/**
	 * @return the time left
	 */
	public int getTimeLeft() {
		return this.timeLeft;
	}
	
	/**
	 * Run the end game timer
	 */
	public void startEndGame() {
		stop();
		
		MCInfectedGameOverEvent event = new MCInfectedGameOverEvent();
		Bukkit.getPluginManager().callEvent(event);
		
		Timers.this.lobby.setGamestate(GameState.GameOver);
		
		this.timeLimit = 5;
		this.timeLeft = this.timeLimit;
		
		for (IPlayer iPlayer : Timers.this.lobby.getIPlayers())
			iPlayer.getiScoreboard().update();
		
		McInfected.getMessanger().broadcast(lobby, McInfected.getMessanger().getMessage(true, Messages.Game__Over__Notification));
		
		this.gameover = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(McInfected.getPlugin(), new Runnable()
		{
			
			@Override
			public void run() {
				
				Timers.this.lobby.getPlayingIn().reset();
				for (IPlayer iPlayer : Timers.this.lobby.getIPlayers())
				{
					iPlayer.reset();
					Timers.this.lobby.teleport(iPlayer);
				}
				Timers.this.lobby.setGamestate(GameState.InLobby);
			}
		}, 20 * 5);
		
	}
	
	/**
	 * Start Game
	 */
	public void startGame() {
		
		stop();
		McInfectedGameEvent event = new McInfectedGameEvent();
		Bukkit.getPluginManager().callEvent(event);
		
		Timers.this.lobby.setGamestate(GameState.Game);
		
		this.timeLimit = McInfected.getSettings().getTimeGame();
		this.timeLeft = this.timeLimit;
		
		for (IPlayer iPlayer : Timers.this.lobby.getIPlayers())
		{
			iPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
			iPlayer.equip();
			iPlayer.getiScoreboard().update();
		}
		
		this.game = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(McInfected.getPlugin(), new Runnable()
		{
			
			@Override
			public void run() {
				// Check the time, if it's not 0, subtract 1
				if (Timers.this.timeLeft != 0)
				{
					Timers.this.timeLeft -= 1;
					for (IPlayer iPlayer : Timers.this.lobby.getIPlayers())
						iPlayer.getPlayer().setLevel(Timers.this.timeLeft);
					
					// Display time if it's a certain value
					if ((Timers.this.timeLeft == ((Timers.this.timeLimit / 4) * 3)) || (Timers.this.timeLeft == (Timers.this.timeLimit / 2)) || (Timers.this.timeLeft == (Timers.this.timeLimit / 4)) || (Timers.this.timeLeft == 60) || (Timers.this.timeLeft == 10) || (Timers.this.timeLeft == 9) || (Timers.this.timeLeft == 8) || (Timers.this.timeLeft == 7) || (Timers.this.timeLeft == 6) || (Timers.this.timeLeft == 5) || (Timers.this.timeLeft == 4) || (Timers.this.timeLeft == 3) || (Timers.this.timeLeft == 2))
						for (IPlayer iPlayer : Timers.this.lobby.getIPlayers())
						{
							iPlayer.getPlayer().sendMessage(McInfected.getMessanger().getMessage(true, Messages.Game__Time__Game, "<time>", StringUtil.getTime(Timers.this.timeLeft)));
							iPlayer.getPlayer().playSound(iPlayer.getPlayer().getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
						}
					else if (Timers.this.timeLeft == 1)
						for (IPlayer iPlayer : Timers.this.lobby.getIPlayers())
						{
							iPlayer.getPlayer().sendMessage(McInfected.getMessanger().getMessage(true, Messages.Game__Time__Game, "<time>", StringUtil.getTime(Timers.this.timeLeft)));
							iPlayer.getPlayer().playSound(iPlayer.getPlayer().getLocation(), Sound.NOTE_BASS_DRUM, 1, 5);
						}
				}
				// GAME ENDS
				else if (Timers.this.timeLeft == 0)
				{
					McInfected.getMessanger().broadcast(lobby, McInfected.getMessanger().getMessage(true, Messages.Game__Over__Humans_Win));
					startEndGame();
				}
			}
		}, 0L, 20L);
	}
	
	public void startInfecting() {
		
		stop();
		
		McInfectedInfectingEvent event = new McInfectedInfectingEvent();
		Bukkit.getPluginManager().callEvent(event);
		
		Timers.this.lobby.setGamestate(GameState.Infecting);
		
		this.timeLimit = McInfected.getSettings().getTimeInfecting();
		this.timeLeft = this.timeLimit;
		
		for (IPlayer iPlayer : Timers.this.lobby.getIPlayers())
		{
			iPlayer.getPlayer().sendMessage(McInfected.getMessanger().getMessage(true, Messages.Game__Infecting));
			
			if (iPlayer.getHumanKit() == null)
				iPlayer.setKit(Team.Human, iPlayer.chooseRandomKit(Team.Human));
			
			if (iPlayer.getZombieKit() == null)
				iPlayer.setKit(Team.Zombie, iPlayer.chooseRandomKit(Team.Zombie));
			
			iPlayer.getiScoreboard().update();
			iPlayer.equip();
			Timers.this.lobby.teleportToArena(iPlayer, Timers.this.lobby.getPlayingIn());
			
		}
		this.infecting = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(McInfected.getPlugin(), new Runnable()
		{
			
			@Override
			public void run() {
				// Check the time, if it's not 0, subtract 1
				if (Timers.this.timeLeft != 0)
				{
					Timers.this.timeLeft -= 1;
					for (IPlayer iPlayer : Timers.this.lobby.getIPlayers())
						iPlayer.getPlayer().setLevel(Timers.this.timeLeft);
					
					// Display time if it's a certain value
					if ((Timers.this.timeLeft == 10) || (Timers.this.timeLeft == 9) || (Timers.this.timeLeft == 8) || (Timers.this.timeLeft == 7) || (Timers.this.timeLeft == 6) || (Timers.this.timeLeft == 5) || (Timers.this.timeLeft == 4) || (Timers.this.timeLeft == 3) || (Timers.this.timeLeft == 2))
						for (IPlayer iPlayer : Timers.this.lobby.getIPlayers())
						{
							iPlayer.getPlayer().playSound(iPlayer.getPlayer().getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
							iPlayer.getPlayer().sendMessage(McInfected.getMessanger().getMessage(true, Messages.Game__Time__Infecting, "<time>", StringUtil.getTime(Timers.this.timeLeft)));
						}
					else if (Timers.this.timeLeft == 1)
						for (IPlayer iPlayer : Timers.this.lobby.getIPlayers())
						{
							iPlayer.getPlayer().sendMessage(McInfected.getMessanger().getMessage(true, Messages.Game__Time__Infecting, "<time>", StringUtil.getTime(Timers.this.timeLeft)));
							iPlayer.getPlayer().playSound(iPlayer.getPlayer().getLocation(), Sound.NOTE_BASS_DRUM, 1, 5);
						}
				}
				else if (Timers.this.timeLeft == 0)
				{
					chooseAlphas((int) (Timers.this.lobby.getIPlayers().size() * (McInfected.getSettings().getGameInfectingAlphaPercent())));
					
					String[] face = PictureUtil.getHuman();
					face[2] = face[2] + "     " + McInfected.getMessanger().getMessage(false, Messages.Picture__Human__You);
					face[3] = face[3] + "     " + McInfected.getMessanger().getMessage(false, Messages.Picture__Human__To_Win);
					
					for (IPlayer iPlayer : Timers.this.lobby.getHumans())
						iPlayer.getPlayer().sendMessage(face);
					
					startGame();
				}
			}
		}, 0L, 20L);
	}
	
	/**
	 * Start the Pre-Game timer
	 */
	public void startPreGame() {
		stop();
		
		McInfectedPreGameEvent event = new McInfectedPreGameEvent();
		Bukkit.getPluginManager().callEvent(event);
		
		Timers.this.lobby.setGamestate(GameState.PreGame);
		
		this.timeLimit = McInfected.getSettings().getTimePreGame();
		this.timeLeft = this.timeLimit;
		
		for (IPlayer iPlayer : Timers.this.lobby.getIPlayers())
		{
			iPlayer.getPlayer().sendMessage(McInfected.getMessanger().getMessage(true, Messages.Game__Arena__Chosen, "<arena>", Timers.this.lobby.getPlayingIn().getName()));
			
			iPlayer.getPlayer().sendMessage(McInfected.getMessanger().getMessage(true, Messages.Game__Time__PreGame, "<time>", StringUtil.getTime(this.timeLeft)));
			iPlayer.getiScoreboard().update();
		}
		this.pregame = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(McInfected.getPlugin(), new Runnable()
		{
			
			@Override
			public void run() {
				startInfecting();
			}
		}, 20 * McInfected.getSettings().getTimePreGame());
		
	}
	
	/**
	 * Start Game timer
	 */
	public void startVoting() {
		stop();
		McInfectedVotingEvent event = new McInfectedVotingEvent();
		Bukkit.getPluginManager().callEvent(event);
		
		Timers.this.lobby.setGamestate(GameState.Voting);
		
		this.timeLimit = McInfected.getSettings().getTimeVoting();
		this.timeLeft = this.timeLimit;
		
		for (IPlayer iPlayer : Timers.this.lobby.getIPlayers())
			iPlayer.getiScoreboard().update();
		
		McInfected.getMessanger().broadcast(lobby, McInfected.getMessanger().getMessage(true, Messages.Game__Voting));
		this.voting = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(McInfected.getPlugin(), new Runnable()
		{
			
			@Override
			public void run() {
				// Check the time, if it's not 0, subtract 1
				if (Timers.this.timeLeft != 0)
				{
					Timers.this.timeLeft -= 1;
					for (IPlayer iPlayer : Timers.this.lobby.getIPlayers())
						iPlayer.getPlayer().setLevel(Timers.this.timeLeft);
					
					// Display time if it's a certain value
					if ((Timers.this.timeLeft == 10) || (Timers.this.timeLeft == 9) || (Timers.this.timeLeft == 8) || (Timers.this.timeLeft == 7) || (Timers.this.timeLeft == 6) || (Timers.this.timeLeft == 5) || (Timers.this.timeLeft == 4) || (Timers.this.timeLeft == 3) || (Timers.this.timeLeft == 2))
						for (IPlayer iPlayer : Timers.this.lobby.getIPlayers())
						{
							iPlayer.getPlayer().playSound(iPlayer.getPlayer().getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
							iPlayer.getPlayer().sendMessage(McInfected.getMessanger().getMessage(true, Messages.Game__Time__Voting, "<time>", StringUtil.getTime(Timers.this.timeLeft)));
						}
					else if (Timers.this.timeLeft == 1)
						for (IPlayer iPlayer : Timers.this.lobby.getIPlayers())
						{
							iPlayer.getPlayer().sendMessage(McInfected.getMessanger().getMessage(true, Messages.Game__Time__Voting, "<time>", StringUtil.getTime(Timers.this.timeLeft)));
							iPlayer.getPlayer().playSound(iPlayer.getPlayer().getLocation(), Sound.NOTE_BASS_DRUM, 1, 5);
						}
				}
				else if (Timers.this.timeLeft == 0)
				{
					Timers.this.lobby.setPlayingIn(getMostVotedArena());
					startPreGame();
				}
			}
		}, 0L, 20L);
	}
	
	/**
	 * Stop all timers
	 */
	public void stop() {
		Bukkit.getScheduler().cancelTask(this.pregame);
		Bukkit.getScheduler().cancelTask(this.voting);
		Bukkit.getScheduler().cancelTask(this.game);
		Bukkit.getScheduler().cancelTask(this.infecting);
		Bukkit.getScheduler().cancelTask(this.gameover);
	}
	
}
