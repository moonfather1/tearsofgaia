package moonfather.tearsofgaia.item_abilities.mixin.tetra;

import moonfather.tearsofgaia.forging.ElementalHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.tetra.items.modular.ThrownModularItemEntity;

@Pseudo
@Mixin(ThrownModularItemEntity.class)
public abstract class JavelinDespawnPoke extends AbstractArrow
{
    private JavelinDespawnPoke(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_) { super(p_36721_, p_36722_); }


    @Inject(method = "tickDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow; tickDespawn()V"), cancellable = true)
    private void PreventDespawn(CallbackInfo ci)
    {
        if (ElementalHelper.IsElementalTool(thrownStack))
        {
            ci.cancel();
        }
    }


    @Shadow
    private ItemStack thrownStack;
}
