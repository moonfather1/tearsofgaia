package moonfather.tearsofgaia.item_abilities;

import moonfather.tearsofgaia.forging.ElementalHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Iterator;

@Mod.EventBusSubscriber
public class EventsForPersistence
{
	@SubscribeEvent
	public static void OnItemExpire(ItemExpireEvent event)
	{
		if (ElementalHelper.IsTearOrElementalTool(event.getEntityItem().getItem()))
		{
			event.setExtraLife(20*1200*5); // no expiration (5 extra days)
			event.setCanceled(true);
		}
	}



	private static final double zeroHeightFloatVelocity = 0.12345;// 1.0/20/4;


	private static void DoItemFloat(ItemEntity entity)
	{
		DoStupidParticles(entity);
		if (entity.getY() < entity.level.getMinBuildHeight() + 3.8)
		{
			// fell out of world
			if (entity.getDeltaMovement().y() == zeroHeightFloatVelocity)
			{
				if (entity.getY() >= entity.level.getMinBuildHeight() + 1.9)
				{
					entity.setNoGravity(true);
					entity.setDeltaMovement(0, -zeroHeightFloatVelocity, 0);
					//System.out.println("go down!: " + entity.getItem().getDisplayName() + "; speed==" + entity.motionY);
				}
			}
			else if (entity.getDeltaMovement().y() == -zeroHeightFloatVelocity)
			{
				if (entity.getY() < entity.level.getMinBuildHeight() + -1)
				{
					entity.setNoGravity(true);
					entity.setDeltaMovement(0, +zeroHeightFloatVelocity, 0);
					//System.out.println("go up!: " + entity.getItem().getDisplayName() + "; speed==" + entity.motionY);
				}
			}
			else
			{
				entity.setNoGravity(true);
				entity.setDeltaMovement(0, +zeroHeightFloatVelocity, 0);
				//System.out.println("go up!!: " + entity.getItem().getDisplayName() + "; speed==" + entity.motionY);
			}
		}
		else
		{
			// ne bi trebalo da se desi!
			if (entity.getY() >= entity.level.getMinBuildHeight() + 9.9 && entity.getDeltaMovement().y() == zeroHeightFloatVelocity)
			{
				entity.setNoGravity(false);
				entity.setDeltaMovement(0, -zeroHeightFloatVelocity, 0);
				//System.out.println("go down!!: " + entity.getItem().getDisplayName() + "; speed==" + entity.motionY);
			}
		}
	}



	@SubscribeEvent
	public static void DoWorldTickForFire(TickEvent.WorldTickEvent event)
	{
		if (droppedElementalItemsFire.size() == 0)
		{
			return;
		}
		if (event.phase == TickEvent.Phase.START || event.world.isClientSide || event.world.getGameTime() % 2*20 != 0)
		{
			return;
		}
		Iterator iterator = droppedElementalItemsFire.iterator();
		while (iterator.hasNext())
		{
			ItemEntity e = (ItemEntity) iterator.next();
			if (e.level == event.world)
			{
				if (! e.isAlive())
				{
					iterator.remove();
				}
				else if (e.isInLava())
				{
					DoStupidParticles(e);
				}
			}
		}
	}



	@SubscribeEvent
	public static void DoWorldTickForAir(TickEvent.WorldTickEvent event)
	{
		if (droppedElementalItemsAir.size() == 0)
		{
			return;
		}
		if (event.phase == TickEvent.Phase.START || event.world.isClientSide || event.world.getGameTime() % 10 != 0)
		{
			return;
		}
		Iterator iterator = droppedElementalItemsAir.iterator();
		while (iterator.hasNext())
		{
			ItemEntity e = (ItemEntity) iterator.next();
			if (e.level == event.world)
			{
				if (! e.isAlive())
				{
					//System.out.println("dead " + e.getItem().getDisplayName() + "; speed==" + e.motionY + "; set_size==" + droppedElementalItemsAir.size());
					iterator.remove();
				}
				else if (e.getY() < e.level.getMinBuildHeight() + 0 || e.getDeltaMovement().y() == zeroHeightFloatVelocity || e.getDeltaMovement().y() == -zeroHeightFloatVelocity || e.getTags().contains("DoItemFloat"))
				{
					//System.out.println("below zero: " + e.getItem().getDisplayName() + "; Y==" + e.posY + "; speed==" + e.motionY + "; set_size==" + droppedElementalItemsAir.size());
					DoItemFloat(e);
					e.addTag("DoItemFloat");
				}
			}
		}
	}



