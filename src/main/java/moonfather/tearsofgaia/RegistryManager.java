package moonfather.tearsofgaia;

import com.mojang.serialization.Codec;
import moonfather.tearsofgaia.enchantments.EnchantmentEasyRepair;
import moonfather.tearsofgaia.enchantments.EnchantmentSoulbound;
import moonfather.tearsofgaia.items.ItemGem;
import moonfather.tearsofgaia.items.OptionalRecipeCondition;
import moonfather.tearsofgaia.obtaining.GemLootModifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryManager
{
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
	private static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Constants.MODID);
	private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Constants.MODID);

	public static void Init()
	{
		RegistryManager.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegistryManager.ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegistryManager.LOOT_MODIFIERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static final RegistryObject<Item> ItemGemEarth = ITEMS.register("gem_earth", () -> new ItemGem("earth", 1));
	public static final RegistryObject<Item> ItemGemWater = ITEMS.register("gem_water", () -> new ItemGem("water", 1));
	public static final RegistryObject<Item> ItemGemAir = ITEMS.register("gem_air", () -> new ItemGem("air", 1));
	public static final RegistryObject<Item> ItemGemFire = ITEMS.register("gem_fire", () -> new ItemGem("fire", 1));

	public static final RegistryObject<Item> ItemGemEarth2 = ITEMS.register("gem_earth_level2", () -> new ItemGem("earth", 2));
	public static final RegistryObject<Item> ItemGemWater2 = ITEMS.register("gem_water_level2", () -> new ItemGem("water", 2));
	public static final RegistryObject<Item> ItemGemAir2 = ITEMS.register("gem_air_level2", () -> new ItemGem("air", 2));
	public static final RegistryObject<Item> ItemGemFire2 = ITEMS.register("gem_fire_level2", () -> new ItemGem("fire", 2));

	public static final RegistryObject<Enchantment> EnchantmentSoulbound = ENCHANTMENTS.register("soulbound_mf", EnchantmentSoulbound::new);
	public static final RegistryObject<Enchantment> EnchantmentEasyRepair = ENCHANTMENTS.register("repair_mf", EnchantmentEasyRepair::new);
	public static final RegistryObject<Codec<? extends IGlobalLootModifier>> StupidGLMSerializer = LOOT_MODIFIERS.register("loot_modifier_for_gems", GemLootModifier.CODEC);

	/////////////////////////////////////////////////////

	public static void OnCreativeTabPopulation(BuildCreativeModeTabContentsEvent event)
	{
		if (event.getTabKey().equals(CreativeModeTabs.INGREDIENTS))
		{
			event.accept(RegistryManager.ItemGemEarth);
			event.accept(RegistryManager.ItemGemWater);
			event.accept(RegistryManager.ItemGemAir);
			event.accept(RegistryManager.ItemGemFire);
			event.accept(RegistryManager.ItemGemEarth2);
			event.accept(RegistryManager.ItemGemWater2);
			event.accept(RegistryManager.ItemGemAir2);
			event.accept(RegistryManager.ItemGemFire2);
		}
	}
}
