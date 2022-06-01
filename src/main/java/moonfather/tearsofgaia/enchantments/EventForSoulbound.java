package moonfather.tearsofgaia.enchantments;

import moonfather.tearsofgaia.RegistryManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;

@Mod.EventBusSubscriber
public class EventForSoulbound
{
	@SubscribeEvent
	public static void OnClonePlayer(PlayerEvent.Clone event)
	{
		if (!event.isWasDeath() /* changed dimension */ || event.isCanceled())
		{
			return;
		}

		OnClonePlayerInternal(event.getOriginal().inventory.armor, event.getPlayer().inventory.armor);
		OnClonePlayerInternal(event.getOriginal().inventory.items, event.getPlayer().inventory.items);
		OnClonePlayerInternal(event.getOriginal().inventory.offhand, event.getPlayer().inventory.offhand);
	}



	@SubscribeEvent
	public static void OnPlayerDrops(LivingDropsEvent event)
	{
		if (! event.getEntity().level.isClientSide() && event.getEntity() instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) event.getEntity();
			Iterator i = event.getDrops().iterator();
			while (i.hasNext())
			{
				ItemEntity ei = (ItemEntity) i.next();
				if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentSoulbound.GetInstance(), ei.getItem()) > 0)
				{
					CompoundNBT t = ei.getItem().getOrCreateTagElement("Soulbound_temp");
					String itemListId = t.getString("inv");
					int slot = t.getInt("slot");
					if (itemListId.equals("armor"))
					{
						player.inventory.armor.set(slot, ei.getItem().copy());
					}
					else if (itemListId.equals("main"))
					{
						player.inventory.items.set(slot, ei.getItem().copy());
					}
					else if (itemListId.equals("offh"))
					{
						player.inventory.offhand.set(slot, ei.getItem().copy());
					}
					else
					{
						continue;
					}
					t.remove("Soulbound_temp");
					i.remove();
				}
			}
		}
	}



	@SubscribeEvent
	public static void OnDeath(LivingDeathEvent event)
	{
		if (!event.isCanceled() && ! event.getEntity().level.isClientSide() && event.getEntity() instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) event.getEntity();
			OnDeathInternal(player.inventory.armor, "armor");
			OnDeathInternal(player.inventory.items, "main");
			OnDeathInternal(player.inventory.offhand, "offh");
		}
	}

	private static void OnDeathInternal(NonNullList<ItemStack> itemList, String itemListId)
	{
		int level = 0;
		for (int i = 0; i < itemList.size(); i++)
		{
			level = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentSoulbound.GetInstance(), itemList.get(i));
			if (level > 0)
			{
				CompoundNBT t = itemList.get(i).getOrCreateTagElement("Soulbound_temp");
				t.putString("inv", itemListId);
				t.putInt("slot", i);
			}
		}
	}



	private static void OnClonePlayerInternal(NonNullList<ItemStack> oldInventory, NonNullList<ItemStack> newInventory)
	{
		int level = 0;
		for (int i = 0; i < oldInventory.size(); i++)
		{
			level = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentSoulbound.GetInstance(), oldInventory.get(i));
			if (level > 0)
			{
				ItemStack itemToReturn = oldInventory.get(i).copy();
				ReduceLevelOfSoulbound(itemToReturn);
				newInventory.set(i, itemToReturn);
			}
		}
	}



	private static void ReduceLevelOfSoulbound(ItemStack itemToReturn)
	{
		ListNBT nbttaglist = itemToReturn.getEnchantmentTags();
		for (int i = 0; i < nbttaglist.size(); ++i)
		{
			CompoundNBT nbttagcompound = nbttaglist.getCompound(i);
			Enchantment enchantment = Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(nbttagcompound.getString("id"))).orElse(null);
			if (! (enchantment == EnchantmentSoulbound.GetInstance()))
			{
				continue;
			}
			int level = nbttagcompound.getShort("lvl");
			if (level > 1)
			{
				nbttagcompound.putShort("lvl", (short) (level - 1));
			}
			else if (level == 1)
			{
				nbttaglist.remove(i);
				break;
			}
		}
	}
}