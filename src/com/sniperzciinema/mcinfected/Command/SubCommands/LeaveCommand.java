
package com.sniperzciinema.mcinfected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sniperzciinema.mcinfected.McInfected;
import com.sniperzciinema.mcinfected.Messanger.Messages;
import com.sniperzciinema.mcinfected.Command.SubCommand;
import com.sniperzciinema.mcinfected.Command.FancyMessages.FancyMessage;
import com.sniperzciinema.mcinfected.Events.McInfectedLeaveEvent;
import com.sniperzciinema.mcinfected.IPlayers.IPlayer;
import com.sniperzciinema.mcinfected.Lobbys.Lobby;


public class LeaveCommand extends SubCommand {
	
	public LeaveCommand()
	{
		super("Leave");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (!(sender instanceof Player))
			sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Command__Not_A_Player));
		
		McInfectedLeaveEvent event = new McInfectedLeaveEvent((Player) sender);
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled())
			if (!sender.hasPermission(getPermission()))
				sender.sendMessage(messanger.getMessage(true, Messages.Error__Misc__Invalid_Permission));
			
			else if (!McInfected.getLobbyManager().isPlaying((Player) sender))
				sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Game__Not_In_A_Game));
			
			else
			{
				IPlayer iPlayer = McInfected.getLobbyManager().getIPlayer((Player) sender);
				Lobby lobby = McInfected.getLobbyManager().getLobby((Player) sender);
				iPlayer.leave();
				lobby.removeIPlayer(iPlayer);
				sender.sendMessage(getMessanger().getMessage(true, Messages.Command__Leave));
				messanger.broadcast(lobby, getMessanger().getMessage(true, Messages.Command__Game_Left, "<player>", sender.getName()));
				
			}
		
	}
	
	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "l", "quit" });
	}
	
	@Override
	public FancyMessage getFancyMessage() {
		return new FancyMessage(getHelpMessage()).tooltip("Leave", " ", "§eLeave the Arena", " ", "§f§l/McInfected Leave").suggest("/McInfected Leave");
	}
	
	@Override
	public String getHelpMessage() {
		return getMessanger().getMessage(false, Messages.Format__Prefix) + "§7/McInfected §oLeave";
	}
	
	@Override
	public String getPermission() {
		return "McInfected.Leave";
	}
	
	@Override
	public List<String> getTabs(String[] args) {
		return Arrays.asList(new String[] { "" });
	}
}
