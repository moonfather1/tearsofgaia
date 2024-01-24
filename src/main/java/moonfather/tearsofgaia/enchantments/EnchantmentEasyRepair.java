package moonfather.tearsofgaia.enchantments;

import moonfather.tearsofgaia.OptionsHolder;
import moonfather.tearsofgaia.RegistryManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentEasyRepair extends Enchantment
{
	public static Enchantment GetInstance() { return RegistryManager.EnchantmentEasyRepair.get(); }



	public EnchantmentEasyRepair()
	{
		super(Rarity.RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values());
	}



	public static int GetMaxAnvilCost(int enchantmentLevel)
	{
		switch (enchantmentLevel)
		{
			case 1: return 12;
			case 2: return 4;
			case 3: return 1;
			default: return 1;
		}
	}



	/**
	 * Returns the minimal value of enchantability needed on the enchantment level passed.
	 */
	@Override
	public int getMinCost(int enchantmentLevel)
	{
		return 6 + (enchantmentLevel - 1) * 6;
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
		return stack.getEnchantmentValue() > 0
			&& (
				! (stack.getItem() instanceof TieredItem || stack.getItem() instanceof ArmorItem)
				|| (stack.getItem() instanceof TieredItem && (((TieredItem) stack.getItem()).getTier().getLevel() > 1 || ((TieredItem) stack.getItem()).getTier().equals(Tiers.GOLD)))
				|| (stack.getItem() instanceof ArmorItem && ! ((ArmorItem) stack.getItem()).getMaterial().equals(ArmorMaterials.LEATHER))
			);
	}

	@Override
	public boolean canEnchant(ItemStack stack)
	{
		return this.canApplyAtEnchantingTable(stack) && ! ForgeRegistries.ITEMS.getKey(stack.getItem()).getNamespace().equals("tetra");
	}




	@Override
	public boolean allowedInCreativeTab(Item book, CreativeModeTab tab)
	{
		return true;
	}




	@Override
	public boolean isTradeable()
	{
		return OptionsHolder.COMMON.RepairBookEnabledInWorld.get();
	}

	@Override
	public boolean isAllowedOnBooks() { return OptionsHolder.COMMON.RepairBookEnabledInWorld.get(); }

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
		return other != Enchantments.BINDING_CURSE && other != this;
	}
}
