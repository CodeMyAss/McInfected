
package com.sniperzciinema.mcinfected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sniperzciinema.mcinfected.McInfected;
import com.sniperzciinema.mcinfected.Messanger.Messages;
import com.sniperzciinema.mcinfected.Command.SubCommand;
import com.sniperzciinema.mcinfected.Command.FancyMessages.FancyMessage;
import com.sniperzciinema.mcinfected.Lobbys.Lobby;
import com.sniperzciinema.mcinfected.Utils.Coord;


public class CreateLobbyCommand extends SubCommand {
	
	public CreateLobbyCommand()
	{
		super("CreateLobby");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		
		if (!(sender instanceof Player))
			sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Command__Not_A_Player));
		
		else if (!sender.hasPermission(getPermission()))
			sender.sendMessage(messanger.getMessage(true, Messages.Error__Misc__Invalid_Permission));
		
		else
		{
			
			if (args.length >= 2)
			{
				if (!McInfected.getLobbyManager().isLobby(args[1]))
				{
					String lobbyName = args[1];
					
					Lobby lobby = McInfected.getLobbyManager().createLobby(lobbyName);
					lobby.setLocation(new Coord(((Player) sender).getLocation()));
					sender.sendMessage(getMessanger().getMessage(true, Messages.Command__CreateLobby, "<lobby>", lobbyName));
					
				}
				
				else
					sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Lobby__Already_Exists));
				
			}
			else
				getFancyMessage().send((Player) sender);
			
		}
		
	}
	
	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "cl" });
	}
	
	@Override
	public FancyMessage getFancyMessage() {
		return new FancyMessage(getHelpMessage()).tooltip("CreateLobby", " ", "§eCreate a Lobby", " ", "§f§l/McInfected CreateLobby <LobbyName>").suggest("/McInfected CreateLobby <LobbyName>");
	}
	
	@Override
	public String getHelpMessage() {
		return getMessanger().getMessage(false, Messages.Format__Prefix) + "§7/McInfected §oCreateLobby <LobbyName>";
	}
	
	@Override
	public String getPermission() {
		return "McInfected.Setup.CreateLobby";
	}
	
	@Override
	public List<String> getTabs(String[] args) {
		
		return Arrays.asList(new String[] { "" });
		
	}
}
