
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
import com.sniperzciinema.mcinfected.Command.SubCommand;
import com.sniperzciinema.mcinfected.Command.FancyMessages.FancyMessage;
import com.sniperzciinema.mcinfected.Utils.Coord;


public class SetLobbyCommand extends SubCommand {
	
	public SetLobbyCommand()
	{
		super("SetLobby");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		
		if (!(sender instanceof Player))
			sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Command__Not_A_Player));
		
		else if (!sender.hasPermission(getPermission()))
			sender.sendMessage(messanger.getMessage(true, Messages.Error__Misc__Invalid_Permission));
		else
		{
			if (McInfected.getLobbyManager().isLobby(args[1]))
			{
				Lobby lobby = McInfected.getLobbyManager().getLobby(args[1]);
				
				lobby.setLocation(new Coord(((Player) sender).getLocation()));
				sender.sendMessage(getMessanger().getMessage(true, Messages.Command__SetLobby));
			}
			else
				sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Lobby__Doesnt_Exist));
			
		}
	}
	
	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "" });
	}
	
	@Override
	public FancyMessage getFancyMessage() {
		return new FancyMessage(getHelpMessage()).tooltip("SetLobby", " ", "§eSet the lobby's position to your current spot", " ", "§f§l/McInfected SetLobby").suggest("/McInfected SetLobby");
	}
	
	@Override
	public String getHelpMessage() {
		return getMessanger().getMessage(false, Messages.Format__Prefix) + "§7/McInfected §oSetLobby";
	}
	
	@Override
	public String getPermission() {
		return "McInfected.Setup.SetLobby";
	}
	
	@Override
	public List<String> getTabs(String[] args) {
		ArrayList<String> lobbys = new ArrayList<String>();
		for (Lobby lobby : McInfected.getLobbyManager().getLobbys())
			lobbys.add(lobby.getName());
		
		return lobbys;
	}
}
