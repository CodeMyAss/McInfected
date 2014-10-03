
package com.sniperzciinema.mcinfected.Kits;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;

import com.sniperzciinema.mcinfected.McInfected;
import com.sniperzciinema.mcinfected.IPlayers.IPlayer.Team;
import com.sniperzciinema.mcinfected.Utils.ItemUtil;
import com.sniperzciinema.mcinfected.Utils.PotionUtil;


public class KitManager {
	
	private ArrayList<Kit>	kits;
	
	public KitManager()
	{
		this.kits = new ArrayList<Kit>();
		loadKitsFromFile();
	}
	
	/**
	 * Create your own kit the way you want
	 * 
	 * @param Kit
	 */
	public void createKit(Kit kit) {
		this.kits.add(kit);
	}
	
	/**
	 * Create a kit
	 * 
	 * @param kitName
	 * @param team
	 */
	public void createKit(String kitName, Team team) {
		this.kits.add(new Kit(kitName, team));
	}
	
	/**
	 * @param kitName
	 * @return the kit
	 */
	public Kit getKit(String kitName) {
		for (Kit kit : this.kits)
			if (kit.getName().equalsIgnoreCase(kitName))
				return kit;
		return null;
	}
	
	/**
	 * @return kits
	 */
	public ArrayList<Kit> getKits() {
		return this.kits;
	}
	
	/**
	 * @param team
	 * @return kits that belong to the team
	 */
	public ArrayList<Kit> getKits(Team team) {
		ArrayList<Kit> teamKits = new ArrayList<Kit>();
		for (Kit kit : this.kits)
			if (kit.getTeam() == team)
				teamKits.add(kit);
		return teamKits;
	}
	
	/**
	 * Load kits from the default file
	 */
	public void loadKitsFromFile() {
		
		// Load Human Kits
		if (McInfected.getFileManager().getKits().getConfigurationSection("Human") != null)
			for (String kitName : McInfected.getFileManager().getKits().getConfigurationSection("Human").getKeys(true))
				if (!kitName.contains("."))
				{
					createKit(kitName, Team.Human);
					McInfected.getPlugin().getLogger().log(Level.INFO, "Loaded Human Kit: " + kitName);
				}
		// Load Zombie Kits
		if (McInfected.getFileManager().getKits().getConfigurationSection("Zombie") != null)
			for (String kitName : McInfected.getFileManager().getKits().getConfigurationSection("Zombie").getKeys(true))
				if (!kitName.contains("."))
				{
					createKit(kitName, Team.Zombie);
					McInfected.getPlugin().getLogger().log(Level.INFO, "Loaded Zombie Kit: " + kitName);
				}
	}
	
	/**
	 * Remove a kit
	 * 
	 * @param kitName
	 */
	public void removeKit(String kitName) {
		this.kits.remove(getKit(kitName));
	}
	
	public void saveKit(Kit kit) {
		
		FileConfiguration kitFile = McInfected.getFileManager().getKits();
		Team team = kit.getTeam();
		String name = kit.getName();
		
		kitFile.set(team.toString() + "." + name + ".Description", kit.getDescription());
		kitFile.set(team.toString() + "." + name + ".Icon", ItemUtil.getItemStackToString(kit.getIcon()));
		kitFile.set(team.toString() + "." + name + ".Inventory", ItemUtil.getItemStacksToString(kit.getInventory()));
		kitFile.set(team.toString() + "." + name + ".Armor.Helmet", ItemUtil.getItemStackToString(kit.getHelmet()));
		kitFile.set(team.toString() + "." + name + ".Armor.Chestplate", ItemUtil.getItemStackToString(kit.getChestplate()));
		kitFile.set(team.toString() + "." + name + ".Armor.Leggings", ItemUtil.getItemStackToString(kit.getLeggings()));
		kitFile.set(team.toString() + "." + name + ".Armor.Boots", ItemUtil.getItemStackToString(kit.getBoots()));
		kitFile.set(team.toString() + "." + name + ".Potions", PotionUtil.getPotionEffectsToString(kit.getPotions()));
		kitFile.set(team.toString() + "." + name + ".Disguise", kit.getDisguise());
		
		McInfected.getFileManager().saveKits();
		
	}
	
}
