package moonfather.tearsofgaia.obtaining;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import moonfather.tearsofgaia.OptionsHolder;
import moonfather.tearsofgaia.RegistryManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import java.util.function.Supplier;

public class GemLootModifier extends LootModifier
{
	public GemLootModifier(LootItemCondition[] conditionsIn)
	{
		super(conditionsIn);
	}

	@Override
	public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
	{
		if (!IsBlockAGemOre(context.getQueriedLootTableId().toString()))
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

	private String[] tablesForGemOres = null;
	private boolean IsBlockAGemOre(String lootTableId)
	{
		if (this.tablesForGemOres == null)
		{
			this.tablesForGemOres = OptionsHolder.COMMON.BlocksThatDropGaiasTears.get().trim().split("\\s*,\\s*");
		}
		for (int i = 0; i < this.tablesForGemOres.length; i++)
		{
			if (lootTableId.equals(this.tablesForGemOres[i]))
			{
				return true;
			}
		}
		return false;
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



	///////////////////////////////////////////////////////////

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC.get();
	}

	public static final Supplier<Codec<GemLootModifier>> CODEC = Suppliers.memoize(() ->
			RecordCodecBuilder.create(inst -> codecStart(inst)
					.apply(inst, GemLootModifier::new)));
}
