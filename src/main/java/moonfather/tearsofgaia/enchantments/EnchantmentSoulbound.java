package moonfather.tearsofgaia.enchantments;

import moonfather.tearsofgaia.OptionsHolder;
import moonfather.tearsofgaia.RegistryManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Set;

public class EnchantmentSoulbound extends Enchantment
{
	public static Enchantment GetInstance() { return RegistryManager.EnchantmentSoulbound.get(); }


	public EnchantmentSoulbound()
	{
		super(Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values());
	}

	/**
	 * Returns the minimal value of enchantability needed on the enchantment level passed.
	 */
	@Override
	public int getMinCost(int enchantmentLevel)
	{
		return 3 + (enchantmentLevel - 1) * 6;
	}

	/**
	 * Returns the maximum value of enchantability needed on the enchantment level passed.
	 */
	@Override
	public int getMaxCost(int enchantmentLevel)
	{
		return this.getMinCost(enchantmentLevel) + 50;
	}

	@Override
	public boolean isTreasureOnly()
	{
		return true;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return stack.getEnchantmentValue() > 0;
	}

	@Override
	public boolean isTradeable()
	{
		return OptionsHolder.COMMON.SoulboundBookEnabledInWorld.get();
	}

	@Override
	public boolean isAllowedOnBooks() { return OptionsHolder.COMMON.SoulboundBookEnabledInWorld.get(); }

	@Override
	public boolean allowedInCreativeTab(Item book, Set<EnchantmentCategory> allowedCategories)
	{
		return true;
	}




	/**
	 * Returns the maximum level that the enchantment can have.
	 */
	@Override
	public int getMaxLevel()
	{
		return 3;
	}

	@Override
	public boolean checkCompatibility(Enchantment other)
	{
		return other != Enchantments.VANISHING_CURSE && other != this;
	}
}
