package moonfather.tearsofgaia.item_abilities;

import moonfather.tearsofgaia.forging.ElementalHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;


@Mod.EventBusSubscriber
public class EventForItemGrantingFireImmunity
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void OnLivingHurt(LivingHurtEvent event)
	{
		if (event.isCanceled() || event.getEntity().level.isClientSide)
		{
			return;
		}
		if (!(event.getEntity() instanceof PlayerEntity) || !(event.getSource() == DamageSource.IN_FIRE || event.getSource() == DamageSource.ON_FIRE || event.getSource() == DamageSource.LAVA))
		{
			return;
		}

		PlayerEntity player = null;
		ItemStack item = ItemStack.EMPTY;
		Iterator i = playersWithFireLevel2Item.iterator();
		boolean foundInCache = false;
		while (i.hasNext())
		{
			PlayerRecord cached = (PlayerRecord) i.next();
			if (event.getEntity().level.getGameTime() > cached.recordCreationTimestamp + 20 * 12 /*12s*/)
			{
				i.remove();
				continue;
			}
			if (cached.player == ((PlayerEntity)event.getEntity()))
			{
				ItemStack stack = GetPlayersSlotContents(cached);
				if (stack != ItemStack.EMPTY && ItemIsntTooDamaged(stack) && ElementalHelper.IsItemElementEqual(stack, "fire") && ElementalHelper.GetItemElementLevel(stack) == 2)
				{
					player = cached.player;
					item = stack;
					cached.recordCreationTimestamp = event.getEntity().level.getGameTime();
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
			toCache = SearchForFire2((PlayerEntity) event.getEntity());
			if (toCache == null)
			{
				return; //before return store as false ?!?
			}
			player = (PlayerEntity) event.getEntity();
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
			return record.player.inventory.items.get(record.slot);
		}
		else if (record.inventoryId == 'A')
		{
			return record.player.inventory.armor.get(record.slot);
		}
		else if (record.inventoryId == 'O')
		{
			return record.player.inventory.offhand.get(record.slot);
		}
		return ItemStack.EMPTY;
	}



	private static PlayerRecord SearchForFire2(PlayerEntity player)
	{
		int slot = LookForFireLevel2(player.inventory.armor, 1000);
		if (slot >= 0)
		{
			return new PlayerRecord(player, player.level.getGameTime(), slot, 'A');
		}
		slot = LookForFireLevel2(player.inventory.offhand, 1000);
		if (slot >= 0)
		{
			return new PlayerRecord(player, player.level.getGameTime(), slot, 'O');
		}
		slot = LookForFireLevel2(player.inventory.items, 9);
		if (slot >= 0)
		{
			return new PlayerRecord(player, player.level.getGameTime(), slot, 'M');
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



	private static ArrayList<PlayerRecord> playersWithFireLevel2Item = new ArrayList<PlayerRecord>();

	private static class PlayerRecord
	{
		private PlayerEntity player;
		private long recordCreationTimestamp;
		private int slot;
		private char inventoryId;

		PlayerRecord(PlayerEntity player, long recordCreationTimestamp, int slot, char inventoryId)
		{
			this.player = player;
			this.recordCreationTimestamp = recordCreationTimestamp;
			this.slot = slot;
			this.inventoryId = inventoryId;
		}
	}
}
