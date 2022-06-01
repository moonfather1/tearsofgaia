package moonfather.tearsofgaia.item_abilities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityItemWithFireImmunity extends ItemEntity
{
	public EntityItemWithFireImmunity(World world)
	{
		super(EntityType.ITEM, world);
	}

	public EntityItemWithFireImmunity(ItemEntity original)
	{
		super(EntityType.ITEM, original.level);
		this.load(original.saveWithoutId(new CompoundNBT()));
		this.setSecondsOnFire(0);
		//this.stringUUID = UUID.randomUUID();
		//this.cachedUniqueIdString = this.entityUniqueID.toString(); why did i write this?
	}



	@Override
	public boolean hurt(DamageSource source, float amount)
	{
		if (source != null && (source.equals(DamageSource.IN_FIRE) || source.equals(DamageSource.ON_FIRE) || source.equals(DamageSource.LAVA) || source.equals(DamageSource.HOT_FLOOR) || source.equals(DamageSource.DRAGON_BREATH)))
		{
			return false;
		}
		return super.hurt(source, amount);
	}

	@Override
	public boolean fireImmune()
	{
		return true;
	}

	@Override
	public boolean displayFireAnimation()
	{
		return false;
	}
}
