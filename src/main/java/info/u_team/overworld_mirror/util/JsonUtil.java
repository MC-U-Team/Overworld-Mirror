package info.u_team.overworld_mirror.util;

import java.util.Map.Entry;
import java.util.function.LongPredicate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtil {
	
	public static void replaceNamedLongElement(JsonObject object, String name, LongPredicate replace, long newValue) {
		for (Entry<String, JsonElement> entry : object.entrySet()) {
			final String key = entry.getKey();
			final JsonElement value = entry.getValue();
			
			if (key.equals(name) && value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber()) {
				final long oldValue = value.getAsJsonPrimitive().getAsLong();
				if (replace.test(oldValue)) {
					object.addProperty(name, newValue);
				}
			}
			
			if (value instanceof JsonObject) {
				replaceNamedLongElement(value.getAsJsonObject(), name, replace, newValue);
			}
		}
	}
	
}
