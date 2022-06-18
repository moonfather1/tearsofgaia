package moonfather.tearsofgaia.forging;

import moonfather.tearsofgaia.Constants;
import moonfather.tearsofgaia.ModTears;
import moonfather.tearsofgaia.integration.AnvilHelperTetra;
import moonfather.tearsofgaia.integration.IntegrationTetra;
import moonfather.tearsofgaia.items.ItemGem;
import net.minecraft.item.Items;
import net.minecraft.util.text.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventForTooltips
{
	@SubscribeEvent
	public static void OnItemTooltip(ItemTooltipEvent event)
	{
		String element = ElementalHelper.GetItemElement(event.getItemStack());
		if (element != null)
		{
			int level = ElementalHelper.GetItemElementLevel(event.getItemStack());
			AddSpecialTextShownAboveSubtitle(event, element, level);
			event.getToolTip().add(ItemGem.GetSubtitleLine1Text(element, level));
		}
		else if (event.getItemStack().getItem().equals(Items.POISONOUS_POTATO))
		{
			if (event.getItemStack().getTag() != null && event.getItemStack().getTag().contains("tog_message_for_tc"))
			{
				Color color = Color.fromRgb(0xff8566);
				event.getToolTip().add(new TranslationTextComponent("tearsofgaia.message.tinkersconstruct").withStyle(Style.EMPTY.withColor(color)));
				event.getToolTip().add(new TranslationTextComponent("tearsofgaia.message.line2").withStyle(Style.EMPTY.withColor(color)));
			}
		}
	}


	private static void AddSpecialTextShownAboveSubtitle(ItemTooltipEvent event, String element, int level)
	{
		if (element.equals("air") && level == 2)
		{
			event.getToolTip().add(new TranslationTextComponent("item.minecraft.totem_of_undying").withStyle(TextFormatting.YELLOW));
		}
		if (element.equals("earth") && level == 2)
		{
			IntegrationTetra.AddSpecialTextShownAboveSubtitle(event, element, level);
		}
	}
}
