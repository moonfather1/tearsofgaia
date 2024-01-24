package moonfather.tearsofgaia.item_abilities.mixin.accessors;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Projectile.class)
public interface TridentAccessor1
{
    @Invoker("getOwner")
    Entity invokeGetOwner();
}
