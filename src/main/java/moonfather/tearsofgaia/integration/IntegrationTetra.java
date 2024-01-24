package moonfather.tearsofgaia.integration;

import moonfather.tearsofgaia.forging.ElementalHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class IntegrationTetra
{
	public static boolean IsASoulboundTool(ItemStack tool)
	{
		if (! tool.isEmpty() && ForgeRegistries.ITEMS.getKey(tool.getItem()).getNamespace().equals("tetra"))
		{
			if (ElementalHelper.GetNamedIntValue(tool, AnvilHelperTetra.TAG_KEY_SOULBOUND_LEVEL) > 0)
			{
				return true;
			}
		}
		return false;
	}



	public static boolean IsTetraTool(ItemStack tool)
	{
		return ForgeRegistries.ITEMS.getKey(tool.getItem()).getNamespace().equals("tetra");
	}



	public static void ReduceLevelOfSoulbound(ItemStack tool)
	{
		// normal part: tear added after v->t conversion
		int level = ElementalHelper.GetNamedIntValue(tool, AnvilHelperTetra.TAG_KEY_SOULBOUND_LEVEL);
		ElementalHelper.PutNamedIntValue(tool, AnvilHelperTetra.TAG_KEY_SOULBOUND_LEVEL, level - 1);
		// removing enchantment if a tear is added before conversion:
	}



	public static void AddSpecialTextShownAboveSubtitle(ItemTooltipEvent event, String element, int level)
	{
		int soulboundLevel = ElementalHelper.GetNamedIntValue(event.getItemStack(), AnvilHelperTetra.TAG_KEY_SOULBOUND_LEVEL);
		if (soulboundLevel == 0)
		{
			return;
		}
		String suffix = "";
		switch (soulboundLevel)
		{
			case 1: suffix = " I"; break;
			case 2: suffix = " II"; break;
			case 3: suffix = " III"; break;
		}
		event.getToolTip().add(Component.translatable("enchantment.tearsofgaia.soulbound_mf").append(suffix).withStyle(ChatFormatting.YELLOW));
	}
}
