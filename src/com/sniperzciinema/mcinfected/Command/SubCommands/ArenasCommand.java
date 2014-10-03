
package com.sniperzciinema.mcinfected.Command.SubCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sniperzciinema.mcinfected.McInfected;
import com.sniperzciinema.mcinfected.Messanger.Messages;
import com.sniperzciinema.mcinfected.Arenas.Arena;
import com.sniperzciinema.mcinfected.Command.SubCommand;
import com.sniperzciinema.mcinfected.Command.FancyMessages.FancyMessage;
import com.sniperzciinema.mcinfected.Lobbys.Lobby;


public class ArenasCommand extends SubCommand {
	
	public ArenasCommand()
	{
		super("Arenas");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (!sender.hasPermission(getPermission()))
			sender.sendMessage(messanger.getMessage(true, Messages.Error__Misc__Invalid_Permission));
		
		else
		{
			if (args.length == 2)
			{
				if (McInfected.getLobbyManager().isLobby(args[1]))
				{
					Lobby lobby = McInfected.getLobbyManager().getLobby(args[1]);
					StringBuilder valid = new StringBuilder();
					ArrayList<Arena> validArenas = lobby.getArenaManager().getValidArenas();
					for (Arena a : validArenas)
					{
						valid.append(a.getName());
						if ((validArenas.size() > 1) && (validArenas.get(validArenas.size() - 1) != a))
							valid.append(", ");
					}
					
					StringBuilder inValid = new StringBuilder();
					ArrayList<Arena> invalidArenas = lobby.getArenaManager().getInValidArenas();
					for (Arena a : invalidArenas)
					{
						inValid.append(a.getName());
						if ((invalidArenas.size() > 1) && (invalidArenas.get(invalidArenas.size() - 1) != a))
							inValid.append(", ");
					}
					
					sender.sendMessage(messanger.getHeader("McInfected Arenas"));
					if (!lobby.getArenaManager().getValidArenas().isEmpty())
						sender.sendMessage("" + ChatColor.WHITE + ChatColor.BOLD + "Valid: " + ChatColor.GREEN + valid.toString());
					if (!lobby.getArenaManager().getInValidArenas().isEmpty())
						sender.sendMessage("" + ChatColor.WHITE + ChatColor.BOLD + "Not Valid: " + ChatColor.RED + inValid.toString());
					
				}
				else
					sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Lobby__Doesnt_Exist));
			}else
				getFancyMessage().send((Player)sender);
		}
		
	}
	
	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "maps" });
	}
	
	@Override
	public FancyMessage getFancyMessage() {
		return new FancyMessage(getHelpMessage()).tooltip("Arenas", " ", "§eList all Arenas", " ", "§f§l/McInfected Arenas <Lobby>").suggest("/McInfected Arenas <Lobby>");
	}
	
	@Override
	public String getHelpMessage() {
		return getMessanger().getMessage(false, Messages.Format__Prefix) + "§7/McInfected §oArenas <Lobby>";
	}
	
	@Override
	public String getPermission() {
		return "McInfected.Arenas";
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
		else
			return Arrays.asList(new String[] { "" });
	}
}
