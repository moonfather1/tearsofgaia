package moonfather.tearsofgaia.forging;

import moonfather.tearsofgaia.ModTears;
import moonfather.tearsofgaia.items.ItemGem;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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
	}


	public static void AddSpecialTextShownAboveSubtitle(ItemTooltipEvent event, String element, int level)
	{
		if (element.equals("air") && level == 2)
		{
			////!! zasto ne radi?
			event.getToolTip().add(new TranslationTextComponent("item.minecraft.totem_of_undying").withStyle(TextFormatting.YELLOW));
		}
	}
}
