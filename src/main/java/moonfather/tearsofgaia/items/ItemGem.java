package moonfather.tearsofgaia.items;

import moonfather.tearsofgaia.forging.ElementalHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.loot.*;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemGem extends Item
{
	public ItemGem(String element, int level)
	{
		super(ItemGem.GetProperties(element));
		this.element = element;
		this.level = level;
		this.subtitle1 = ItemGem.GetSubtitleLine1Text(element, level);
		this.subtitle2 = new TranslationTextComponent("item.tearsofgaia.gem_all.subtitle2");
		this.subtitle2B = new TranslationTextComponent(String.format("item.tearsofgaia.gem_%s.usage_%d", this.element, this.level));
	}

	private static Properties GetProperties(String element)
	{
		Item.Properties properties = new Properties();
		properties.setNoRepair();
		properties.rarity(Rarity.RARE);
		properties.tab(ItemGroup.TAB_MISC);
		if (element.equals("fire"))
		{
			properties.fireResistant();
		}
		return properties;
	}



	private static Map<String, ITextComponent> subtitleLine1 = new HashMap<String, ITextComponent>();

	private String element = "none";
	private int level = 1;
	private ITextComponent subtitle1, subtitle2, subtitle2B;

	@Override
	public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag advanced)
	{
		list.add(1, this.subtitle1);
		int tempTooltipTime = ElementalHelper.GetTempTooltipTime(itemStack);
		if (tempTooltipTime == 0)
		{
			list.add(2, new StringTextComponent(" "));
			list.add(3, this.subtitle2);
		}
		else
		{
			list.add(new StringTextComponent(" "));
			list.add(this.subtitle2B);
			ElementalHelper.UpdateTempTooltipTime(itemStack);
		}
		super.appendHoverText(itemStack, world, list, advanced);
	}

	private static ITextComponent PrepareSubtitleLine1Text(String element, int level)
	{
		Color color = Color.fromLegacyFormat(TextFormatting.DARK_RED);
		switch (element)
		{
			case "earth": color = Color.fromRgb(0x996633); break;
			case "water": color = Color.fromRgb(0x0088cc); break;
			case "fire": color = Color.fromRgb(0xff8c1a); break;
			case "air": color = Color.fromRgb(0xeeeeff); break;
		}
		StringTextComponent result = new StringTextComponent("");
		String subtitle1Key = String.format("item.tearsofgaia.gem_%s.subtitle", element);
		TranslationTextComponent elementText = new TranslationTextComponent(subtitle1Key);
		elementText.withStyle(Style.EMPTY.withColor(color));
		result.append(elementText);
		if (level > 1)
		{
			result.append(new TranslationTextComponent("item.tearsofgaia.level_suffix", " ", level));
		}
		return result;
	}

	public static ITextComponent GetSubtitleLine1Text(String element, int level)
	{
		if (ItemGem.subtitleLine1.size() == 0)
		{
			ItemGem.subtitleLine1.put("earth1", ItemGem.PrepareSubtitleLine1Text("earth", 1));
			ItemGem.subtitleLine1.put("water1", ItemGem.PrepareSubtitleLine1Text("water", 1));
			ItemGem.subtitleLine1.put("air1", ItemGem.PrepareSubtitleLine1Text("air", 1));
			ItemGem.subtitleLine1.put("fire1", ItemGem.PrepareSubtitleLine1Text("fire", 1));
			ItemGem.subtitleLine1.put("earth2", ItemGem.PrepareSubtitleLine1Text("earth", 2));
			ItemGem.subtitleLine1.put("water2", ItemGem.PrepareSubtitleLine1Text("water", 2));
			ItemGem.subtitleLine1.put("air2", ItemGem.PrepareSubtitleLine1Text("air", 2));
			ItemGem.subtitleLine1.put("fire2", ItemGem.PrepareSubtitleLine1Text("fire", 2));
		}
		return ItemGem.subtitleLine1.get(element + level);
	}

	public String GetElement()
	{
		return this.element;
	}



	public int GetLevel()
	{
		return this.level;
	}
}
