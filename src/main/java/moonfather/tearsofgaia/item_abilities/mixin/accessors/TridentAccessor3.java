package moonfather.tearsofgaia.item_abilities.mixin.accessors;

import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractArrow.class)
public interface TridentAccessor3
{
    @Accessor("pickup")
    AbstractArrow.Pickup invokePickup();
}
