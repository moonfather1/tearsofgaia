package moonfather.tearsofgaia.integration;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventForCritsFromTetraTools
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void OnLivingHurt(LivingHurtEvent event)
	{
		if (! event.getEntityLiving().level.isClientSide && event.getSource().getEntity() != null)
		{
			if (event.getSource().getEntity() instanceof LivingEntity)
			{
				ItemStack item = ((LivingEntity) event.getSource().getEntity()).getMainHandItem();
				if (EventForResistancesFromTetraTools.CheckItemsForElementalItem(item, "fire"))
				{
					int rollResult = event.getSource().getEntity().level.random.nextInt(100);
					if (rollResult < 10)
					{
						event.setAmount(event.getAmount() * 2);
						event.getSource().setIsFire();
					}
				}
			}
		}
	}
}
