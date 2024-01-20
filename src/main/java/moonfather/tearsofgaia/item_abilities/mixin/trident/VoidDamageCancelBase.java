package moonfather.tearsofgaia.item_abilities.mixin.trident;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class VoidDamageCancelBase
{
    @SuppressWarnings("CancellableInjectionUsage")
    @Inject(at = @At("HEAD"), method = "outOfWorld", cancellable = true)
    protected void CancelVoidDamage(CallbackInfo ci)
    {
        // marker for override
    }
}
