package moonfather.tearsofgaia.integration;

import moonfather.tearsofgaia.enchantments.EventForSoulbound;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.ArrayList;
import java.util.List;

public class CuriosInventory
{
	public static void PutItemBack(Player player, ItemStack item, String location, int slot)
	{
		CuriosApi.getCuriosInventory(player).ifPresent(
				(ih)->
				{
					ICurioStacksHandler sh = ih.getCurios().get(location.substring(7)); // "curios-belt" -> "belt"
					sh.getStacks().setStackInSlot(slot,item);
				}
		);
	}



	public static void MarkItemsWithLocations(Player player)
	{
		CuriosApi.getCuriosInventory(player).ifPresent(
				(ih)->
				{
					ih.getCurios().forEach((key, value) ->
						{
							for (int i = 0; i < value.getStacks().getSlots(); i++)
							{
								if (EventForSoulbound.IsItemSoulbound(value.getStacks().getStackInSlot(i)))
								{
									CompoundTag t = value.getStacks().getStackInSlot(i).getOrCreateTagElement("Soulbound_temp");
									t.putString("inv", "curios-" + key);
									t.putInt("slot", i);
								}
							}
						}
					);
				}
		);
	}

	public static List<ItemStack> GetFlatList(LivingEntity entity)
	{
		List<ItemStack> result = new ArrayList<>();
		CuriosApi.getCuriosInventory(entity).ifPresent(
				(ih)->
				{
					for (ICurioStacksHandler sh : ih.getCurios().values())
					{
						int slotCount = sh.getSlots();
						for (int i = 0; i < slotCount; i++)
						{
							result.add(sh.getStacks().getStackInSlot(i));
						}
					}
				}
		);
		return result;
	}
}
