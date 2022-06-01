package moonfather.tearsofgaia.enchantments;

import moonfather.tearsofgaia.OptionsHolder;
import moonfather.tearsofgaia.RegistryManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class EnchantmentSoulbound extends Enchantment
{
	public static Enchantment GetInstance() { return RegistryManager.EnchantmentSoulbound.get(); }


	public EnchantmentSoulbound()
	{
		super(Rarity.VERY_RARE, EnchantmentType.BREAKABLE, EquipmentSlotType.values());
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
		return stack.getItemEnchantability() > 0;
	}

	@Override
	public boolean isTradeable()
	{
		return OptionsHolder.COMMON.SoulboundBookEnabledInWorld.get();
	}

	@Override
	public boolean isAllowedOnBooks() { return OptionsHolder.COMMON.SoulboundBookEnabledInWorld.get(); }

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
