package moonfather.tearsofgaia;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class OptionsHolder
{
	public static class Common
	{
		private static final int defaultNumberOfTearsInA1000DiamondBlocks = 50;
		private static final boolean defaultShouldReplaceAGem = true;
		private static final boolean defaultLevelTwoGemsEnabled = true;
		private static final boolean defaultSoulboundBookEnabledInWorld = true;
		private static final boolean defaultRepairBookEnabledInWorld = true;

		public final ConfigValue<Integer> NumberOfTearsInA1000DiamondBlocks;
		public final ConfigValue<Boolean> ShouldReplaceAGem;
		public final ConfigValue<Boolean> LevelTwoGemsEnabled;
		public final ConfigValue<Boolean> SoulboundBookEnabledInWorld;
		public final ConfigValue<Boolean> RepairBookEnabledInWorld;

		public Common(ForgeConfigSpec.Builder builder)
		{
			this.NumberOfTearsInA1000DiamondBlocks = builder.comment("Gaia's tears are obtained from diamond ore blocks and emerald ore blocks. This is a weight value which sets how often you will find a tear gem. Default value is 100 (meaning 50 out of every 1000, or in other words 5%) and might even be a generous weight. Set to 0 do disable (in which case you need to establish a different way of obtaining these gems). There is no worldgen.")
					.defineInRange("Number of tears in a 1000 gem blocks", defaultNumberOfTearsInA1000DiamondBlocks, 0, 1000);
			this.ShouldReplaceAGem = builder.comment("Gaia's tears are obtained from diamond ore blocks and emerald ore blocks. This setting controls whether they replace one of the gems you get from a block when breaking it, or is a tear added to the drop list.")
					.define("Should replace a gem", defaultShouldReplaceAGem);
			this.LevelTwoGemsEnabled = builder.comment("Advanced versions of these gems crafted from normal versions and aplied after normal ones. Quite powerful.").worldRestart()
					.define("Level two gems enabled", defaultLevelTwoGemsEnabled);
			this.SoulboundBookEnabledInWorld = builder.comment("If set to true, enchanted books with Soulbound enchantment can be found in fishing loot or bought from librarians or found in chests. If set to false, only mods can put the book into loot or make it otherwise available to players. This mod bestows soulbound enchantment through earth gem.").worldRestart()
					.define("Enchanted books with Soulbound available", defaultSoulboundBookEnabledInWorld);
			this.RepairBookEnabledInWorld = builder.comment("If set to true, enchanted books with Smith's Triuph enchantment (limiting anvil cost) can be found in fishing loot or bought from librarians or found in chests. If set to false, only mods can put the book into loot or make it otherwise available to players. This mod bestows soulbound enchantment through water gem.").worldRestart()
					.define("Enchanted books with Smith's Triumph available", defaultRepairBookEnabledInWorld);
		}
	}

	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;

	static //constructor
	{
		Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON = commonSpecPair.getLeft();
		COMMON_SPEC = commonSpecPair.getRight();
	}
}
