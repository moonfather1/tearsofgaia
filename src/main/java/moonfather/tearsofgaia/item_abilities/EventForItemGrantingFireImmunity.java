package moonfather.tearsofgaia.item_abilities;

import moonfather.tearsofgaia.forging.ElementalHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;

import java.util.ArrayList;
import java.util.Iterator;


@Mod.EventBusSubscriber
public class EventForItemGrantingFireImmunity
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void OnLivingHurt(LivingHurtEvent event)
	{
		if (event.isCanceled() || event.getEntity().level().isClientSide)
		{
			return;
		}
		if (! (event.getEntity() instanceof Player) || ! event.getSource().is(DamageTypeTags.IS_FIRE))
		{
			return;
		}

		Player player = null;
		ItemStack item = ItemStack.EMPTY;
		Iterator<PlayerRecord> i = playersWithFireLevel2Item.iterator();
		boolean foundInCache = false;
		while (i.hasNext())
		{
			PlayerRecord cached = i.next();
			if (event.getEntity().level().getGameTime() > cached.recordCreationTimestamp + 20 * 12 /*12s*/)
			{
				i.remove();
				continue;
			}
			if (cached.player == event.getEntity())
			{
				ItemStack stack = GetPlayersSlotContents(cached);
				if (stack != ItemStack.EMPTY && ItemIsntTooDamaged(stack) && ElementalHelper.IsItemElementEqual(stack, "fire") && ElementalHelper.GetItemElementLevel(stack) == 2)
				{
					player = cached.player;
					item = stack;
					cached.recordCreationTimestamp = event.getEntity().level().getGameTime();
					foundInCache = true;
					break;
				}
				else
				{
					i.remove();
				}
			}
		}

		PlayerRecord toCache = null;
		if (item == ItemStack.EMPTY)
		{
			toCache = SearchForFire2((Player) event.getEntity());
			if (toCache == null)
			{
				return; //before return store as false ?!?
			}
			player = (Player) event.getEntity();
			item = GetPlayersSlotContents(toCache);
		}

		if (foundInCache == false)
		{
			playersWithFireLevel2Item.add(toCache);
		}

		int damage = (int) Math.floor((item.getMaxDamage() - item.getDamageValue()) * 0.1d);
		item.hurtAndBreak(damage, player, (p)->{});
		event.setAmount(0);
		player.setRemainingFireTicks(0);
		//player.setFlag(0,false);
	}



	private static boolean ItemIsntTooDamaged(ItemStack stack)
	{
		return stack.getDamageValue() < stack.getMaxDamage() - 16;
	}



	private static ItemStack GetPlayersSlotContents(PlayerRecord record)
	{
		if (record.inventoryId == 'M')
		{
			return record.player.getInventory().items.get(record.slot);
		}
		else if (record.inventoryId == 'A')
		{
			return record.player.getInventory().armor.get(record.slot);
		}
		else if (record.inventoryId == 'O')
		{
			return record.player.getInventory().offhand.get(record.slot);
		}
		return ItemStack.EMPTY;
	}



	private static PlayerRecord SearchForFire2(Player player)
	{
		int slot = LookForFireLevel2(player.getInventory().armor, 1000);
		if (slot >= 0)
		{
			return new PlayerRecord(player, player.level().getGameTime(), slot, 'A');
		}
		slot = LookForFireLevel2(player.getInventory().offhand, 1000);
		if (slot >= 0)
		{
			return new PlayerRecord(player, player.level().getGameTime(), slot, 'O');
		}
		slot = LookForFireLevel2(player.getInventory().items, 9);
		if (slot >= 0)
		{
			return new PlayerRecord(player, player.level().getGameTime(), slot, 'M');
		}
		return null;
	}



	private static int LookForFireLevel2(NonNullList<ItemStack> inventory, int numberOfItemsToCheck)
	{
		ItemStack current;
		for (int i = 0; i < inventory.size() && i < numberOfItemsToCheck; i++)
		{
			current = inventory.get(i);
			if (ItemIsntTooDamaged(current) && ElementalHelper.IsItemElementEqual(current, "fire") && ElementalHelper.GetItemElementLevel(current) == 2)
			{
				return i;
			}
		}
		return -1;
	}



	private static final ArrayList<PlayerRecord> playersWithFireLevel2Item = new ArrayList<PlayerRecord>();

	private static class PlayerRecord
	{
		private Player player;
		private long recordCreationTimestamp;
		private int slot;
		private char inventoryId;

		PlayerRecord(Player player, long recordCreationTimestamp, int slot, char inventoryId)
		{
			this.player = player;
			this.recordCreationTimestamp = recordCreationTimestamp;
			this.slot = slot;
			this.inventoryId = inventoryId;
		}
	}
}
