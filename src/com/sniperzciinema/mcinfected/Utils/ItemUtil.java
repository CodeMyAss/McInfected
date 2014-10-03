
package com.sniperzciinema.mcinfected.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.shampaggon.crackshot.CSUtility;


public class ItemUtil {
	
	private static class EnchantGlow extends EnchantmentWrapper {
		
		private static Enchantment	glow;
		
		public static void addGlow(ItemStack item) {
			Enchantment glow = getGlow();
			if (!item.containsEnchantment(glow))
				item.addEnchantment(glow, 1);
		}
		
		public static Enchantment getGlow() {
			if (glow != null)
				return glow;
			
			try
			{
				Field f = Enchantment.class.getDeclaredField("acceptingNew");
				f.setAccessible(true);
				f.set(null, true);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			glow = new EnchantGlow(255);
			try
			{
				Enchantment.registerEnchantment(glow);
			}
			catch (IllegalArgumentException iae)
			{
			}
			return glow;
		}
		
		public static boolean isGlow(ItemStack item) {
			return item.getEnchantments().containsKey(glow);
		}
		
		public EnchantGlow(int id)
		{
			super(id);
		}
		
		@Override
		public boolean canEnchantItem(ItemStack item) {
			return true;
		}
		
		@Override
		public boolean conflictsWith(Enchantment other) {
			return false;
		}
		
		@Override
		public EnchantmentTarget getItemTarget() {
			return null;
		}
		
		@Override
		public int getMaxLevel() {
			return 10;
		}
		
		@Override
		public String getName() {
			return "Glow";
		}
		
		@Override
		public int getStartLevel() {
			return 1;
		}
		
	}
	
	/**
	 * Add Glow
	 * 
	 * @param is
	 * @return
	 */
	public static ItemStack addGlow(ItemStack is) {
		EnchantGlow.addGlow(is);
		return is;
	}
	
	/**
	 * Get the itemstack from the item code
	 * 
	 * @param itemCode
	 * @return the ItemStack
	 */
	@SuppressWarnings("deprecation")
	public static ItemStack getItemStack(String itemCode) {
		ItemStack stack = new ItemStack(Material.AIR, 1);
		if (itemCode != null)
			if (itemCode.contains(" "))
			{
				String[] line = itemCode.split(" ");
				// Loop through every part of the code
				for (String data : line)
					
					if (data.startsWith("crackshot"))
					{
						String name = data.split(":")[1];
						CSUtility cs = new CSUtility();
						stack = cs.generateWeapon(name);
					}
					else
					
					// Item Variables
					if (data.startsWith("id") || data.startsWith("item"))
					{
						
						Material mat = null;
						String item = data.split(":")[1];
						try
						{
							mat = Material.getMaterial(Integer.valueOf(item));
						}
						catch (NumberFormatException e)
						{
							if (Material.getMaterial(item) != null)
								mat = Material.getMaterial(item);
							else
								mat = Material.AIR;
						}
						stack.setType(mat);
					}
					
					// Amount Variables
					else if (data.startsWith("amount") || data.startsWith("quantity"))
						stack.setAmount(Integer.parseInt(data.split(":")[1]));
					
					// Durability Variables
					else if (data.startsWith("data") || data.startsWith("durability") || data.startsWith("damage"))
						stack.setDurability(Short.parseShort(data.split(":")[1]));
					
					// Enchantment variables
					else if (data.startsWith("enchantment") || data.startsWith("enchant"))
					{
						String s = data.split(":")[1];
						
						Enchantment enchantment;
						try
						{
							enchantment = Enchantment.getById(Integer.parseInt(s.split("-")[0]));
						}
						catch (NumberFormatException e)
						{
							enchantment = Enchantment.getByName(s.split("-")[0].toUpperCase());
						}
						// Level Stated
						if (s.contains("-"))
							stack.addUnsafeEnchantment(enchantment, Integer.parseInt(s.split("-")[1]));
						// No Level Stated
						else
							stack.addUnsafeEnchantment(enchantment, 1);
						
					}
					else if (data.startsWith("glow"))
					{
						stack = addGlow(stack);
					}
					// Name Variables
					else if (data.startsWith("name") || data.startsWith("title"))
					{
						ItemMeta im = stack.getItemMeta();
						im.setDisplayName(StringUtil.addColor(data.split(":")[1]));
						stack.setItemMeta(im);
					}
					
					// Owner Variables
					else if (data.startsWith("owner") || data.startsWith("player"))
					{
						SkullMeta im = (SkullMeta) stack.getItemMeta();
						im.setOwner(data.split(":")[1]);
						stack.setItemMeta(im);
					}
					
					// Color Variables(Leather Only)
					else if (data.startsWith("color") || data.startsWith("colour"))
						try
						{
							LeatherArmorMeta im = (LeatherArmorMeta) stack.getItemMeta();
							String[] s = data.replaceAll("color:", "").replaceAll("colour", "").split(",");
							int red = Integer.parseInt(s[0]);
							int green = Integer.parseInt(s[1]);
							int blue = Integer.parseInt(s[2]);
							im.setColor(Color.fromRGB(red, green, blue));
							stack.setItemMeta(im);
						}
						catch (ClassCastException notLeather)
						{
						}
					// Potion Effect Variables(Potions Only)
					else if (data.startsWith("potion"))
					{
						String[] s = data.replaceAll("potion:", "").split(",");
						PotionEffectType type = PotionEffectType.SPEED;
						try
						{
							type = PotionEffectType.getById((Integer.valueOf(s[0])));
						}
						catch (NumberFormatException e)
						{
							if (PotionEffectType.getByName(s[0].toUpperCase()) != null)
								type = PotionEffectType.getByName(s[0].toUpperCase());
						}
						Potion potion = new Potion(PotionType.getByEffect(type));
						potion.setSplash((s.length == 4) && s[3].equalsIgnoreCase("true") ? true : false);
						potion.apply(stack);
						PotionMeta pm = (PotionMeta) stack.getItemMeta();
						
						int level = Integer.parseInt(s[1]) - 1;
						int time = Integer.parseInt(s[2]) * 20;
						pm.addCustomEffect(new PotionEffect(type, time, level), false);
					}
					
					// Lore Variables
					else if (data.startsWith("lore") || data.startsWith("desc") || data.startsWith("description"))
					{
						String s = data.split(":")[1];
						List<String> lores = new ArrayList<String>();
						for (String lore : s.split("\\|"))
							lores.add(StringUtil.addColor(lore.replace('_', ' ')));
						ItemMeta meta = stack.getItemMeta();
						meta.setLore(lores);
						stack.setItemMeta(meta);
					}
					// Page Variables
					else if (data.startsWith("page") || data.startsWith("pages"))
					{
						String s = data.split(":")[1];
						List<String> pages = new ArrayList<String>();
						for (String lore : s.split("\\|"))
							pages.add(StringUtil.addColor(lore.replace('_', ' ')));
						
						BookMeta meta = (BookMeta) stack.getItemMeta();
						meta.setPages(pages);
						stack.setItemMeta(meta);
					}// Author Variables
					else if (data.startsWith("author") || data.startsWith("writter"))
					{
						BookMeta im = (BookMeta) stack.getItemMeta();
						im.setAuthor(StringUtil.addColor(data.split(":")[1]));
						stack.setItemMeta(im);
					}
					// Title Variables
					else if (data.startsWith("title"))
					{
						BookMeta im = (BookMeta) stack.getItemMeta();
						im.setTitle(StringUtil.addColor(data.split(":")[1]));
						stack.setItemMeta(im);
					}
			}
			
			// Item Variables
			else if (itemCode.startsWith("id") || itemCode.startsWith("item"))
				return new ItemStack(Material.getMaterial(Integer.parseInt(itemCode.split(":")[1])));
			
			// Crackshot Variables else if (itemCode.startsWith("crackshot") ||
			else if (itemCode.startsWith("gun"))
			{
				String name = itemCode.split(":")[1];
				CSUtility cs = new CSUtility();
				stack = cs.generateWeapon(name);
			}
		
		return stack;
	}
	
