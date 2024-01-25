package moonfather.tearsofgaia.enchantments;

import moonfather.tearsofgaia.integration.CuriosInventory;
import moonfather.tearsofgaia.integration.IntegrationTetra;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Iterator;

@Mod.EventBusSubscriber
public class EventForSoulbound
{
	@SubscribeEvent
	public static void OnClonePlayer(PlayerEvent.Clone event)
	{
		if (! event.isWasDeath() /* changed dimension */ || event.isCanceled())
		{
			return;
		}

		OnClonePlayerInternal(event.getOriginal().getInventory().armor, event.getEntity().getInventory().armor);
		OnClonePlayerInternal(event.getOriginal().getInventory().items, event.getEntity().getInventory().items);
		OnClonePlayerInternal(event.getOriginal().getInventory().offhand, event.getEntity().getInventory().offhand);
	}



	@SubscribeEvent
	public static void OnPlayerDrops(LivingDropsEvent event)
	{
		if (! event.getEntity().level().isClientSide() && event.getEntity() instanceof Player)
		{
			Player player = (Player) event.getEntity();
			Iterator i = event.getDrops().iterator();
			while (i.hasNext())
			{
				ItemEntity ei = (ItemEntity) i.next();
				if (IsItemSoulbound(ei))
				{
					CompoundTag t = ei.getItem().getOrCreateTagElement("Soulbound_temp");
					String itemListId = t.getString("inv");
					int slot = t.getInt("slot");
					if (itemListId.equals("armor"))
					{
						player.getInventory().armor.set(slot, ei.getItem().copy());
					}
					else if (itemListId.equals("main"))
					{
						player.getInventory().items.set(slot, ei.getItem().copy());
					}
					else if (itemListId.equals("offh"))
					{
						player.getInventory().offhand.set(slot, ei.getItem().copy());
					}
					else if (itemListId.startsWith("curios"))
					{
						ItemStack itemToPutBack = ei.getItem().copy();
						ReduceLevelOfSoulbound(itemToPutBack); // because we can't do this in clone event
						CuriosInventory.PutItemBack(player, itemToPutBack, itemListId, slot);
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



	private static boolean IsItemSoulbound(ItemEntity ei)
	{
		return IsItemSoulbound(ei.getItem());
	}

	public static boolean IsItemSoulbound(ItemStack item)
	{
		return EnchantmentHelper.getItemEnchantmentLevel(EnchantmentSoulbound.GetInstance(), item) > 0 || IntegrationTetra.IsASoulboundTool(item);
	}



	@SubscribeEvent
	public static void OnDeath(LivingDeathEvent event)
	{
		if (! event.isCanceled() && ! event.getEntity().level().isClientSide() && event.getEntity() instanceof Player)
		{
			Player player = (Player) event.getEntity();
			OnDeathInternal(player.getInventory().armor, "armor");
			OnDeathInternal(player.getInventory().items, "main");
			OnDeathInternal(player.getInventory().offhand, "offh");
			if (ModList.get().isLoaded("curios"))
			{
				CuriosInventory.MarkItemsWithLocations(player);
			}
		}
	}

	private static void OnDeathInternal(NonNullList<ItemStack> itemList, String itemListId)
	{
		for (int i = 0; i < itemList.size(); i++)
		{
			if (IsItemSoulbound(itemList.get(i)))
			{
				CompoundTag t = itemList.get(i).getOrCreateTagElement("Soulbound_temp");
				t.putString("inv", itemListId);
				t.putInt("slot", i);
			}
		}
	}



	private static void OnClonePlayerInternal(NonNullList<ItemStack> oldInventory, NonNullList<ItemStack> newInventory)
	{
		for (int i = 0; i < oldInventory.size(); i++)
		{
			if (IsItemSoulbound(oldInventory.get(i)))
			{
				ItemStack itemToReturn = oldInventory.get(i).copy();
				ReduceLevelOfSoulbound(itemToReturn);
				newInventory.set(i, itemToReturn);
			}
		}
	}



	public static void ReduceLevelOfSoulbound(ItemStack itemToReturn)
	{
		if (ForgeRegistries.ITEMS.getKey(itemToReturn.getItem()).getNamespace().equals("tetra"))
		{
			IntegrationTetra.ReduceLevelOfSoulbound(itemToReturn);
			ReduceLevelOfSoulboundInternal(itemToReturn); // also here to remove enchantments placed before van->tetra conversion
		}
		else
		{
			ReduceLevelOfSoulboundInternal(itemToReturn);
		}
	}

	private static void ReduceLevelOfSoulboundInternal(ItemStack itemToReturn)
	{
		ListTag nbttaglist = itemToReturn.getEnchantmentTags();
		for (int i = 0; i < nbttaglist.size(); ++i)
		{
			CompoundTag nbttagcompound = nbttaglist.getCompound(i);
			Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(ResourceLocation.tryParse(nbttagcompound.getString("id")));
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