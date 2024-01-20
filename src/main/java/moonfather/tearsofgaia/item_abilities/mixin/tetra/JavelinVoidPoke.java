package moonfather.tearsofgaia.item_abilities.mixin.tetra;

import moonfather.tearsofgaia.forging.ElementalHelper;
import moonfather.tearsofgaia.item_abilities.mixin.accessors.TridentAccessor1;
import moonfather.tearsofgaia.item_abilities.mixin.accessors.TridentAccessor2;
import moonfather.tearsofgaia.item_abilities.mixin.accessors.TridentAccessor3;
import moonfather.tearsofgaia.item_abilities.mixin.trident.VoidDamageCancelBase;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.tetra.items.modular.ThrownModularItemEntity;

@Pseudo
@Mixin(ThrownModularItemEntity.class)
public abstract class JavelinVoidPoke extends VoidDamageCancelBase
{
    @Override
    protected void CancelVoidDamage(CallbackInfo ci)
    {
        if (ElementalHelper.IsItemElementEqual(thrownStack, "air"))
        {
            if (((TridentAccessor3)this).invokePickup() == AbstractArrow.Pickup.ALLOWED)
            {
                Entity owner = ((TridentAccessor1)this).invokeGetOwner();
                if (owner != null)
                {
                    ((TridentAccessor2)this).invokeSetDeltaMovement(0, 0, 0);
                    ((TridentAccessor2)this).invokeSetPosRaw(owner.getX(), owner.getY(), owner.getZ());
                    ci.cancel();
                }
            }
        }
    }


    @Shadow
    private ItemStack thrownStack;
}
