
package com.sniperzciinema.mcinfected.Command.SubCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sniperzciinema.mcinfected.McInfected;
import com.sniperzciinema.mcinfected.Messanger.Messages;
import com.sniperzciinema.mcinfected.Command.SubCommand;
import com.sniperzciinema.mcinfected.Command.FancyMessages.FancyMessage;
import com.sniperzciinema.mcinfected.Lobbys.Lobby;
import com.sniperzciinema.mcinfected.Utils.StringUtil;


public class CreateArenaCommand extends SubCommand {
	
	public CreateArenaCommand()
	{
		super("CreateArena");
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		
		if (!(sender instanceof Player))
			sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Command__Not_A_Player));
		
		else if (!sender.hasPermission(getPermission()))
			sender.sendMessage(messanger.getMessage(true, Messages.Error__Misc__Invalid_Permission));
		
		else
		{
			
			if (args.length >= 3)
			{
				if (McInfected.getLobbyManager().isLobby(args[1]))
				{
					Lobby lobby = McInfected.getLobbyManager().getLobby(args[1]);
					String arenaName = StringUtil.getCapitalized(args[2]);
					if (!lobby.getArenaManager().isArena(arenaName))
					{
						lobby.getArenaManager().createArena(arenaName);
						Block block = ((Player) sender).getLocation().add(0, -1.0, 0).getBlock();
						ItemStack is = new ItemStack(block.getType());
						is.setDurability(block.getData());
						
						lobby.getArenaManager().getArena(arenaName).setIcon(is);
						sender.sendMessage(getMessanger().getMessage(true, Messages.Command__CreateArena, "<arena>", arenaName));
						
						if (args.length == 4)
							if (lobby.getArenaManager().isArena(args[2]))
								lobby.getArenaManager().getArena(args[2]).setCreator(args[3]);
						
					}
					else
						sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Arena__Already_Exists));
				}
				else
					sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Lobby__Doesnt_Exist));
				
			}
			else
				getFancyMessage().send((Player) sender);
			
		}
		
	}
	
	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "ca" });
	}
	
	@Override
	public FancyMessage getFancyMessage() {
		return new FancyMessage(getHelpMessage()).tooltip("CreateArena", " ", "§eCreate an Arena", " ", "§f§l/McInfected CreateArena <Lobby> <ArenaName> [Creator]").suggest("/McInfected CreateArena <Lobby> <ArenaName> [Creator]");
	}
	
	@Override
	public String getHelpMessage() {
		return getMessanger().getMessage(false, Messages.Format__Prefix) + "§7/McInfected §oCreateArena <Lobby> <Arena> [Creator]";
	}
	
	@Override
	public String getPermission() {
		return "McInfected.Setup.CreateArena";
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
