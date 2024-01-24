package moonfather.tearsofgaia.forging;

import moonfather.tearsofgaia.Constants;
import moonfather.tearsofgaia.items.ItemGem;
import net.minecraft.world.item.ItemStack;

public class ElementalHelper
{
	public static String GetItemElement(ItemStack stack)
	{
		if (stack.isEmpty() || stack.getTag() == null || !stack.getTag().contains(Constants.TAG_KEY_ELEMENT))
		{
			return null;
		}
		return stack.getTag().getString(Constants.TAG_KEY_ELEMENT);
	}



	public static int GetItemElementLevel(ItemStack stack)
	{
		if (stack.isEmpty() || stack.getTag() == null || !stack.getTag().contains(Constants.TAG_KEY_LEVEL))
		{
			return 1;
		}
		return stack.getTag().getInt(Constants.TAG_KEY_LEVEL);
	}



	public static boolean IsItemElementEqual(ItemStack stack, String element)
	{
		String temp = GetItemElement(stack);
		return temp != null ? temp.equals(element) : (element == null);
	}



	public static boolean IsElementalTool(ItemStack stack)
	{
		if (stack.isEmpty() || stack.getTag() == null || !stack.getTag().contains(Constants.TAG_KEY_ELEMENT))
		{
			return false;
		}
		return true;
	}



	public static boolean IsTearOrElementalTool(ItemStack stack)
	{
		return IsTear(stack) || IsElementalTool(stack);
	}



	public static boolean IsTear(ItemStack stack)
	{
		return !stack.isEmpty() && stack.getItem() instanceof ItemGem;
	}



	public static String GetTearElement(ItemStack gem)
	{
		return ((ItemGem) gem.getItem()).GetElement();
	}



	public static int GetTearLevel(ItemStack gem) {	return ((ItemGem) gem.getItem()).GetLevel();  }



	public static void ReduceAirLevelTo1(ItemStack stack)
	{
		stack.getOrCreateTag().putInt(Constants.TAG_KEY_LEVEL, 1);
	}



	public static int GetNamedIntValue(ItemStack stack, String tagName)
	{
		if (stack.isEmpty() || stack.getTag() == null || ! stack.getTag().contains(tagName))
		{
			return 0;
		}
		return stack.getTag().getInt(tagName);
	}

	public static void PutNamedIntValue(ItemStack stack, String tagName, int value)
	{
		if (stack.isEmpty())
		{
			return;
		}
		if (value <= 0)
		{
			stack.getOrCreateTag().remove(tagName);
			return;
		}
		stack.getOrCreateTag().putInt(tagName, value);
	}
}
