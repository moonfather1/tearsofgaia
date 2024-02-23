package moonfather.tearsofgaia.item_abilities.mixin.accessors;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractArrow.class)
public interface TridentAccessor3
{
    @Accessor("pickup")
    AbstractArrow.Pickup invokePickup();

    @Invoker("getPickupItemStackOrigin")
    ItemStack invokeGetItem();

}
