package moonfather.tearsofgaia.item_abilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;

public class EntityItemWithFireImmunity extends ItemEntity
{
	public EntityItemWithFireImmunity(Level world)
	{
		super(EntityType.ITEM, world);
	}

	public EntityItemWithFireImmunity(ItemEntity original)
	{
		super(EntityType.ITEM, original.level());
		this.load(original.saveWithoutId(new CompoundTag()));
		this.setSecondsOnFire(0);
		//this.stringUUID = UUID.randomUUID();
		//this.cachedUniqueIdString = this.entityUniqueID.toString(); why did i write this?
	}



	@Override
	public boolean hurt(DamageSource source, float amount)
	{
		if (source != null && source.is(DamageTypeTags.IS_FIRE))
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