	private static void DoStupidParticles(Entity entity)
	{
		if (entity.level.getGameTime() % 5 != 0)
		{
			//System.out.println("NO particles for " + ((EntityItem)entity).getItem().getDisplayName());
			return;
		}
		//((WorldServer)entity.world).spawnParticle(EnumParticleTypes.VILLAGER_ANGRY, entity.posX + (double)(entity.world.rand.nextFloat() * entity.width * 2.0F) - (double)entity.width, entity.posY + 0.5D + (double)(entity.world.rand.nextFloat() * entity.height), entity.posZ + (double)(entity.world.rand.nextFloat() * entity.width * 2.0F) - (double)entity.width, entity.world.rand.nextFloat(), entity.world.rand.nextFloat(), entity.world.rand.nextFloat());
		if (entity.level instanceof ServerLevel)
		{
			//((WorldServer) entity.world).spawnParticle(EnumParticleTypes.VILLAGER_ANGRY, true, entity.posX + (double) (entity.world.rand.nextFloat() * entity.width * 2.0F) - (double) entity.width, entity.posY + 0.5D + (double) (entity.world.rand.nextFloat() * entity.height), entity.posZ + (double) (entity.world.rand.nextFloat() * entity.width * 2.0F) - (double) entity.width, entity.world.rand.nextFloat(), entity.world.rand.nextFloat(), entity.world.rand.nextFloat(), 1, 2, 3);
			double xSpeed, ySpeed, zSpeed;
			for (int i = 1; i <= 8; i++)
			{
				xSpeed = (entity.level.random.nextFloat() - 0.5D) * 0.40D;
				ySpeed = (entity.level.random.nextFloat() - 0.5D) * 0.05D;
				zSpeed = (entity.level.random.nextFloat() - 0.5D) * 0.40D;
				((ServerLevel)entity.level).sendParticles(ParticleTypes.INSTANT_EFFECT,entity.getX() + xSpeed * 8, entity.getY() + ySpeed * 6, entity.getZ() + zSpeed * 8, 1 /*numberOfParticles*/, xSpeed, ySpeed, zSpeed  , 0.10d /*particleSpeed*/ /*, int... particleArguments*/);
				//entityItem.worldObj.spawnParticle(particleType, entityItem.posX + xSpeed * 8, entityItem.posY + ySpeed * 4, entityItem.posZ + zSpeed * 8, xSpeed, 2 * ySpeed, zSpeed, new int[0]);
			}

		}
		else
		{
			//System.out.println("particles params: xcoord==" + (entity.posX + (double)(entity.world.rand.nextFloat() * entity.width * 2.0F) - (double)entity.width) + "; ycoord==" + (entity.posY + 0.5D + (double)(entity.world.rand.nextFloat() * entity.height)) + "; zcord==" + (entity.posZ + (double)(entity.world.rand.nextFloat() * entity.width * 2.0F) - (double)entity.width) + "; xs==" + entity.world.rand.nextFloat() + "; ys==" + entity.world.rand.nextFloat() + "; zs==" + entity.world.rand.nextFloat());
		}
		//System.out.println("particles for " + ((ItemEntity)entity).getItem().getDisplayName() + "; speed==" + entity.getDeltaMovement().y() + "; width==" + entity.getBbWidth());
	}


	private static final HashSet<ItemEntity> droppedElementalItemsAir = new HashSet<ItemEntity>();
	private static final HashSet<ItemEntity> droppedElementalItemsFire = new HashSet<ItemEntity>();


	@SubscribeEvent
	public static void OnEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if (! event.getWorld().isClientSide && event.getEntity() instanceof ItemEntity)
		{
			ItemStack stack = ((ItemEntity)event.getEntity()).getItem();
			if ((ElementalHelper.IsTear(stack) && ElementalHelper.GetTearElement(stack).equals("air"))
				|| ElementalHelper.IsItemElementEqual(stack,"air"))
			{
				//System.out.println("join world " + stack.getDisplayName() + "; speed==" + event.getEntity().motionY);
				droppedElementalItemsAir.add((ItemEntity) event.getEntity());
				if (event.getEntity().getY() < event.getEntity().level.getMinBuildHeight() + 0)
				{
					event.getEntity().setNoGravity(true);
					event.getEntity().setDeltaMovement(0, zeroHeightFloatVelocity, 0);
					if (event.getEntity().getY() < event.getEntity().level.getMinBuildHeight() + -20)
					{
						//System.out.println("void?");
						event.getEntity().setPos(event.getEntity().getX(), event.getEntity().level.getMinBuildHeight() + -19, event.getEntity().getZ());
					}
				}
			}
			else if ((ElementalHelper.IsTear(stack) && ElementalHelper.GetTearElement(stack).equals("fire"))
					|| ElementalHelper.IsItemElementEqual(stack,"fire"))
			{
				if (! (event.getEntity() instanceof EntityItemWithFireImmunity))
				{
					EntityItemWithFireImmunity e2 = new EntityItemWithFireImmunity((ItemEntity) event.getEntity());
					event.getEntity().remove(Entity.RemovalReason.DISCARDED);
					event.getWorld().addFreshEntity(e2);
					droppedElementalItemsFire.add(e2);
				}
			}
		}
	}
}
