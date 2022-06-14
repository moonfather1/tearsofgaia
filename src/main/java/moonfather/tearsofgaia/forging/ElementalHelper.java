package moonfather.tearsofgaia.forging;

import moonfather.tearsofgaia.Constants;
import moonfather.tearsofgaia.items.ItemGem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.IntNBT;

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



	public static int GetTempTooltipTime(ItemStack stack)
	{
		if (stack.isEmpty() || stack.getTag() == null || !stack.getTag().contains(Constants.TAG_KEY_TTT_TIME))
		{
		//	System.out.println("---- GetTempTooltipTime " + stack.hashCode() + "->0");
			return 0;
		}
		//System.out.println("---- GetTempTooltipTime " + stack.hashCode() + "->" + stack.getTagCompound().getInteger(Constants.TAG_KEY_TTT_TIME));
		return stack.getTag().getInt(Constants.TAG_KEY_TTT_TIME);
	}



	public static void UpdateTempTooltipTime(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			//System.out.println("---- UpdateTempTooltipTime " + stack.hashCode() + ",NIL");
			return;
		}
		int current = GetTempTooltipTime(stack);
		if (current > 1)
		{
			stack.getOrCreateTag().putInt(Constants.TAG_KEY_TTT_TIME, current - 1);
		}
		else
		{
			stack.getOrCreateTag().remove(Constants.TAG_KEY_TTT_TIME);
		}
		//int current2 = stack.getTag().getInt(Constants.TAG_KEY_TTT_TIME);
		//System.out.println("---- UpdateTempTooltipTime " + stack.hashCode() + ":    " + current + "->" + newTime + "   ====>" + current2);
	}



	public static void SetStartingTempTooltipTime(ItemStack stack)
	{
		stack.getOrCreateTag().putInt(Constants.TAG_KEY_TTT_TIME, 500);   // tried 100; was about 1s while i expected 5s.
	}



	public static int GetTearLevel(ItemStack gem) {	return ((ItemGem) gem.getItem()).GetLevel();  }



	public static void ReduceAirLevelTo1(ItemStack stack)
	{
		stack.getOrCreateTag().putInt(Constants.TAG_KEY_LEVEL, 1);
	}
}
