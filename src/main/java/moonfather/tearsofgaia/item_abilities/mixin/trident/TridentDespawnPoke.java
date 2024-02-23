package moonfather.tearsofgaia.item_abilities.mixin.trident;

import moonfather.tearsofgaia.forging.ElementalHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class TridentDespawnPoke extends AbstractArrow
{
    private TridentDespawnPoke(EntityType<? extends AbstractArrow> p_36711_, Level p_36715_, ItemStack p_308982_) { super(p_36711_, p_36715_, p_308982_); }


    @Inject(method = "tickDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow; tickDespawn()V"), cancellable = true)
    private void PreventDespawn(CallbackInfo ci)
    {
        if (ElementalHelper.IsElementalTool(this.getPickupItemStackOrigin()))
        {
            ci.cancel();
        }
    }
}
