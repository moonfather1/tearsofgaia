package moonfather.tearsofgaia.forging;

import moonfather.tearsofgaia.Constants;
import moonfather.tearsofgaia.RegistryManager;
import moonfather.tearsofgaia.enchantments.EnchantmentEasyRepair;
import moonfather.tearsofgaia.enchantments.EnchantmentSoulbound;
import moonfather.tearsofgaia.integration.IntegrationTinkersTools;
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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
public class EventForAnvil
{
	@SubscribeEvent
	public static void OnAnvilUpdate(AnvilUpdateEvent event)
	{
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();

		if (ElementalHelper.IsTear(right))
		{
			ElementalHelper.SetStartingTempTooltipTime(right);
		}
		if (ElementalHelper.IsTear(left))
		{
			ElementalHelper.SetStartingTempTooltipTime(left);
		}

		if (left.isEmpty() || right.isEmpty())
		{
			return;
		}
		else if (IntegrationTinkersTools.isTinkersTool(left) && ElementalHelper.IsTear(right) || IntegrationTinkersTools.isTinkersTool(right) && ElementalHelper.IsTear(left))
		{
			IntegrationTinkersTools.makeAndStoreResult(left, right, event);
		}
		else if (ElementalHelper.IsTear(right) && IsValidItem(left, ElementalHelper.GetTearElement(right), ElementalHelper.GetTearLevel(right)))
		{
			ImbueItem(left, right, event);
		}
		else if (ElementalHelper.IsTear(left) && IsValidItem(right, ElementalHelper.GetTearElement(left), ElementalHelper.GetTearLevel(left)))
		{
			ImbueItem(right, left, event);
		}
	}


	private static boolean IsValidItemForLevel2(ItemStack stack, String gemElement)
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



	private static boolean IsValidItemForLevel1(ItemStack stack, String element)
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



	private static boolean IsValidItem(ItemStack stack, String element, int level)
	{
		if (level == 1)
		{
			return IsValidItemForLevel1(stack, element);
		}
		else if (level == 2)
		{
			return IsValidItemForLevel2(stack, element);
		}
		else
		{
			return false;
		}
	}



	private static void ImbueItem(ItemStack stack, ItemStack gem, AnvilUpdateEvent event)
	{
		if (ElementalHelper.GetTearLevel(gem) > 1)
		{
			ImbueItemToLevel2(stack, gem, event);
			return;
		}

		String element = ElementalHelper.GetTearElement(gem);
		ItemStack output = stack.copy();
		output.setHoverName(event.getName() != null && !event.getName().equals("") ? new StringTextComponent(event.getName()) : stack.getHoverName());
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



	private static void ImbueItemToLevel2(ItemStack stack, ItemStack gem, AnvilUpdateEvent event)
	{
		String element = ElementalHelper.GetTearElement(gem);
		ItemStack output = stack.copy();
		output.setHoverName(event.getName() != null && !event.getName().equals("") ? new StringTextComponent(event.getName()) : stack.getDisplayName());
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
