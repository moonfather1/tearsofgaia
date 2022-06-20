package moonfather.tearsofgaia.items;

import com.google.gson.JsonObject;
import moonfather.tearsofgaia.OptionsHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class OptionalRecipeCondition implements ICondition
{
	private final String flagCode;
	private final ResourceLocation conditionId;

	private OptionalRecipeCondition(ResourceLocation id, String value)
	{
		this.conditionId = id;
		this.flagCode = value;
	}

	@Override
	public ResourceLocation getID()
	{
		return this.conditionId;
	}

	@Override
	public boolean test()
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

	public static class Serializer implements IConditionSerializer<OptionalRecipeCondition>
	{
		private final ResourceLocation conditionId;

		public Serializer(ResourceLocation id)
		{
			this.conditionId = id;
		}

		@Override
		public void write(JsonObject json, OptionalRecipeCondition condition)
		{
			json.addProperty("flag_code", condition.flagCode);
		}

		@Override
		public OptionalRecipeCondition read(JsonObject json)
		{
			return new OptionalRecipeCondition(this.conditionId, json.getAsJsonPrimitive("flag_code").getAsString());
		}

		@Override
		public ResourceLocation getID()
		{
			return this.conditionId;
		}
	}
}
