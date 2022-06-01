package moonfather.tearsofgaia;

import moonfather.tearsofgaia.enchantments.EnchantmentEasyRepair;
import moonfather.tearsofgaia.enchantments.EnchantmentSoulbound;
import moonfather.tearsofgaia.items.ItemGem;
import moonfather.tearsofgaia.obtaining.GemLootModifier;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryManager
{
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
	private static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Constants.MODID);
	private static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, Constants.MODID);

	public static void Init()
	{
		RegistryManager.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegistryManager.ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
		RegistryManager.LOOT_MODIFIER_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
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
	public static final RegistryObject<GlobalLootModifierSerializer<GemLootModifier>> StupidGLMSerializer = LOOT_MODIFIER_SERIALIZERS.register("loot_modifier_for_gems", GemLootModifier.Serializer::new);
}
