package io.github.shitsurei.common.resource;

import com.google.gson.*;
import lombok.SneakyThrows;

import java.lang.reflect.Type;

/**
 * Gson管理器
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/17 15:30
 */
public class GsonManager {

    private static Gson normalGson;
    private static Gson customGson;
    private static GsonManager gsonManager;

    private GsonManager() {
        normalGson = new Gson();
        customGson = new GsonBuilder()
                .serializeNulls()
                // 使Expose注解生效（必须指明每个字段）
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(Class.class, new ClassCodec())
                .create();
    }

    /**
     * 类转化器
     */
    static class ClassCodec implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {
        // 反序列化
        @SneakyThrows
        @Override
        public Class<?> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String clazz = jsonElement.getAsString();
            return Class.forName(clazz);
        }

        // 序列化
        @Override
        public JsonElement serialize(Class<?> aClass, Type type, JsonSerializationContext jsonSerializationContext) {
            // 将 Class 变为 json
            return new JsonPrimitive(aClass.getName());
        }
    }

    public static Gson getInstance(boolean custom) {
        if (gsonManager == null) {
            synchronized (GsonManager.class) {
                if (gsonManager == null) {
                    gsonManager = new GsonManager();
                }
            }
        }
        return custom ? customGson : normalGson;
    }
}
