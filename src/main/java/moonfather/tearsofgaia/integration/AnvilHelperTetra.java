package moonfather.tearsofgaia.integration;

import moonfather.tearsofgaia.Constants;
import moonfather.tearsofgaia.enchantments.EnchantmentSoulbound;
import moonfather.tearsofgaia.forging.AnvilHelperVanilla;
import moonfather.tearsofgaia.forging.ElementalHelper;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.AnvilUpdateEvent;

public class AnvilHelperTetra extends AnvilHelperVanilla
{
	@Override
	public boolean IsValidItemForLevel1(ItemStack stack, String element)
	{
		String existingElement = ElementalHelper.GetItemElement(stack);
		if (existingElement != null)
		{
			return false; // already imbued
		}
		else if (stack.getItem() == Items.ENCHANTED_BOOK)
		{
			return false;
		}
		return stack.getMaxStackSize() == 1;
	}


	public static final String TAG_KEY_SOULBOUND_LEVEL = "tog_tetra_SB_level";


	@Override
	public boolean IsValidItemForLevel2(ItemStack stack, String gemElement)
	{
		if (ElementalHelper.GetItemElementLevel(stack) == 2)
		{
			String existingElement = ElementalHelper.GetItemElement(stack);
			if (existingElement != null && gemElement.equals("earth") && existingElement.equals("earth") && ElementalHelper.GetNamedIntValue(stack, TAG_KEY_SOULBOUND_LEVEL) < EnchantmentSoulbound.GetInstance().getMaxLevel())
			{
				return true;
			}
			return false;
		}
		return super.IsValidItemForLevel2(stack, gemElement);
	}



	@Override
	public void ImbueItemToLevel1(ItemStack stack, String element, AnvilUpdateEvent event)
	{
		if (element.equals("earth"))
		{
			ItemStack output = stack.copy();
			if (event.getName() != null && ! event.getName().equals(""))
			{
				output.setHoverName(Component.literal(event.getName()));
			}
			output.getOrCreateTag().putString(Constants.TAG_KEY_ELEMENT, element);
			output.getOrCreateTag().putInt(Constants.TAG_KEY_LEVEL, 1);
			event.setOutput(output);
			event.setMaterialCost(1);
			event.setCost(output.getHoverName().equals(stack.getHoverName()) ? 1 : 2);
		}
		else if (element.equals("water"))
		{
			ItemStack output = stack.copy();
			output.setDamageValue(output.getDamageValue() / 2); //i'll consider this
			if (event.getName() != null && ! event.getName().equals(""))
			{
				output.setHoverName(Component.literal(event.getName()));
			}
			output.getOrCreateTag().putString(Constants.TAG_KEY_ELEMENT, element);
			output.getOrCreateTag().putInt(Constants.TAG_KEY_LEVEL, 1);
			event.setOutput(output);
			event.setMaterialCost(1);
			event.setCost(output.getHoverName().equals(stack.getHoverName()) ? 1 : 2);
		}
		else
		{
			super.ImbueItemToLevel1(stack, element, event);
		}
	}


	@Override
	public void ImbueItemToLevel2(ItemStack stack, String element, AnvilUpdateEvent event)
	{
		ItemStack output = stack.copy();
		if (event.getName() != null && ! event.getName().equals(""))
		{
			output.setHoverName(Component.literal(event.getName()));
		}
		output.getOrCreateTag().putString(Constants.TAG_KEY_ELEMENT, element);
		output.getOrCreateTag().putInt(Constants.TAG_KEY_LEVEL, 2);
		if (element.equals("earth"))
		{
			int soulBoundLevel = ElementalHelper.GetNamedIntValue(output, TAG_KEY_SOULBOUND_LEVEL);
			ElementalHelper.PutNamedIntValue(output, TAG_KEY_SOULBOUND_LEVEL, soulBoundLevel + 1);
		}
		if (element.equals("water"))
		{
			output.setDamageValue((int) Math.floor(output.getDamageValue() * 0.5));
		}
		event.setOutput(output);
		event.setMaterialCost(1);
		event.setCost(output.getDisplayName().getString().equals(stack.getDisplayName().getString()) ? 10 : 11);
	}
}
