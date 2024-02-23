package moonfather.tearsofgaia.forging;

import moonfather.tearsofgaia.Constants;
import moonfather.tearsofgaia.enchantments.EnchantmentEasyRepair;
import moonfather.tearsofgaia.enchantments.EnchantmentSoulbound;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

public class AnvilHelperVanilla extends AnvilHelper
{
	@Override
	public boolean IsValidItemForLevel1(ItemStack stack, String element)
	{
		String existingElement = ElementalHelper.GetItemElement(stack);
		if (existingElement != null && ! (element.equals("earth") && existingElement.equals("earth") && ElementalHelper.GetItemElementLevel(stack) == 1))
		{
			return false; // already imbued
		}
		else if (stack.getItem() == Items.ENCHANTED_BOOK)
		{
			return false;
		}
		else if (element.equals("earth"))
		{
			return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) < Enchantments.UNBREAKING.getMaxLevel() && Enchantments.UNBREAKING.canEnchant(stack);
		}
		else if (element.equals("water"))
		{
			return (stack.isEnchanted() || stack.isEnchantable()) && stack.isDamageableItem();
		}
		else if (element.equals("air"))
		{
			//used to be IsEquipableArmorOrShieldOrHorseEquipement(stack) but we're now letting anything through
			return  IsNonStackable(stack);
		}
		else if (element.equals("fire"))
		{
			return  IsNonStackable(stack);
		}
		else
		{
			return false;
		}
	}



	@Override
	public boolean IsValidItemForLevel2(ItemStack stack, String gemElement)
	{
		if (stack.getItem() == Items.ENCHANTED_BOOK)
		{
			return false;
		}
		String itemElement = ElementalHelper.GetItemElement(stack);
		int itemLevel = 0;
		if (itemElement == null)
		{
			return false; // not imbued at all
		}
		else if (!itemElement.equals(gemElement))
		{
			return false; // wrong element
		}
		else if ((itemLevel = ElementalHelper.GetItemElementLevel(stack)) != 1)
		{
			if (itemLevel == 2 && itemElement.equals("earth") && EnchantmentHelper.getItemEnchantmentLevel(EnchantmentSoulbound.GetInstance(), stack) < EnchantmentSoulbound.GetInstance().getMaxLevel())
			{
				return true; // spent soulbound; allow refill
			}
			return false; // not imbued to level 1
		}
		else if (gemElement.equals("fire"))
		{
			// finally something element-dependant
			return stack.isDamageableItem();
		}
		else
		{
			return true;
		}
	}



	@Override
	public void ImbueItemToLevel1(ItemStack stack, String element, AnvilUpdateEvent event)
	{
		ItemStack output = stack.copy();
		if (event.getName() != null && !event.getName().equals(""))
		{
			output.setHoverName(Component.literal(event.getName()));
		}
		output.getOrCreateTag().putString(Constants.TAG_KEY_ELEMENT, element);
		output.getOrCreateTag().putInt(Constants.TAG_KEY_LEVEL, 1);
		if (element.equals("earth"))
		{
			ListTag nbttaglist = output.getEnchantmentTags();
			int i = 0;
			boolean increased = false;
			while (i < nbttaglist.size())
			{
				CompoundTag nbttagcompound = nbttaglist.getCompound(i);
				Enchantment enchantment = BuiltInRegistries.ENCHANTMENT.get(ResourceLocation.tryParse(nbttagcompound.getString("id")));
				if (enchantment == Enchantments.UNBREAKING)
				{
					int level = nbttagcompound.getShort("lvl");
					if (level < Enchantments.UNBREAKING.getMaxLevel())
					{
						nbttagcompound.putShort("lvl", (short)(level + 1));
					}
					increased = true;
					break;
				}
				i += 1;
			}
			if (! increased)
			{
				output.enchant(Enchantments.UNBREAKING, 1);
			}
		}
		else if (element.equals("water"))
		{
			output.setDamageValue(output.getDamageValue() / 2);

			ListTag nbttaglist = output.getEnchantmentTags();
			boolean increasedRepair = false;
			int i = 0;
			while (i < nbttaglist.size())
			{
				CompoundTag nbttagcompound = nbttaglist.getCompound(i);
				Enchantment enchantment = BuiltInRegistries.ENCHANTMENT.get(ResourceLocation.tryParse(nbttagcompound.getString("id")));
				if (enchantment != null && enchantment.isCurse())
				{
					nbttaglist.remove(i);
				}
				else if (enchantment == EnchantmentEasyRepair.GetInstance())
				{
					int level = nbttagcompound.getShort("lvl");
					if (level < EnchantmentEasyRepair.GetInstance().getMaxLevel())
					{
						nbttagcompound.putShort("lvl", (short)(level + 1));
					}
					increasedRepair = true;
					i += 1;
				}
				else
				{
					i = i + 1;
				}
			}
			if (! increasedRepair)
			{
				output.enchant(EnchantmentEasyRepair.GetInstance(), 1);
			}
		}
		else if (element.equals("air"))
		{
		}
		else if (element.equals("fire"))
		{
		}
		event.setOutput(output);
		event.setMaterialCost(1);
		event.setCost(output.getHoverName().equals(stack.getHoverName()) ? 1 : 2);
	}



	@Override
	public void ImbueItemToLevel2(ItemStack stack, String element, AnvilUpdateEvent event)
	{
		ItemStack output = stack.copy();
		if (event.getName() != null && !event.getName().equals(""))
		{
			output.setHoverName(Component.literal(event.getName()));
		}
		output.getOrCreateTag().putString(Constants.TAG_KEY_ELEMENT, element);
		output.getOrCreateTag().putInt(Constants.TAG_KEY_LEVEL, 2);
		if (element.equals("earth"))
		{
			output.setDamageValue((int) Math.floor(output.getDamageValue() * 0.75));

			ListTag nbttaglist = output.getEnchantmentTags();
			int i = 0; boolean done = false;
			while (i < nbttaglist.size())
			{
				CompoundTag nbttagcompound = nbttaglist.getCompound(i);
				Enchantment enchantment = BuiltInRegistries.ENCHANTMENT.get(ResourceLocation.tryParse(nbttagcompound.getString("id")));
				if (enchantment == EnchantmentSoulbound.GetInstance())
				{
					int level = nbttagcompound.getShort("lvl");
					if (level < enchantment.getMaxLevel())
					{
						nbttagcompound.putShort("lvl", (short) (level + 1));
					}
					done = true;
					break;
				}
				i += 1;
			}
			if (!done)
			{
				output.enchant(EnchantmentSoulbound.GetInstance(), 1);
			}
		}
		else if (element.equals("water"))
		{
			output.setDamageValue((int) Math.floor(output.getDamageValue() * 0.5));

			ListTag nbttaglist = output.getEnchantmentTags();
			int i = 0;
			while (i < nbttaglist.size())
			{
				CompoundTag nbttagcompound = nbttaglist.getCompound(i);
				Enchantment enchantment = BuiltInRegistries.ENCHANTMENT.get(ResourceLocation.tryParse(nbttagcompound.getString("id")));
				if (enchantment == null)
				{
					i = i + 1;
					continue;
				}
				else if (enchantment.isCurse())
				{
					nbttaglist.remove(i);
				}
				else
				{
					if (enchantment.getMaxLevel() > 1)
					{
						int level = nbttagcompound.getShort("lvl");
						nbttagcompound.putShort("lvl", (short)(level + 1));
					}
					i = i + 1;
				}
			}
		}
		else if (element.equals("air"))
		{
		}
		else if (element.equals("fire"))
		{
		}
		event.setOutput(output);
		event.setMaterialCost(1);
		event.setCost(output.getDisplayName().getString().equals(stack.getDisplayName().getString()) ? 10 : 11);
	}



	private static boolean IsEquipableArmorOrShieldOrHorseEquipement(ItemStack stack)
	{
		if (stack.getEquipmentSlot() != null && stack.getEquipmentSlot() != EquipmentSlot.MAINHAND)
		{
			return true;
		}
		if (stack.isEnchanted() || stack.getItem().isEnchantable(stack) || stack.getItem() instanceof TieredItem)
		{
			return true;
		}
		if (stack.getItem() instanceof SaddleItem || stack.getItem() instanceof HorseArmorItem)
		{
			return true;
		}
		return false;
	}

	private static boolean IsNonStackable(ItemStack stack)
	{
		return stack.getMaxStackSize() == 1;
	}
}
