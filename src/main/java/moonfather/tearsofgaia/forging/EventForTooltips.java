package moonfather.tearsofgaia.forging;

import moonfather.tearsofgaia.integration.IntegrationTetra;
import moonfather.tearsofgaia.items.ItemGem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@Mod.EventBusSubscriber
public class EventForTooltips
{
	@SubscribeEvent
	public static void OnItemTooltip(ItemTooltipEvent event)
	{
		if (ElementalHelper.IsTear(event.getItemStack()))
		{
			ItemGem gem = (ItemGem) event.getItemStack().getItem();
			event.getToolTip().add(1, ItemGem.GetSubtitleLine1Text(gem.GetElement(), gem.GetLevel()));
			if (ExtendedTooltipManager.ShouldShowExtendedTooltip(event.getItemStack(), event.getEntity()))
			{
				boolean tetra = event.getItemStack().hasTag() && event.getItemStack().getTag().contains(ExtendedTooltipManager.TOOLTIP_IS_TETRA);
				event.getToolTip().add(2, gem.GetLoreSeparator());
				event.getToolTip().add(3, gem.GetLoreText(true, tetra));
			}
			else
			{
				event.getToolTip().add(2, gem.GetLoreSeparator());
				event.getToolTip().add(3, gem.GetLoreText(false, false));
			}
		}

		/////////////////////////////

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
				TextColor color = TextColor.fromRgb(0xff8566);
				event.getToolTip().add(Component.translatable("tearsofgaia.message.tinkersconstruct").withStyle(Style.EMPTY.withColor(color)));
				event.getToolTip().add(Component.translatable("tearsofgaia.message.line2").withStyle(Style.EMPTY.withColor(color)));
			}
		}
	}


	private static void AddSpecialTextShownAboveSubtitle(ItemTooltipEvent event, String element, int level)
	{
		if (element.equals("air") && level == 2)
		{
			event.getToolTip().add(Component.translatable("item.minecraft.totem_of_undying").withStyle(ChatFormatting.YELLOW));
		}
		if (element.equals("earth") && level == 2)
		{
			IntegrationTetra.AddSpecialTextShownAboveSubtitle(event, element, level);
		}
	}
}
