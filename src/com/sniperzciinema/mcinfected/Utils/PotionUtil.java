
package com.sniperzciinema.mcinfected.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


@SuppressWarnings("deprecation")
public class PotionUtil {
	
	public static PotionEffect getPotionEffect(String potionCode) {
		PotionEffect pe = null;
		int dur = 1, amp = 0;
		if (potionCode != null)
		{
			String[] strings = potionCode.split(" ");
			
			for (String data : strings)
				if (data.startsWith("type") || data.startsWith("id"))
				{
					PotionEffectType pet = null;
					
					String p = data.split(":")[1];
					
					try
					{
						pet = PotionEffectType.getById(Integer.valueOf(p));
					}
					catch (NumberFormatException e)
					{
						if (PotionEffectType.getByName(p) != null)
							pet = PotionEffectType.getByName(p);
						else
							pet = PotionEffectType.SPEED;
					}
					
					pe = new PotionEffect(pet, 1, 1);
				}
				else if (data.startsWith("length") || data.startsWith("time") || data.startsWith("duration"))
					dur = Integer.valueOf(data.split(":")[1]) * 20;
				else if (data.startsWith("power") || data.startsWith("strength") || data.startsWith("amplifier"))
					amp = Integer.valueOf(data.split(":")[1]) - 1;
			
		}
		pe.getType().createEffect(dur, amp);
		return pe;
	}
	
	public static ArrayList<PotionEffect> getPotionEffects(List<String> potionCodes) {
		ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
		if ((potionCodes != null) && !potionCodes.isEmpty())
			for (String code : potionCodes)
				effects.add(getPotionEffect(code));
		
		return effects;
	}
	
	public static ArrayList<String> getPotionEffectsToString(List<PotionEffect> potions) {
		ArrayList<String> effects = new ArrayList<String>();
		if ((potions != null) && !potions.isEmpty())
			for (PotionEffect potion : potions)
				effects.add(getPotionEffectToString(potion));
		
		return effects;
	}
	
	public static String getPotionEffectToString(PotionEffect pe) {
		String potion = "type:" + pe.getType().getName();
		potion += " amplifier:" + (pe.getAmplifier() - 1);
		potion += " duration:" + (pe.getDuration() / 20);
		return potion;
	}
}