	/** Loop through a list of these Item Codes and make a List<ItemStack> */
	public static ArrayList<ItemStack> getItemStacks(List<String> list) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		if ((list != null) && !list.isEmpty())
			for (String string : list)
				items.add(getItemStack(string));
		
		return items;
	}
	
	/** Loop through a list of these ItemStacks and make a List<String> */
	public static ArrayList<String> getItemStacksToString(List<ItemStack> list) {
		ArrayList<String> items = new ArrayList<String>();
		if ((list != null) && !list.isEmpty())
			for (ItemStack item : list)
				items.add(getItemStackToString(item));
		
		return items;
	}
	
	// TODO: Add Potions
	@SuppressWarnings("deprecation")
	public static String getItemStackToString(ItemStack i) {
		String itemCode = "id:0";
		
		if (i != null && i.getType() != Material.AIR)
		{
			
			itemCode = "id:" + String.valueOf(i.getTypeId());
			
			if (i.getDurability() != 0)
				itemCode += " data:" + i.getDurability();
			if (i.getAmount() > 1)
				itemCode += " amount:" + i.getAmount();
			
			for (Entry<Enchantment, Integer> ench : i.getEnchantments().entrySet())
			{
				if (!ench.getKey().getName().equals("Glow"))
				{
					itemCode += " enchantment:" + ench.getKey().getId();
					if (ench.getValue() > 1)
						itemCode += "-" + ench.getValue();
				}
			}
			if (isGlow(i))
			{
				itemCode += " glow";
			}
			if (i.getItemMeta().getDisplayName() != null)
				itemCode += " name:" + i.getItemMeta().getDisplayName().replaceAll(" ", "_").replaceAll("§", "&");
			
			if (i.getItemMeta().hasLore())
			{
				itemCode += " lore:";
				for (String string : i.getItemMeta().getLore())
					itemCode += string.replaceAll(" ", "_").replaceAll("§", "&") + "|";
				itemCode = itemCode.substring(0, itemCode.length() - 1);
			}
			if (i.getType().toString().toLowerCase().contains("leather") && (((LeatherArmorMeta) i.getItemMeta()).getColor() != null))
			{
				LeatherArmorMeta im = (LeatherArmorMeta) i.getItemMeta();
				itemCode += " color:" + im.getColor().getRed() + "," + im.getColor().getGreen() + "," + im.getColor().getBlue();
			}
			if (i.getType() == Material.WRITTEN_BOOK)
			{
				BookMeta im = (BookMeta) i.getItemMeta();
				if (im.hasAuthor())
					itemCode += " author:" + im.getAuthor();
				if (im.hasTitle())
					itemCode += " title:" + im.getTitle().replaceAll(" ", "_").replaceAll("§", "&");
				
				if (im.hasPages())
				{
					itemCode += " pages:";
					for (String string : im.getPages())
						itemCode += string.replaceAll(" ", "_").replaceAll("§", "&") + "|";
					
					itemCode = itemCode.substring(0, itemCode.length() - 1);
				}
			}
		}
		return itemCode;
	}
	
	/**
	 * Is it just a glow?
	 * 
	 * @param is
	 * @return
	 */
	public static boolean isGlow(ItemStack is) {
		return EnchantGlow.isGlow(is);
	}
}
