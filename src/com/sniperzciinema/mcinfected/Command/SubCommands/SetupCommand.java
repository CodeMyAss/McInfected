
package com.sniperzciinema.mcinfected.Command.SubCommands;


//TODO: Make every setup run from this class

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
import com.sniperzciinema.mcinfected.IPlayers.IPlayer.Team;
import com.sniperzciinema.mcinfected.Kits.Kit;
import com.sniperzciinema.mcinfected.Lobbys.Lobby;
import com.sniperzciinema.mcinfected.Utils.ItemUtil;
import com.sniperzciinema.mcinfected.Utils.StringUtil;


public class SetupCommand extends SubCommand {
	
	public SetupCommand()
	{
		super("Setup");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		Player player = null;
		try
		{
			player = (Player) sender;
		}
		catch (Exception e)
		{
		}
		
		if (!(sender instanceof Player))
			sender.sendMessage(getMessanger().getMessage(true, Messages.Error__Command__Not_A_Player));
		
		if (!sender.hasPermission(getPermission()))
			sender.sendMessage(messanger.getMessage(true, Messages.Error__Misc__Invalid_Permission));
		
		else
		{
			for (int i = 0; i != 15; i++)
				sender.sendMessage("");
			
			sender.sendMessage(messanger.getHeader("Setup" + (args.length > 1 ? " " + StringUtil.combineArgs(args, 1, args.length - 1) : "")));
			sender.sendMessage("");
			if (args.length == 1)
			{
				
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Edit The Lobbys").tooltip(ChatColor.GOLD + "Modify the Lobbys' values").command("/McInfected Setup Lobby").send(player);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Edit The Kits").tooltip(ChatColor.GOLD + "Modify the Kit's values").command("/McInfected Setup Kit").send(player);
			}
		}
		if (args.length > 1)
		{
			if (args[1].equals("Lobby"))
				setupLobby(player, args);
			
			else if (args[1].equals("Kit"))
				setupKit(player, args);
			
			player.sendMessage("");
			new FancyMessage(messanger.getPrefix()).then("§4<< Back").tooltip("Go back a Setup Page").command("/McInfected " + StringUtil.combineArgs(args, 0, args.length - 2)).send(player);
		}
	}
	
	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "Manage" });
	}
	
	@Override
	public FancyMessage getFancyMessage() {
		return new FancyMessage(getHelpMessage()).tooltip("Setup", " ", "§eManage Infected", " ").suggest("/McInfected Setup");
	}
	
	@Override
	public String getHelpMessage() {
		return getMessanger().getMessage(false, Messages.Format__Prefix) + "§7/McInfected §oSetup";
	}
	
	@Override
	public String getPermission() {
		return "McInfected.Setup";
	}
	
	@Override
	public List<String> getTabs(String[] args) {
		
		return Arrays.asList(new String[] { "" });
		
	}
	
	public void setupArenas(Player player, String[] args) {

		System.out.println(args);
		String lobbyName = args.length >= 3 ? args[2] : null;
		String arenaName = args.length >= 5 ? args[4] : null;
		
		if (arenaName == null)
		{
			new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Create Arena").suggest("/McInfected CreateArena <Name>").send(player);
			for (Arena arena : McInfected.getLobbyManager().getLobby(lobbyName).getArenaManager().getArenas())
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Edit " + arena.getName()).tooltip(ChatColor.GOLD + "Edit this arena's values").command("/McInfected Setup Lobby " + lobbyName + " Arenas " + arena.getName()).send(player);
			
		}
		else
		{
			Arena arena = McInfected.getLobbyManager().getLobby(lobbyName).getArenaManager().getArena(arenaName);
			if (args.length == 5)
			{
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Global Spawns").tooltip(ChatColor.GOLD + "Modify Global Spawns").command("/McInfected Setup Lobby " + lobbyName + " Arenas "  + arena.getName() + " Global").send(player);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Human Spawns").tooltip(ChatColor.GOLD + "Modify Human Spawns").command("/McInfected Setup Lobby " + lobbyName + " Arenas "  + arena.getName() + " Human").send(player);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Zombie Spawns").tooltip(ChatColor.GOLD + "Modify Zombie Spawns").command("/McInfected Setup Lobby " + lobbyName + " Arenas "  + arena.getName() + " Zombie").send(player);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Creator").tooltip(ChatColor.GOLD + "Modify Creator").command("/McInfected Setup Lobby " + lobbyName + " Arenas "  + arena.getName() + " Creator").send(player);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Icon").tooltip(ChatColor.GOLD + "Modify Icon").command("/McInfected Setup Lobby " + lobbyName + " Arenas "  + arena.getName() + " Icon").send(player);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Delete Arena").tooltip(ChatColor.GOLD + "Deletes this arena").suggest("/McInfected DeleteArena " + arena.getName()).send(player);
				
			}
			else
			{
				if (args[5].equalsIgnoreCase("Creator"))
				{
					new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Creator: " + arena.getCreator()).send(player);
					new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Set Creator").tooltip(ChatColor.GOLD + "Set arena's creator").suggest("/McInfected SetCreator " + arena.getName() + "<creator>").send(player);
				}
				else if (args[5].equalsIgnoreCase("Icon"))
				{
					new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Icon: " + ItemUtil.getItemStackToString(arena.getIcon())).send(player);
					new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Set Icon").tooltip(ChatColor.GOLD + "Set arena's icon").suggest("/McInfected SetIcon " + arena.getName() + "<item code>").send(player);
				}
				else if (Team.isTeam(args[5]))
				{
					Team team = Team.valueOf(StringUtil.getCapitalized(args[5]));
					new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Total Spawns: " + arena.getSpawns(team).size()).tooltip(ChatColor.GOLD + "How many spawns").send(player);
					new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Set Spawn").tooltip(ChatColor.GOLD + "Sets a new " + team.toString() + " spawn at your current location").command("/McInfected SetSpawn " + arena.getName() + " " + team.toString()).send(player);
					new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Tp Spawn").tooltip(ChatColor.GOLD + "Teleport to a spawn #").suggest("/McInfected TpSpawn " + arena.getName() + " " + team.toString() + "<#>").send(player);
					
				}
			}
		}
	}
	
	public void setupKit(Player player, String[] args) {
		String kitName = args.length > 3 ? args[3] : null;
		if (kitName == null)
		{
			new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Create Kit").suggest("/McInfected Create Kit").send(player);
			for (Kit kit : McInfected.getKitManager().getKits(Team.Human))
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GREEN + ChatColor.BOLD + "Edit " + kit.getName()).tooltip(ChatColor.GOLD + "Edit this kit's values").command("/McInfected Setup Kit Human " + kit.getName()).send(player);
			for (Kit kit : McInfected.getKitManager().getKits(Team.Zombie))
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.RED + ChatColor.BOLD + "Edit " + kit.getName()).tooltip(ChatColor.GOLD + "Edit this kit's values").command("/McInfected Setup Kit Zombie " + kit.getName()).send(player);
		}
		else
		{
			Kit kit = McInfected.getKitManager().getKit(kitName);
			if (args.length == 4)
			{
				Team team = Team.valueOf(args[2]);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Icon").tooltip(ChatColor.GOLD + "Modify Icon").command("/McInfected Setup Kit " + team.toString() + " " + kit.getName() + " Icon").send(player);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Description").tooltip(ChatColor.GOLD + "Modify Description").command("/McInfected Setup Kit " + team.toString() + " " + kit.getName() + " Description").send(player);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Disguise").tooltip(ChatColor.GOLD + "Modify Disguise").command("/McInfected Setup Kit " + team.toString() + " " + kit.getName() + " Disguise").send(player);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Helmet").tooltip(ChatColor.GOLD + "Modify Helmet").command("/McInfected Setup Kit " + team.toString() + " " + kit.getName() + "Helmet").send(player);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Chestplate").tooltip(ChatColor.GOLD + "Modify Chestplate").command("/McInfected Setup Kit " + team.toString() + " " + kit.getName() + "Chestplate").send(player);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Leggings").tooltip(ChatColor.GOLD + "Modify Leggings").command("/McInfected Setup Kit " + team.toString() + " " + kit.getName() + "Leggings").send(player);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Boots").tooltip(ChatColor.GOLD + "Modify Boots").command("/McInfected Setup Kit " + team.toString() + " " + kit.getName() + "Boots").send(player);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Inventoty").tooltip(ChatColor.GOLD + "Modify Inventory").command("/McInfected Setup Kit " + team.toString() + " " + kit.getName() + "Inventory").send(player);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "PotionEffects").tooltip(ChatColor.GOLD + "Modify PotionEffects").command("/McInfected Setup Kit " + team.toString() + " " + kit.getName() + "PotionEffects").send(player);
			}
		}
	}
	
	public void setupLobby(Player player, String[] args) {
		
		String lobbyName = args.length >= 3 ? args[2] : null;
		
		if (args.length == 2)
		{
			new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Create Lobby").suggest("/McInfected Setup Lobby CreateNew").send(player);
			for (Lobby lobby : McInfected.getLobbyManager().getLobbys())
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Edit " + lobby.getName()).tooltip(ChatColor.GOLD + "Edit this Lobby's values").command("/McInfected Setup Lobby " + lobby.getName()).send(player);
			
		}
		else if (args.length == 3)
		{
			new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Lobby Location").tooltip(ChatColor.GOLD + "Lobby's Location").command("/McInfected Setup Lobby "+lobbyName+" Location").send(player);
			new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Leave Location").tooltip(ChatColor.GOLD + "Where You Go When You Leave's Location").command("/McInfected Setup Lobby "+lobbyName+" Leave").send(player);
			new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Arena Manager").tooltip(ChatColor.GOLD + "Manage this Lobby's Arenas").command("/McInfected Setup Lobby " + lobbyName + " Arenas").send(player);
			
		}
		else if (args.length >= 4)
		{
			if (args[3].equalsIgnoreCase("Location"))
			{
				String location = McInfected.getLobbyManager().getLobby(lobbyName).getLocation() != null ? McInfected.getLobbyManager().getLobby(lobbyName).getLocation().asStringIgnoreYawAndPitch() : "";
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Location: " + location).send(player);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Set Location").tooltip(ChatColor.GOLD + "Set Lobby's Location to your current location").command("/McInfected SetLobby").send(player);
			}
			else if (args[3].equalsIgnoreCase("Leave"))
			{
				String location = McInfected.getLobbyManager().getLobby(lobbyName).getLeaveLocation() != null ? McInfected.getLobbyManager().getLobby(lobbyName).getLeaveLocation().asStringIgnoreYawAndPitch() : "";
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Location: " + location).send(player);
				new FancyMessage(messanger.getPrefix()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Set Location").tooltip(ChatColor.GOLD + "Set Lobby's Leave Location to your current location").command("/McInfected SetLeave").send(player);
			}
			else if (args[3].equalsIgnoreCase("Arenas"))
			{
			setupArenas(player, args);	
			}
		}
	}
}
