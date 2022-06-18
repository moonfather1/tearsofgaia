package moonfather.tearsofgaia.integration;

import moonfather.tearsofgaia.forging.ElementalHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventForResistancesFromTetraTools
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void OnPotionApplicableCheck(PotionEvent.PotionApplicableEvent event)
	{
		if (event.getPotionEffect().getEffect().equals(Effects.POISON))
		{
			// clientside event fires if i allow serverside. can't deny it on client, otherwise i get a poison but no indicator.
			if (! event.getEntity().level.isClientSide)
			{
				ItemStack tool = event.getEntityLiving().getMainHandItem();
				if (CheckItemsForElementalItem(tool, "earth") && tool.getDamageValue() < tool.getMaxDamage() * 0.95)
				{
					DenyEffect(event, tool, "poison");
					tool.setDamageValue(tool.getDamageValue() + Math.min((int)(tool.getMaxDamage() * 0.02), 15));
					return;
				}
				tool = event.getEntityLiving().getOffhandItem();
				if (CheckItemsForElementalItem(tool, "earth") && tool.getDamageValue() < tool.getMaxDamage() * 0.95)
				{
					DenyEffect(event, tool, "poison");
					tool.setDamageValue(tool.getDamageValue() + Math.min((int)(tool.getMaxDamage() * 0.02), 15));
					return;
				}
				for (ItemStack armor : event.getEntityLiving().getArmorSlots())
				{
					if (CheckItemsForElementalItem(armor, "earth") && armor.getDamageValue() < armor.getMaxDamage() * 0.95)
					{
						DenyEffect(event, armor, "poison");
						armor.setDamageValue(armor.getDamageValue() + Math.min((int)(armor.getMaxDamage() * 0.02), 15));
						return;
					}
				}
			}
		}
		else if (event.getPotionEffect().getEffect().equals(Effects.WITHER))
		{
			// clientside event fires if i allow serverside. can't deny it on client, otherwise i get a poison but no indicator.
			if (! event.getEntity().level.isClientSide)
			{
				ItemStack tool = event.getEntityLiving().getMainHandItem();
				if (CheckItemsForElementalItem(tool, "water") && tool.getDamageValue() < tool.getMaxDamage() * 0.95)
				{
					DenyEffect(event, tool, "wither");
					tool.setDamageValue(tool.getDamageValue() + Math.min((int)(tool.getMaxDamage() * 0.05), 25));
					return;
				}
				tool = event.getEntityLiving().getOffhandItem();
				if (CheckItemsForElementalItem(tool, "water") && tool.getDamageValue() < tool.getMaxDamage() * 0.95)
				{
					DenyEffect(event, tool, "wither");
					tool.setDamageValue(tool.getDamageValue() + Math.min((int)(tool.getMaxDamage() * 0.05), 25));
					return;
				}
				for (ItemStack armor : event.getEntityLiving().getArmorSlots())
				{
					if (CheckItemsForElementalItem(armor, "water") && armor.getDamageValue() < armor.getMaxDamage() * 0.95)
					{
						DenyEffect(event, armor, "wither");
						armor.setDamageValue(armor.getDamageValue() + Math.min((int)(armor.getMaxDamage() * 0.05), 25));
						return;
					}
				}
			}
		}
	}



	private static void DenyEffect(PotionEvent.PotionApplicableEvent event, ItemStack toolFound, String effectName)
	{
		int roll = event.getEntity().level.random.nextInt(100);
		if (roll < 75)
		{
			// 75% chance to block
			if (event.getEntityLiving() instanceof PlayerEntity)
			{
				String key = String.format("tearsofgaia.message.%sdenied", effectName);
				int color = effectName.equals("poison") ? 0x996633 : 0x0088cc; // earth and water
				((PlayerEntity) event.getEntityLiving()).displayClientMessage(new TranslationTextComponent(key, toolFound.getDisplayName().copy().withStyle(TextFormatting.GOLD).withStyle(Style.EMPTY.withColor(Color.fromRgb(color)))), true);
			}
			event.setResult(Event.Result.DENY);
		}
	}



	static boolean CheckItemsForElementalItem(ItemStack item, String element)
	{
		return CheckItemsForElementalItem(item, element, 1);
	}



	static boolean CheckItemsForElementalItem(ItemStack item, String element, int level)
	{
		if (item.getItem().getRegistryName().getNamespace().equals("tetra"))
		{
			String itemElement = ElementalHelper.GetItemElement(item);
			if (itemElement != null && itemElement.equals(element) && ElementalHelper.GetItemElementLevel(item) >= level)
			{
				return true;
			}
		}
		return false;
	}

	////////////////////////////////////////////////////////

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void OnLivingHurt(LivingHurtEvent event)
	{
		if (event.getSource().isExplosion() && ! event.getEntity().level.isClientSide)
		{
			System.out.println("!!!   blast dmg==" + event.getAmount());
			if (CheckItemsForElementalItem(event.getEntityLiving().getMainHandItem(), "water", 2))
			{
				event.setAmount(event.getAmount() / 2); //beneficial rounding. don't care really.
			}
			if (CheckItemsForElementalItem(event.getEntityLiving().getOffhandItem(), "water", 2))
			{
				event.setAmount(event.getAmount() / 2); //beneficial rounding. don't care really.
			}
			for (ItemStack item : event.getEntityLiving().getArmorSlots())
			{
				if (CheckItemsForElementalItem(item, "water", 2))
				{
					event.setAmount(event.getAmount() / 2); //beneficial rounding. don't care really.
				}
			}
			System.out.println("!!!---blast dmg==" + event.getAmount());
		}
	}
}
