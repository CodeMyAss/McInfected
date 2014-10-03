
package com.sniperzciinema.mcinfected.Command.SubCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.sniperzciinema.mcinfected.McInfected;
import com.sniperzciinema.mcinfected.Messanger.Messages;
import com.sniperzciinema.mcinfected.Command.SubCommand;
import com.sniperzciinema.mcinfected.Command.FancyMessages.FancyMessage;
import com.sniperzciinema.mcinfected.Lobbys.Lobby;
import com.sniperzciinema.mcinfected.Lobbys.Lobby.GameState;
import com.sniperzciinema.mcinfected.Utils.ItemUtil;
import com.sniperzciinema.mcinfected.Utils.PotionUtil;


public class AdminCommand extends SubCommand {
	
	public AdminCommand()
	{
		super("Admin");
	}
	
	private void edit(CommandSender sender, String[] args) {
		
	}
	
	private void end(CommandSender sender, String[] args) {
		
		if (!sender.hasPermission(getPermission() + ".End"))
			sender.sendMessage(messanger.getMessage(true, Messages.Error__Misc__Invalid_Permission));
		
		else
		{
			sender.sendMessage(getMessanger().getMessage(true, Messages.Command__End));
			if (args.length == 3)
			{
				if (McInfected.getLobbyManager().isLobby(args[2]))
				{
					Lobby lobby = McInfected.getLobbyManager().getLobby(args[2]);
					if (lobby.getIPlayers().isEmpty())
						sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Command__Not_Enough_Players));
					
					else if (lobby.getGameState() == GameState.InLobby)
						sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Game__Not_Started));
					else
						lobby.getTimers().startEndGame();
				}
				else
					sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Lobby__Doesnt_Exist));
				
			}
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (args.length < 2)
		{
			if (sender instanceof Player)
				getFancyMessage().send((Player) sender);
			else
				sender.sendMessage(getHelpMessage());
		}
		else
		{
			if (args[1].equalsIgnoreCase("Start"))
				start(sender, args);
			
			else if (args[1].equalsIgnoreCase("End"))
				end(sender, args);
			
			else if (args[1].equalsIgnoreCase("ItemCode"))
				itemcode(sender, args);
			
			else if (args[1].equalsIgnoreCase("PotionCode"))
				potioncode(sender, args);
			
			else if (args[1].equalsIgnoreCase("Reload"))
				reload(sender, args);
			
			else if (args[1].equalsIgnoreCase("Edit"))
				edit(sender, args);
			
			else if (args[1].equalsIgnoreCase("Files"))
				files(sender, args);
			
			else
				Bukkit.dispatchCommand(sender, "McInfected Admin");
		}
	}
	
	private void files(CommandSender sender, String[] args) {
		
	}
	
	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "" });
	}
	
	@Override
	public FancyMessage getFancyMessage() {
		return new FancyMessage(getHelpMessage()).tooltip("Admin", " ", "§eManage admin settings", " ", "§6§n§lPossible Commands: ", "§a/McInfected ItemCode §f<- See an items code ", "§a/McInfected PotionCode §f<- See a potion effects code ", "§a/McInfected Reload §f<- Reload all files", "§a/McInfected Edit <Stat> <Player> <New Value>  §f<- Edit a players stats", "§a/McInfected Files <File> <Get/Set/Read> §f<- Manage a file ", " ", "§f§l/McInfected Admin [ItemCode/Reload/Edit/Files]").suggest("/McInfected Admin");
	}
	
	@Override
	public String getHelpMessage() {
		return getMessanger().getMessage(false, Messages.Format__Prefix) + "§7/McInfected §oAdmin";
	}
	
	@Override
	public String getPermission() {
		return "McInfected.Admin";
	}
	
	@Override
	public List<String> getTabs(String[] args) {
		if (args.length == 2)
			return Arrays.asList(new String[] { "Start", "End", "ItemCode", "Reload", "Edit", "Files" });
		
		else if (args.length == 3 && args[1].equalsIgnoreCase("Edit"))
			return Arrays.asList(new String[] { "Score", "Kills", "Deaths", "Wins", "Loses", "KillStreak" });
		
		else if (args.length == 3 && (args[1].equalsIgnoreCase("Start") || args[1].equalsIgnoreCase("End")))
		{
			ArrayList<String> lobbys = new ArrayList<String>();
			for (Lobby lobby : McInfected.getLobbyManager().getLobbys())
				lobbys.add(lobby.getName());
			
			return lobbys;
		}
		else if (args.length == 4 && args[1].equalsIgnoreCase("Edit"))
			return null;
		
		else if (args.length == 4 && args[1].equalsIgnoreCase("Files"))
			return Arrays.asList(new String[] { "Config", "Kits", "Arenas", "Shops", "CommandSets", "Messages" });
		
		else if (args.length == 5 && args[1].equalsIgnoreCase("Files"))
			return Arrays.asList(new String[] { "Set", "Get", "Read" });
		
		else if (args.length == 5 && args[1].equalsIgnoreCase("Edit"))
			return Arrays.asList(new String[] { "" });
		
		else
			return Arrays.asList(new String[] { "" });
		
	}
	
	private void itemcode(CommandSender sender, String[] args) {
		if (!(sender instanceof Player))
			sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Command__Not_A_Player));
		
		else if (!sender.hasPermission(getPermission() + ".ItemCode"))
			sender.sendMessage(messanger.getMessage(true, Messages.Error__Misc__Invalid_Permission));
		
		else
		{
			String code = ItemUtil.getItemStackToString(((Player) sender).getItemInHand());
			sender.sendMessage("McInfected's Item Code: " + code);
			System.out.println("McInfected's Item Code: " + code);
		}
	}
	
	private void potioncode(CommandSender sender, String[] args) {
		if (!(sender instanceof Player))
			sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Command__Not_A_Player));
		
		else if (!sender.hasPermission(getPermission() + ".potionCode"))
			sender.sendMessage(messanger.getMessage(true, Messages.Error__Misc__Invalid_Permission));
		
		else
		{
			for (PotionEffect pe : ((Player) sender).getActivePotionEffects())
			{
				String code = PotionUtil.getPotionEffectToString(pe);
				sender.sendMessage("McInfected's Potion Code: " + code);
				System.out.println("McInfected's Potion Code: " + code);
			}
		}
	}
	
	private void reload(CommandSender sender, String[] args) {
		
		if (!sender.hasPermission(getPermission() + ".Reload"))
			sender.sendMessage(messanger.getMessage(true, Messages.Error__Misc__Invalid_Permission));
		
		else
		{
			sender.sendMessage(messanger.getMessage(true, Messages.Command__Reloaded));
			McInfected.reload();
		}
	}
	
	private void start(CommandSender sender, String[] args) {
		if (!sender.hasPermission(getPermission() + ".Start"))
			sender.sendMessage(messanger.getMessage(true, Messages.Error__Misc__Invalid_Permission));
		
		else
		{
			sender.sendMessage(getMessanger().getMessage(true, Messages.Command__End));
			if (args.length == 3)
			{
				if (McInfected.getLobbyManager().isLobby(args[2]))
				{
					
					if (McInfected.getLobbyManager().getLobby(args[2]).getIPlayers().isEmpty())
						sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Command__Not_Enough_Players));
					
					else if (McInfected.getLobbyManager().getLobby(args[2]).getGameState() != GameState.InLobby)
						sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Game__Already_Started));
					else
						McInfected.getLobbyManager().getLobby(args[2]).getTimers().startVoting();
				}
				else
					sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Lobby__Doesnt_Exist));
				
			}
		}
	}
}
