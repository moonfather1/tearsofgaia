package moonfather.tearsofgaia.integration;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class IntegrationTinkersTools
{
	public static boolean isTinkersTool(ItemStack stack)
	{
		return ForgeRegistries.ITEMS.getKey(stack.getItem()).getNamespace().equals("tconstruct");
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
