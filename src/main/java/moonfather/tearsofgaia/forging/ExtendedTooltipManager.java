package moonfather.tearsofgaia.forging;

import moonfather.tearsofgaia.integration.IntegrationTetra;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Iterator;

@Mod.EventBusSubscriber
public class ExtendedTooltipManager
{
	private static final String TOOLTIP_PLAYER = "tog_ex_tooltip_player";
	private static final String TOOLTIP_PLAYER_X = "tog_ex_tooltip_player_x";
	private static final String TOOLTIP_PLAYER_Z = "tog_ex_tooltip_player_z";
	public static final String TOOLTIP_IS_TETRA = "tog_ex_tooltip_tetra";
	public static void ActivateExtendedTooltip(ItemStack stack, Player player, ItemStack otherItem)
	{
		if (! player.level.isClientSide())
		{
			// it is only here where i'm building a second tooltip system (having discarded 5 more ideas) that i discover that anvil has a client event and that i could have used that in first system (one that counts down in tooltip method)
			stack.getOrCreateTag().putString(TOOLTIP_PLAYER, player.getStringUUID());
			stack.getOrCreateTag().putDouble(TOOLTIP_PLAYER_X, player.position().x);
			stack.getOrCreateTag().putDouble(TOOLTIP_PLAYER_Z, player.position().z);
			if (! AlreadyStored(stack))
			{
				storage.add(new PlayerLocationRecordForItems(player, player.position().x, player.position().z, stack));
			}
			if (! otherItem.isEmpty() && IntegrationTetra.IsTetraTool(otherItem))
			{
				stack.getOrCreateTag().putString(TOOLTIP_IS_TETRA, "y");
			}
		}
	}



	private static boolean AlreadyStored(ItemStack stack)
	{
		for (PlayerLocationRecordForItems record : storage)
		{
			if (record.itemWithTooltip.equals(stack))
			{
				return true;
			}
		}
		return false;
	}



	public static boolean ShouldShowExtendedTooltip(ItemStack itemStack, Player player)
	{
		if (itemStack.getTag() == null || ! itemStack.getTag().contains(TOOLTIP_PLAYER))
		{
			return false;
		}
		return Math.abs(player.position().x - itemStack.getTag().getDouble(TOOLTIP_PLAYER_X)) < 0.5 && Math.abs(player.position().z - itemStack.getTag().getDouble(TOOLTIP_PLAYER_Z)) < 0.5;
	}

	/////////////////////////////////////////////////////////////////////////////////////

	private static final HashSet<PlayerLocationRecordForItems> storage = new HashSet<PlayerLocationRecordForItems>();

	private static class PlayerLocationRecordForItems
	{
		ItemStack itemWithTooltip;
		Double x, z;
		Player player;

		public PlayerLocationRecordForItems(Player player, double x, double z, ItemStack stack)
		{
			this.player = player;
			this.x = x;
			this.z = z;
			this.itemWithTooltip = stack;
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////

	@SubscribeEvent
	public static void DoWorldTickForCleanup(TickEvent.LevelTickEvent event)
	{
		if (! event.level.isClientSide() && event.level.getGameTime() % 120 == 19 && event.phase.equals(TickEvent.Phase.START))
		{
			Iterator<PlayerLocationRecordForItems> i = storage.iterator();
			while (i.hasNext())
			{
				PlayerLocationRecordForItems record = i.next();
				if (true)   // condition to the right kills this system (record.player.level.equals(event.world))
				{
					if (Math.abs(record.player.position().x - record.x) > 0.5 || Math.abs(record.player.position().z - record.z) > 0.5 || ! record.player.isAlive())
					{
						record.itemWithTooltip.getTag().remove(TOOLTIP_PLAYER);
						record.itemWithTooltip.getTag().remove(TOOLTIP_PLAYER_X);
						record.itemWithTooltip.getTag().remove(TOOLTIP_PLAYER_Z); // in order to stack with other in inven
						record.itemWithTooltip.getTag().remove(TOOLTIP_IS_TETRA);
						if (record.itemWithTooltip.getTag().size() == 0)
						{
							record.itemWithTooltip.setTag(null);
						}
						record.itemWithTooltip = null;
						i.remove();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void DoPlayerTickForCleanup(TickEvent.PlayerTickEvent event)
	{
		if (event.player.level.getGameTime() % 200 == 19 && event.phase.equals(TickEvent.Phase.START))
		{
			for (ItemStack stack : event.player.getInventory().items)
			{
				if (! stack.isEmpty() && stack.hasTag() && stack.getTag().contains(TOOLTIP_PLAYER))
				{
					if (stack.getTag().getString(TOOLTIP_PLAYER).equals(event.player.getStringUUID()))
					{
						if (Math.abs(event.player.position().x - stack.getTag().getDouble(TOOLTIP_PLAYER_X)) > 0.5 || Math.abs(event.player.position().z - stack.getTag().getDouble(TOOLTIP_PLAYER_Z)) > 0.5)
						{
							stack.getTag().remove(TOOLTIP_PLAYER);
							stack.getTag().remove(TOOLTIP_PLAYER_X);
							stack.getTag().remove(TOOLTIP_PLAYER_Z); // in order to stack with other in inven
							stack.getTag().remove(TOOLTIP_IS_TETRA);
							if (stack.getTag().size() == 0)
							{
								stack.setTag(null);
							}
						}
					}
				}
			}
		}
	}
}
