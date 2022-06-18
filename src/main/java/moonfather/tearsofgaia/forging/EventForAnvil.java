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



	private static boolean IsValidItem(ItemStack stack, String element, int level)
	{
		if (level == 1)
		{
			return AnvilHelper.getInstance(stack).IsValidItemForLevel1(stack, element);
		}
		else if (level == 2)
		{
			return AnvilHelper.getInstance(stack).IsValidItemForLevel2(stack, element);
		}
		else
		{
			return false;
		}
	}



	private static void ImbueItem(ItemStack stack, ItemStack gem, AnvilUpdateEvent event)
	{
		if (ElementalHelper.GetTearLevel(gem) == 1)
		{
			AnvilHelper.getInstance(stack).ImbueItemToLevel1(stack, ElementalHelper.GetTearElement(gem), event);
		}
		else
		{
			AnvilHelper.getInstance(stack).ImbueItemToLevel2(stack, ElementalHelper.GetTearElement(gem), event);
		}
	}
}
