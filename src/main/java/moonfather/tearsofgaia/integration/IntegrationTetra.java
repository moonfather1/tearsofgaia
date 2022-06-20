package moonfather.tearsofgaia.integration;

import moonfather.tearsofgaia.forging.ElementalHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class IntegrationTetra
{
	public static boolean IsASoulboundTool(ItemStack tool)
	{
		if (tool.getItem().getRegistryName().getNamespace().equals("tetra"))
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
		return tool.getItem().getRegistryName().getNamespace().equals("tetra");
	}



	public static void ReduceLevelOfSoulbound(ItemStack tool)
	{
		int level = ElementalHelper.GetNamedIntValue(tool, AnvilHelperTetra.TAG_KEY_SOULBOUND_LEVEL);
		ElementalHelper.PutNamedIntValue(tool, AnvilHelperTetra.TAG_KEY_SOULBOUND_LEVEL, level - 1);
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
		event.getToolTip().add(new TranslationTextComponent("enchantment.tearsofgaia.soulbound_mf").append(suffix).withStyle(TextFormatting.YELLOW));
	}
}
