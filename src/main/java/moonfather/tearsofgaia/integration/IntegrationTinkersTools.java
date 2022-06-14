package moonfather.tearsofgaia.integration;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class IntegrationTinkersTools
{
	public static boolean isTinkersTool(ItemStack stack)
	{
		return stack.getItem().getRegistryName().getNamespace().equals("tconstruct");
	}
}
