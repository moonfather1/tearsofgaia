package moonfather.tearsofgaia.integration;

import moonfather.tearsofgaia.enchantments.EventForSoulbound;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.ArrayList;
import java.util.List;

public class CuriosInventory
{
	public static void PutItemBack(PlayerEntity player, ItemStack item, String location, int slot)
	{
		CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(
				(ih)->
				{
					ICurioStacksHandler sh = ih.getCurios().get(location.substring(7)); // "curios-belt" -> "belt"
					sh.getStacks().setStackInSlot(slot,item);
				}
		);
	}

	/*public static void CopyAllToClonedPlayer(PlayerEntity originalPlayer, PlayerEntity newPlayer)
	{
		CuriosApi.getCuriosHelper().getCuriosHandler(originalPlayer).ifPresent(     this one just returns false
				(ih)->
				{
					CuriosApi.getCuriosHelper().getCuriosHandler(newPlayer).ifPresent(
							(ih2)->
							{
								for (String slotName : ih.getCurios().keySet())
								{
									ICurioStacksHandler sh = ih.getCurios().get(slotName);
									for (int slot = 0; slot < sh.getStacks().getSlots(); slot++)
									{
										if (EventForSoulbound.IsItemSoulbound(sh.getStacks().getStackInSlot(slot)))
										{
											ItemStack itemToReturn = sh.getStacks().getStackInSlot(slot).copy();
											EventForSoulbound.ReduceLevelOfSoulbound(itemToReturn);
											ih2.getCurios().get(slotName).getStacks().setStackInSlot(slot, itemToReturn);
										}
									}
								}
							}
					);
				}
		);
	}*/



	public static void MarkItemsWithLocations(PlayerEntity player)
	{
		CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(
				(ih)->
				{
					ih.getCurios().forEach((key, value) ->
						{
							for (int i = 0; i < value.getStacks().getSlots(); i++)
							{
								if (EventForSoulbound.IsItemSoulbound(value.getStacks().getStackInSlot(i)))
								{
									CompoundNBT t = value.getStacks().getStackInSlot(i).getOrCreateTagElement("Soulbound_temp");
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
		CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(
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
