package moonfather.tearsofgaia.item_abilities;

import moonfather.tearsofgaia.forging.ElementalHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventForTotem
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void OnPlayerDeath(LivingDeathEvent event)
	{
		if (event.isCanceled() || event.getEntity().level.isClientSide || event.getSource().isBypassInvul())
		{
			return;
		}
		if (!(event.getEntity() instanceof PlayerEntity))
		{
			return;
		}


		ItemStack found;
		PlayerEntity player = (PlayerEntity) event.getEntity();
		found = LookForAirLevel2(player.inventory.armor);
		if (found == ItemStack.EMPTY)
		{
			found = LookForAirLevel2(player.inventory.offhand);
		}
		if (found == ItemStack.EMPTY)
		{
			found = LookForAirLevel2(player.inventory.items, 9);
		}
		if (found == ItemStack.EMPTY)
		{
			return;
		}

		ElementalHelper.ReduceAirLevelTo1(found);

		event.setCanceled(true);
		if (player instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity entityplayermp = (ServerPlayerEntity)player;
			entityplayermp.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
			CriteriaTriggers.USED_TOTEM.trigger(entityplayermp, found);
		}

		player.setHealth(1.0F);
		player.removeAllEffects();
		player.addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
		player.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
		player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
		player.level.broadcastEntityEvent(player, (byte)35);
		//System.out.println("EventHandlersForPlayerHarm.OnPlayerDeath()");
	}



	private static ItemStack LookForAirLevel2(NonNullList<ItemStack> inventory)
	{
		return LookForAirLevel2(inventory, 1000);
	}



	private static ItemStack LookForAirLevel2(NonNullList<ItemStack> inventory, int numberOfItemsToCheck)
	{
		return LookForElementLevel2(inventory, numberOfItemsToCheck, "air");
	}



	public static ItemStack LookForElementLevel2(NonNullList<ItemStack> inventory, int numberOfItemsToCheck, String element)
	{
		ItemStack current;
		for (int i = 0; i < inventory.size() && i < numberOfItemsToCheck; i++)
		{
			current = inventory.get(i);
			if (ElementalHelper.IsItemElementEqual(current, element) && ElementalHelper.GetItemElementLevel(current) == 2)
			{
				return current;
			}
		}
		return ItemStack.EMPTY;
	}
}
