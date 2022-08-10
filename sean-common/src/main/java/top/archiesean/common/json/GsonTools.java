package top.archiesean.common.json;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * gson序列化工具类
 *
 * @author ArchieSean
 * @since 2022-08-04 11:36
 */
@Slf4j
public class GsonTools {
    /**
     * <p>
     * 配置属性注解 @SerializedName(value = "json_KEY")用于指定属性和json-key之间的关系</p>
     * <p>@Expose注解可以设置序列化和反序列化，对应两个属性serialize,deserialize</p>
     */
    private static final Gson gson;
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATEFORMAT = "yyyy-MM-dd";
    private static final String TIMEFRAME = "HH:mm:ss";
    private static final JsonSerializer<LocalDateTime> dateTimeSerializer
            = (obj, type, ctx) ->
            new JsonPrimitive(DateUtil.format(obj, FORMAT));
    private static final JsonSerializer<LocalDate> dateSerializer
            = (obj, type, ctx) ->
            new JsonPrimitive(DateUtil.format(LocalDateTime.from(obj), DATEFORMAT));
    private static final JsonSerializer<LocalTime> timeSerializer
            = (obj, type, ctx) ->
            new JsonPrimitive(DateUtil.format(LocalDateTime.from(obj), TIMEFRAME));

    private static final JsonDeserializer<LocalDateTime> dateTimeDeserializer
            = (json, type, ctx) ->
            DateUtil.parseLocalDateTime(json.getAsJsonPrimitive()
                    .getAsString(), FORMAT);
    private static final JsonDeserializer<LocalDate> dateDeserializer
            = (json, type, ctx) ->
            LocalDate.parse(json.getAsJsonPrimitive().getAsString(),
                    DateTimeFormatter.ofPattern(DATEFORMAT));
    private static final JsonDeserializer<LocalTime> timeDeserializer
            = (json, type, ctx) ->
            LocalTime.parse(json.getAsJsonPrimitive().getAsString(),
                    DateTimeFormatter.ofPattern(TIMEFRAME));

    static {
        GsonBuilder builder = new GsonBuilder();
        //不转义html符号
        builder.disableHtmlEscaping();
        //启用复杂key
        builder.enableComplexMapKeySerialization();

        builder.setDateFormat(FORMAT);
        //序列化
        builder.registerTypeAdapter(LocalDateTime.class, dateTimeSerializer);
        builder.registerTypeAdapter(LocalDate.class, dateSerializer);
        builder.registerTypeAdapter(LocalTime.class, timeSerializer);
        //反序列化
        builder.registerTypeAdapter(LocalDateTime.class, dateTimeDeserializer);
        builder.registerTypeAdapter(LocalDate.class, dateDeserializer);
        builder.registerTypeAdapter(LocalTime.class, timeDeserializer);
        //创建gson实例
        gson = builder.create();
    }

    /**
     * 对象转json
     *
     * @param obj obj
     * @return json
     */
    public static String toGsonString(Object obj) {
        String var2 = null;
        try {
            var2 = gson.toJson(obj);
        } catch (Exception e) {
            log.error("对象转json失败--->", e);
        }
        return var2;
    }

    /**
     * json转对象
     *
     * @param json  json
     * @param clazz class
     * @param <T>   泛型
     * @return T
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    /**
     * json转集合
     *
     * @param json  json
     * @param clazz class
     * @param <T>   泛型
     * @return list
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        ArrayList<T> list = Lists.newArrayList();
        try {
            gson.fromJson(json, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception e) {
            log.error("json转list失败---->", e);
        }
        return list;
    }
}
