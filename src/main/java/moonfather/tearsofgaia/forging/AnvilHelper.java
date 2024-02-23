package moonfather.tearsofgaia.forging;

import moonfather.tearsofgaia.integration.AnvilHelperTetra;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

public abstract class AnvilHelper
{
	private static AnvilHelper vanillaInstance = null;
	private static AnvilHelper tetraInstance = null;

	protected AnvilHelper() { }

	public static AnvilHelper getInstance(Item item)
	{
		if (BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("tetra"))
		{
			if (tetraInstance == null)
			{
				tetraInstance = new AnvilHelperTetra();
			}
			return tetraInstance;
		}
		else
		{
			if (vanillaInstance == null)
			{
				vanillaInstance = new AnvilHelperVanilla();
			}
			return vanillaInstance;
		}
	}

	public static AnvilHelper getInstance(ItemStack stack)
	{
		return getInstance(stack.getItem());
	}

	public abstract boolean IsValidItemForLevel1(ItemStack stack, String element);
	public abstract boolean IsValidItemForLevel2(ItemStack stack, String element);

	public abstract void ImbueItemToLevel1(ItemStack stack, String gemElement, AnvilUpdateEvent event);
	public abstract void ImbueItemToLevel2(ItemStack stack, String gemElement, AnvilUpdateEvent event);
}
