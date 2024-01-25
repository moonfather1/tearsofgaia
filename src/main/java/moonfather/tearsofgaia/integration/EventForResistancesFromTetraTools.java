package moonfather.tearsofgaia.integration;

import moonfather.tearsofgaia.OptionsHolder;
import moonfather.tearsofgaia.forging.ElementalHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

@Mod.EventBusSubscriber
public class EventForResistancesFromTetraTools
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void OnPotionApplicableCheck(MobEffectEvent.Applicable event)
	{
		if (event.getEffectInstance().getEffect().equals(MobEffects.POISON))
		{
			// clientside event fires if i allow serverside. can't deny it on client, otherwise i get a poison but no indicator.
			if (! event.getEntity().level().isClientSide)
			{
				ItemStack tool = event.getEntity().getMainHandItem();
				if (CheckItemsForElementalItem(tool, "earth") && (tool.getDamageValue() == 0 || tool.getDamageValue() < tool.getMaxDamage() * 0.95))
				{
					if (RollSucceeded("poison"))
					{
						DenyEffect(event, tool, "poison");
						tool.setDamageValue(tool.getDamageValue() + Math.min((int) (tool.getMaxDamage() * 0.02), 15));
						return;
					}
				}
				tool = event.getEntity().getOffhandItem();
				if (CheckItemsForElementalItem(tool, "earth") && (tool.getDamageValue() == 0 || tool.getDamageValue() < tool.getMaxDamage() * 0.95))
				{
					if (RollSucceeded("poison"))
					{
						DenyEffect(event, tool, "poison");
						tool.setDamageValue(tool.getDamageValue() + Math.min((int) (tool.getMaxDamage() * 0.02), 15));
						return;
					}
				}
				for (ItemStack armor : event.getEntity().getArmorSlots())
				{
					if (CheckItemsForElementalItem(armor, "earth") && (armor.getDamageValue() == 0 || armor.getDamageValue() < armor.getMaxDamage() * 0.95))
					{
						if (RollSucceeded("poison"))
						{
							DenyEffect(event, armor, "poison");
							armor.setDamageValue(armor.getDamageValue() + Math.min((int) (armor.getMaxDamage() * 0.02), 15));
							return;
						}
					}
				}
				if (ModList.get().isLoaded("curios"))
				{
					for (ItemStack curio : CuriosInventory.GetFlatList(event.getEntity()))
					{
						if (CheckItemsForElementalItem(curio, "earth") && (curio.getDamageValue() == 0 || curio.getDamageValue() < curio.getMaxDamage() * 0.95))
						{
							if (RollSucceeded("poison"))
							{
								DenyEffect(event, curio, "poison");
								curio.setDamageValue(curio.getDamageValue() + Math.min((int) (curio.getMaxDamage() * 0.02), 15));
								return;
							}
						}
					}
				}
			}
		}
		else if (event.getEffectInstance().getEffect().equals(MobEffects.WITHER))
		{
			// clientside event fires if i allow serverside. can't deny it on client, otherwise i get a poison but no indicator.
			if (! event.getEntity().level().isClientSide)
			{
				ItemStack tool = event.getEntity().getMainHandItem();
				if (CheckItemsForElementalItem(tool, "water") && (tool.getDamageValue() == 0 || tool.getDamageValue() < tool.getMaxDamage() * 0.95))
				{
					if (RollSucceeded("wither"))
					{
						DenyEffect(event, tool, "wither");
						tool.setDamageValue(tool.getDamageValue() + Math.min((int) (tool.getMaxDamage() * 0.05), 25));
						return;
					}
				}
				tool = event.getEntity().getOffhandItem();
				if (CheckItemsForElementalItem(tool, "water") && (tool.getDamageValue() == 0 || tool.getDamageValue() < tool.getMaxDamage() * 0.95))
				{
					if (RollSucceeded("wither"))
					{
						DenyEffect(event, tool, "wither");
						tool.setDamageValue(tool.getDamageValue() + Math.min((int) (tool.getMaxDamage() * 0.05), 25));
						return;
					}
				}
				for (ItemStack armor : event.getEntity().getArmorSlots())
				{
					if (CheckItemsForElementalItem(armor, "water") && (armor.getDamageValue() == 0 || armor.getDamageValue() < armor.getMaxDamage() * 0.95))
					{
						if (RollSucceeded("wither"))
						{
							DenyEffect(event, armor, "wither");
							armor.setDamageValue(armor.getDamageValue() + Math.min((int) (armor.getMaxDamage() * 0.05), 25));
							return;
						}
					}
				}
				if (ModList.get().isLoaded("curios"))
				{
					for (ItemStack curio : CuriosInventory.GetFlatList(event.getEntity()))
					{
						if (CheckItemsForElementalItem(curio, "water") && (curio.getDamageValue() == 0 || curio.getDamageValue() < curio.getMaxDamage() * 0.95))
						{
							if (RollSucceeded("wither"))
							{
								DenyEffect(event, curio, "wither");
								curio.setDamageValue(curio.getDamageValue() + Math.min((int) (curio.getMaxDamage() * 0.05), 25));
								return;
							}
						}
					}
				}
			}
		}
	}



	private static void DenyEffect(MobEffectEvent.Applicable event, ItemStack toolFound, String effectName)
	{
		if (event.getEntity() instanceof Player)
		{
			String key = String.format("tearsofgaia.message.%sdenied", effectName);
			int color = effectName.equals("poison") ? 0x996633 : 0x0088cc; // earth and water
			((Player) event.getEntity()).displayClientMessage(Component.translatable(key, toolFound.getDisplayName().copy().withStyle(ChatFormatting.GOLD).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(color)))), true);
		}
		event.setResult(Event.Result.DENY);
	}

	private static boolean RollSucceeded(String type)
	{
		int percentLimit = 0;
		if (type.equals("poison"))
		{
			percentLimit = OptionsHolder.COMMON.TetraPercentageChanceResistingPoison.get();
		}
		if (type.equals("wither"))
		{
			percentLimit = OptionsHolder.COMMON.TetraPercentageChanceResistingWither.get();
		}
		// 75% chance to block
		return random.nextInt(100) < percentLimit;
	}
	private static final Random random = new Random();

	static boolean CheckItemsForElementalItem(ItemStack item, String element)
	{
		return CheckItemsForElementalItem(item, element, 1);
	}



	static boolean CheckItemsForElementalItem(ItemStack item, String element, int level)
	{
		if (ForgeRegistries.ITEMS.getKey(item.getItem()).getNamespace().equals("tetra"))
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
		if (event.getSource().is(DamageTypeTags.IS_EXPLOSION) && ! event.getEntity().level().isClientSide)
		{
			if (CheckItemsForElementalItem(event.getEntity().getMainHandItem(), "water", 2))
			{
				event.setAmount(event.getAmount() / 2); //beneficial rounding. don't care really.
			}
			if (CheckItemsForElementalItem(event.getEntity().getOffhandItem(), "water", 2))
			{
				event.setAmount(event.getAmount() / 2); //beneficial rounding. don't care really.
			}
			for (ItemStack item : event.getEntity().getArmorSlots())
			{
				if (CheckItemsForElementalItem(item, "water", 2))
				{
					event.setAmount(event.getAmount() / 2); //beneficial rounding. don't care really.
				}
			}
		}
	}
}
