package moonfather.tearsofgaia;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(Constants.MODID)
public class ModTears
{
    //private static final Logger LOGGER = LogUtils.getLogger();

    public ModTears(IEventBus modBus)
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OptionsHolder.COMMON_SPEC);
        RegistryManager.Init(modBus);
        modBus.addListener(this::setup);
        modBus.addListener(RegistryManager::OnCreativeTabPopulation);
    }



    private void setup(final FMLCommonSetupEvent event)
    {
	}
}
