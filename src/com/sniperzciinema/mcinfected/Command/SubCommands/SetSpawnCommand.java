
package com.sniperzciinema.mcinfected.Command.SubCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sniperzciinema.mcinfected.McInfected;
import com.sniperzciinema.mcinfected.Lobbys.Lobby;
import com.sniperzciinema.mcinfected.Messanger.Messages;
import com.sniperzciinema.mcinfected.Arenas.Arena;
import com.sniperzciinema.mcinfected.Command.SubCommand;
import com.sniperzciinema.mcinfected.Command.FancyMessages.FancyMessage;
import com.sniperzciinema.mcinfected.IPlayers.IPlayer.Team;
import com.sniperzciinema.mcinfected.Utils.Coord;
import com.sniperzciinema.mcinfected.Utils.StringUtil;


public class SetSpawnCommand extends SubCommand {
	
	public SetSpawnCommand()
	{
		super("SetSpawn");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		
		if (!(sender instanceof Player))
			sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Command__Not_A_Player));
		
		else if (!sender.hasPermission(getPermission()))
			sender.sendMessage(messanger.getMessage(true, Messages.Error__Misc__Invalid_Permission));
		
		else if ((args.length >= 3))
		{
			if (McInfected.getLobbyManager().isLobby(args[1]))
			{
				Lobby lobby = McInfected.getLobbyManager().getLobby(args[1]);
				
				if (lobby.getArenaManager().isArena(args[2]))
				{
					Arena arena = lobby.getArenaManager().getArena(args[2]);
					
					Team team = Team.Global;
					if (args.length == 3)
						team = Team.valueOf(StringUtil.getCapitalized(args[2]));
					
					Coord coord = new Coord(((Player) sender).getLocation());
					
					switch (team)
					{
						case Human:
							arena.addHumanSpawn(coord);
							break;
						
						case Zombie:
							arena.addZombieSpawn(coord);
							break;
						
						default:
							arena.addSpawn(coord);
							break;
					}
					sender.sendMessage(getMessanger().getMessage(true, Messages.Command__SetSpawn, "<team>", team.toString()));
				}
				else
					sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Arena__Doesnt_Exit));
			}
			else
				sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Lobby__Doesnt_Exist));
			
		}
		else
			getFancyMessage().send((Player) sender);
	}
	
	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "ss" });
	}
	
	@Override
	public FancyMessage getFancyMessage() {
		return new FancyMessage(getHelpMessage()).tooltip("SetSpawn", " ", "§eSet an Arenas spawns", "Set the possible spawns", "Set spawns per team, by saying what team", "§f§l/McInfected SetSpawn <Lobby> <Arena> [Human/Zombie]").suggest("/McInfected SetSpawn <Lobby> <Arena>");
	}
	
	@Override
	public String getHelpMessage() {
		return getMessanger().getMessage(false, Messages.Format__Prefix) + "§7/McInfected §oSetSpawn <Lobby> <Arena> [Human/Zombie]";
	}
	
	@Override
	public String getPermission() {
		return "McInfected.Setup.SetSpawn";
	}
	
	@Override
	public List<String> getTabs(String[] args) {
		if (args.length == 1)
		{
			ArrayList<String> lobbys = new ArrayList<String>();
			for (Lobby lobby : McInfected.getLobbyManager().getLobbys())
				lobbys.add(lobby.getName());
			
			return lobbys;
		}
		else if (args.length == 2)
		{
			ArrayList<String> arenas = new ArrayList<String>();
			for (Arena arena : McInfected.getLobbyManager().getLobby(args[4]).getArenaManager().getArenas())
				arenas.add(arena.getName());
			
			return arenas;
		}
		else if (args.length == 3)
			return Arrays.asList(new String[] { " ", "Human", "Zombie" });
		else
			return Arrays.asList(new String[] { "" });
	}
}
