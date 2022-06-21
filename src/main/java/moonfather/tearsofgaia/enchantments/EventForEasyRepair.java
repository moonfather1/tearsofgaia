package moonfather.tearsofgaia.enchantments;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
			return; // doesn't have cheap repair enchantment
		}
		if (!left.getItem().isValidRepairItem(left, right))
		{
			return; // only for repairing, not imbuing or adding enchantments
		}
		int maxCost = EnchantmentEasyRepair.GetMaxAnvilCost(enchantmentLevel) + Math.min(right.getCount(), 4) - 1;
		if (event.getCost() > maxCost)
		{
			event.setCost(maxCost);
		}
		ItemStack output = left.copy();
		int newDamage = output.getDamageValue() - right.getCount() * output.getMaxDamage() / 4;
		newDamage = Math.max(newDamage, 0);
		output.setDamageValue(newDamage);
		event.setOutput(output);
	}
}
