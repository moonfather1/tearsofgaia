package moonfather.tearsofgaia.items;


import moonfather.tearsofgaia.RegistryManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import java.util.HashMap;
import java.util.Map;

public class ItemGem extends Item
{
	public ItemGem(String element, int level)
	{
		super(ItemGem.GetProperties(element));
		this.element = element;
		this.level = level;
		this.subtitle1 = ItemGem.GetSubtitleLine1Text(element, level);
		this.subtitle2Basic = Component.translatable("item.tearsofgaia.gem_all.subtitle2").withStyle(ChatFormatting.GRAY);
		this.subtitle2Usage = Component.translatable(String.format("item.tearsofgaia.gem_%s.usage_%d", this.element, this.level)).withStyle(ChatFormatting.GRAY);
		this.subtitle2UsageTetra = Component.translatable(String.format("item.tearsofgaia.gem_%s.usage_%d_tetra", this.element, this.level)).withStyle(ChatFormatting.GRAY);
		this.subtitle2Sep = Component.literal(" ");
	}

	private static Properties GetProperties(String element)
	{
		Item.Properties properties = new Properties();
		properties.setNoRepair();
		properties.rarity(Rarity.RARE);
		if (element.equals("fire"))
		{
			properties.fireResistant();
		}
		return properties;
	}



	private static final Map<String, Component> subtitleLine1 = new HashMap<String, Component>();

	private String element = "none";
	private int level = 1;
	private final Component subtitle1, subtitle2Basic, subtitle2Usage, subtitle2UsageTetra, subtitle2Sep;



	private static Component PrepareSubtitleLine1Text(String element, int level)
	{
		TextColor color = TextColor.fromLegacyFormat(ChatFormatting.DARK_RED);
		switch (element)
		{
			case "earth": color = TextColor.fromRgb(0x996633); break;
			case "water": color = TextColor.fromRgb(0x0088cc); break;
			case "fire": color = TextColor.fromRgb(0xff8c1a); break;
			case "air": color = TextColor.fromRgb(0xeeeeff); break;
		}
		MutableComponent result = Component.literal("");
		String subtitle1Key = String.format("item.tearsofgaia.gem_%s.subtitle", element);
		MutableComponent elementText = Component.translatable(subtitle1Key);
		elementText.withStyle(Style.EMPTY.withColor(color));
		result.append(elementText);
		if (level > 1)
		{
			result.append(Component.translatable("item.tearsofgaia.level_suffix", " ", level));
		}
		return result;
	}

	public static Component GetSubtitleLine1Text(String element, int level)
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



	public Component GetLoreSeparator()
	{
		return this.subtitle2Sep;
	}

	public Component GetLoreText(boolean extended, boolean alternate)
	{
		if (extended && ! alternate) return this.subtitle2Usage;
		if (extended && alternate) return this.subtitle2UsageTetra;
		return this.subtitle2Basic;
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
