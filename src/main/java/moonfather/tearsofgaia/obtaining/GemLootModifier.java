package moonfather.tearsofgaia.obtaining;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import moonfather.tearsofgaia.OptionsHolder;
import moonfather.tearsofgaia.RegistryManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import java.util.List;

public class GemLootModifier extends LootModifier
{
	public GemLootModifier(ILootCondition[] conditionsIn)
	{
		super(conditionsIn);
	}

	@Override
	public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
	{
		if (! context.getQueriedLootTableId().toString().equals("minecraft:blocks/diamond_ore") && ! context.getQueriedLootTableId().toString().equals("minecraft:blocks/emerald_ore"))
		{
			return generatedLoot;
		}
		if (context.getLevel().random.nextInt(1000) >= OptionsHolder.COMMON.NumberOfTearsInA1000DiamondBlocks.get())
		{
			return generatedLoot;
		}

		for (int i = 0; i < generatedLoot.size(); i++)
		{
			if (generatedLoot.get(i).getItem() == Items.DIAMOND || generatedLoot.get(i).getItem() == Items.EMERALD)
			{
				if (OptionsHolder.COMMON.ShouldReplaceAGem.get())
				{
					int kind = context.getLevel().random.nextInt(4);
					generatedLoot.set(i, GetRandomTear(kind));
				}
				else
				{
					int kind = context.getLevel().random.nextInt(4);
					generatedLoot.add(GetRandomTear(kind));
				}
				break;
			}
		}
		return generatedLoot;
	}



	private static ItemStack GetRandomTear(int kind)
	{
		switch (kind)
		{
			case 0: return new ItemStack(RegistryManager.ItemGemAir.get());
			case 1: return new ItemStack(RegistryManager.ItemGemEarth.get());
			case 2: return new ItemStack(RegistryManager.ItemGemFire.get());
			case 3: return new ItemStack(RegistryManager.ItemGemWater.get());
		}
		return new ItemStack(Items.POISONOUS_POTATO);
	}



	public static class Serializer extends GlobalLootModifierSerializer<GemLootModifier>
	{
		@Override
		public GemLootModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn)
		{
			return new GemLootModifier(conditionsIn);
		}

		@Override
		public JsonObject write(GemLootModifier luckBlockDropsModifier)
		{
			JsonObject result = new JsonObject();
			result.add("conditions", new JsonArray());
			return result;
		}
	}
}
