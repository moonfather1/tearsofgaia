package moonfather.tearsofgaia.item_abilities.mixin.trident;

import moonfather.tearsofgaia.forging.ElementalHelper;
import moonfather.tearsofgaia.item_abilities.mixin.accessors.TridentAccessor1;
import moonfather.tearsofgaia.item_abilities.mixin.accessors.TridentAccessor2;
import moonfather.tearsofgaia.item_abilities.mixin.accessors.TridentAccessor3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class TridentVoidPoke extends VoidDamageCancelBase
{
    @Override
    protected void CancelVoidDamage(CallbackInfo ci)
    {
        if (ElementalHelper.IsItemElementEqual(tridentItem, "air"))
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
    private ItemStack tridentItem;
}
