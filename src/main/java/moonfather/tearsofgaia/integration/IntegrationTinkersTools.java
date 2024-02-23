package moonfather.tearsofgaia.integration;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

public class IntegrationTinkersTools
{
	public static boolean isTinkersTool(ItemStack stack)
	{
		return BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals("tconstruct");
	}

	public static void makeAndStoreResult(ItemStack left, ItemStack right, AnvilUpdateEvent event)
	{
		ItemStack lorePotato = new ItemStack(Items.POISONOUS_POTATO);
		lorePotato.getOrCreateTag().putString("tog_message_for_tc", "special");
		lorePotato.setHoverName(left.getHoverName());
		event.setOutput(lorePotato);
		event.setCost(31568);
	}
}
