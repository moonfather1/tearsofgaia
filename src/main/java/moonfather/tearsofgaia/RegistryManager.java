package moonfather.tearsofgaia;

import com.mojang.serialization.Codec;
import moonfather.tearsofgaia.enchantments.EnchantmentEasyRepair;
import moonfather.tearsofgaia.enchantments.EnchantmentSoulbound;
import moonfather.tearsofgaia.items.ItemGem;
import moonfather.tearsofgaia.items.OptionalRecipeCondition;
import moonfather.tearsofgaia.obtaining.GemLootModifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class RegistryManager
{
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Constants.MODID);
	private static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT, Constants.MODID);
	private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Constants.MODID);
	private static final DeferredRegister<Codec<? extends ICondition>> CONDITIONS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, Constants.MODID);

	public static void Init(IEventBus modBus)
	{
		RegistryManager.ITEMS.register(modBus);
		RegistryManager.ENCHANTMENTS.register(modBus);
		RegistryManager.LOOT_MODIFIERS.register(modBus);
		RegistryManager.CONDITIONS.register(modBus);
	}

	public static final Supplier<Item> ItemGemEarth = ITEMS.register("gem_earth", () -> new ItemGem("earth", 1));
	public static final Supplier<Item> ItemGemWater = ITEMS.register("gem_water", () -> new ItemGem("water", 1));
	public static final Supplier<Item> ItemGemAir = ITEMS.register("gem_air", () -> new ItemGem("air", 1));
	public static final Supplier<Item> ItemGemFire = ITEMS.register("gem_fire", () -> new ItemGem("fire", 1));

	public static final Supplier<Item> ItemGemEarth2 = ITEMS.register("gem_earth_level2", () -> new ItemGem("earth", 2));
	public static final Supplier<Item> ItemGemWater2 = ITEMS.register("gem_water_level2", () -> new ItemGem("water", 2));
	public static final Supplier<Item> ItemGemAir2 = ITEMS.register("gem_air_level2", () -> new ItemGem("air", 2));
	public static final Supplier<Item> ItemGemFire2 = ITEMS.register("gem_fire_level2", () -> new ItemGem("fire", 2));

	public static final Supplier<Enchantment> EnchantmentSoulbound = ENCHANTMENTS.register("soulbound_mf", EnchantmentSoulbound::new);
	public static final Supplier<Enchantment> EnchantmentEasyRepair = ENCHANTMENTS.register("repair_mf", EnchantmentEasyRepair::new);
	public static final Supplier<Codec<? extends IGlobalLootModifier>> StupidGLMSerializer = LOOT_MODIFIERS.register("loot_modifier_for_gems", GemLootModifier.CODEC);

	public static final Supplier<Codec<? extends ICondition>> OptionalRecipe = CONDITIONS.register("optional", () -> OptionalRecipeCondition.CODEC);

	/////////////////////////////////////////////////////

	public static void OnCreativeTabPopulation(BuildCreativeModeTabContentsEvent event)
	{
		if (event.getTabKey().equals(CreativeModeTabs.INGREDIENTS))
		{
			event.accept(RegistryManager.ItemGemEarth.get());
			event.accept(RegistryManager.ItemGemWater.get());
			event.accept(RegistryManager.ItemGemAir.get());
			event.accept(RegistryManager.ItemGemFire.get());
			event.accept(RegistryManager.ItemGemEarth2.get());
			event.accept(RegistryManager.ItemGemWater2.get());
			event.accept(RegistryManager.ItemGemAir2.get());
			event.accept(RegistryManager.ItemGemFire2.get());
		}
	}
}
