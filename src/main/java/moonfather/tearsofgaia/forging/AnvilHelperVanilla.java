package moonfather.tearsofgaia.forging;

import moonfather.tearsofgaia.Constants;
import moonfather.tearsofgaia.enchantments.EnchantmentEasyRepair;
import moonfather.tearsofgaia.enchantments.EnchantmentSoulbound;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.AnvilUpdateEvent;

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
			return  IsEquipableArmorOrShieldOrHorseEquipement(stack);//return stack.getItem().getItemStackLimit(stack) == 1;
		}
		else if (element.equals("fire"))
		{
			return  IsEquipableArmorOrShieldOrHorseEquipement(stack);
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
			output.setHoverName(new StringTextComponent(event.getName()));
		}
		output.getOrCreateTag().putString(Constants.TAG_KEY_ELEMENT, element);
		output.getOrCreateTag().putInt(Constants.TAG_KEY_LEVEL, 1);
		if (element.equals("earth"))
		{
			ListNBT nbttaglist = output.getEnchantmentTags();
			int i = 0;
			boolean increased = false;
			while (i < nbttaglist.size())
			{
				CompoundNBT nbttagcompound = nbttaglist.getCompound(i);
				Enchantment enchantment = Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(nbttagcompound.getString("id"))).orElse(null);
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

			ListNBT nbttaglist = output.getEnchantmentTags();
			boolean increasedRepair = false;
			int i = 0;
			while (i < nbttaglist.size())
			{
				CompoundNBT nbttagcompound = nbttaglist.getCompound(i);
				Enchantment enchantment = Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(nbttagcompound.getString("id"))).orElse(null);
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
			output.setHoverName(new StringTextComponent(event.getName()));
		}
		output.getOrCreateTag().putString(Constants.TAG_KEY_ELEMENT, element);
		output.getOrCreateTag().putInt(Constants.TAG_KEY_LEVEL, 2);
		if (element.equals("earth"))
		{
			output.setDamageValue((int) Math.floor(output.getDamageValue() * 0.75));

			ListNBT nbttaglist = output.getEnchantmentTags();
			int i = 0; boolean done = false;
			while (i < nbttaglist.size())
			{
				CompoundNBT nbttagcompound = nbttaglist.getCompound(i);
				Enchantment enchantment = Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(nbttagcompound.getString("id"))).orElse(null);
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

			ListNBT nbttaglist = output.getEnchantmentTags();
			int i = 0;
			while (i < nbttaglist.size())
			{
				CompoundNBT nbttagcompound = nbttaglist.getCompound(i);
				Enchantment enchantment = Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(nbttagcompound.getString("id"))).orElse(null);
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
		event.setCost(output.getDisplayName().equals(stack.getDisplayName()) ? 10 : 11);
	}



	private static boolean IsEquipableArmorOrShieldOrHorseEquipement(ItemStack stack)
	{
		if (stack.getEquipmentSlot() != null && stack.getEquipmentSlot() != EquipmentSlotType.MAINHAND)
		{
			return true;
		}
		if (stack.isEnchanted() || stack.getItem().isEnchantable(stack) || stack.getItem() instanceof ToolItem)
		{
			return true;
		}
		if (stack.getItem() instanceof SaddleItem || stack.getItem() instanceof HorseArmorItem)
		{
			return true;
		}
		return false;
	}
}
