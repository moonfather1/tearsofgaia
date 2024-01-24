package moonfather.tearsofgaia.item_abilities.mixin.accessors;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface TridentAccessor2
{
    @Invoker("setDeltaMovement")
    void invokeSetDeltaMovement(double p_20335_, double p_20336_, double p_20337_);
    @Invoker("setPosRaw")
    void invokeSetPosRaw(double p_20344_, double p_20345_, double p_20346_);
}
