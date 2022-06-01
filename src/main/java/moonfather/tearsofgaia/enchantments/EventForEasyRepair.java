package moonfather.tearsofgaia.enchantments;

import moonfather.tearsofgaia.RegistryManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventForEasyRepair
{
	@SubscribeEvent
	public static void OnAnvilUpdate(AnvilUpdateEvent event)
	{
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();
		int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentEasyRepair.GetInstance(), left);
		if (enchantmentLevel == 0)
		{
			enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentEasyRepair.GetInstance(), right);
		}
		if (enchantmentLevel == 0)
		{
			return;
		}
		if (event.getCost() > EnchantmentEasyRepair.GetMaxAnvilCost(enchantmentLevel))
		{
			event.setCost(EnchantmentEasyRepair.GetMaxAnvilCost(enchantmentLevel));
		}
	}
}
