package moonfather.tearsofgaia.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moonfather.tearsofgaia.OptionsHolder;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

public class OptionalRecipeCondition implements ICondition
{
	private final String flagCode;

	private OptionalRecipeCondition(String value)
	{
		this.flagCode = value;
	}



	@Override
	public boolean test(@NotNull IContext context)
	{
		if (this.flagCode.equals("level_two_gems_enabled"))
		{
			return OptionsHolder.COMMON.LevelTwoGemsEnabled.get();
		}
		else
		{
			return false;
		}
	}

	/////////////////////////////////////////////////////

	@Override
	public Codec<? extends ICondition> codec()
	{
		return CODEC;
	}

	public static Codec<OptionalRecipeCondition> CODEC = RecordCodecBuilder.create(
			builder -> builder
					.group(Codec.STRING.fieldOf("flag_code").forGetter(orc -> orc.flagCode))
					.apply(builder, OptionalRecipeCondition::new));
}
