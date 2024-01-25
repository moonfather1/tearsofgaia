package moonfather.tearsofgaia.item_abilities;

import moonfather.tearsofgaia.forging.ElementalHelper;
import moonfather.tearsofgaia.integration.CuriosInventory;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import java.util.List;

@Mod.EventBusSubscriber
public class EventForTotem
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void OnPlayerDeath(LivingDeathEvent event)
	{
		if (event.isCanceled() || event.getEntity().level().isClientSide || event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY))
		{
			return;
		}
		if (! (event.getEntity() instanceof Player))
		{
			return;
		}


		ItemStack found;
		Player player = (Player) event.getEntity();
		found = LookForAirLevel2(player.getInventory().armor);
		if (found == ItemStack.EMPTY)
		{
			found = LookForAirLevel2(player.getInventory().offhand);
		}
		if (found == ItemStack.EMPTY)
		{
			found = LookForAirLevel2(player.getInventory().items, 9);
		}
		if (found == ItemStack.EMPTY && ModList.get().isLoaded("curios"))
		{
			found = LookForAirLevel2(CuriosInventory.GetFlatList(player));
		}
		if (found == ItemStack.EMPTY)
		{
			return;
		}

		ElementalHelper.ReduceAirLevelTo1(found);

		event.setCanceled(true);
		if (player instanceof ServerPlayer)
		{
			ServerPlayer entityplayermp = (ServerPlayer)player;
			entityplayermp.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
			CriteriaTriggers.USED_TOTEM.trigger(entityplayermp, found);
		}

		player.setHealth(1.0F);
		player.removeAllEffects();
		player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
		player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
		player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
		player.level().broadcastEntityEvent(player, (byte)35);
		//System.out.println("EventHandlersForPlayerHarm.OnPlayerDeath()");
	}



	private static ItemStack LookForAirLevel2(List<ItemStack> inventory)
	{
		return LookForAirLevel2(inventory, 1000);
	}



	private static ItemStack LookForAirLevel2(List<ItemStack> inventory, int numberOfItemsToCheck)
	{
		return LookForElementLevel2(inventory, numberOfItemsToCheck, "air");
	}



	public static ItemStack LookForElementLevel2(List<ItemStack> inventory, int numberOfItemsToCheck, String element)
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
