package com.stxx.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * JSON工具类 - 基于Jackson的统一JSON处理
 *
 * 提供统一的JSON序列化/反序列化功能，统一异常处理
 * 如果需要更换JSON库，只需要修改此类即可
 *
 * @author wangcc
 */
@Slf4j
public class JsonUtils {

    /**
     * ObjectMapper实例
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 注册Java 8时间模块，支持LocalDateTime等类型
        objectMapper.registerModule(new JavaTimeModule());
        // 忽略未知字段，避免反序列化时出现UnrecognizedPropertyException
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 私有构造函数，防止实例化
     */
    private JsonUtils() {
    }

    /**
     * 对象转JSON字符串
     *
     * @param obj 对象
     * @return JSON字符串，失败时返回"{}"
     */
    public static String toJsonString(Object obj) {
        if (obj == null) {
            return "{}";
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转JSON字符串失败", e);
            return "{}";
        }
    }

    /**
     * JSON字符串转对象
     *
     * @param json  JSON字符串
     * @param clazz 对象类型
     * @param <T>   泛型类型
     * @return 对象实例，失败时返回null
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("JSON字符串转对象失败: {}", json, e);
            return null;
        }
    }

    /**
     * JSON字符串转JsonNode
     *
     * @param json JSON字符串
     * @return JsonNode对象，失败时返回null
     */
    public static JsonNode parseJsonNode(String json) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            log.error("JSON字符串转JsonNode失败: {}", json, e);
            return null;
        }
    }

    /**
     * JSON字符串转List
     *
     * @param json  JSON字符串
     * @param clazz 元素类型
     * @param <T>   泛型类型
     * @return List对象，失败时返回空List
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        if (!StringUtils.hasText(json)) {
            return java.util.Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            log.error("JSON字符串转List失败: {}", json, e);
            return java.util.Collections.emptyList();
        }
    }

    /**
     * JSON字符串转Map
     *
     * @param json JSON字符串
     * @return Map对象，失败时返回空Map
     */
    public static Map<String, Object> parseMap(String json) {
        if (!StringUtils.hasText(json)) {
            return java.util.Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            log.error("JSON字符串转Map失败: {}", json, e);
            return java.util.Collections.emptyMap();
        }
    }

    /**
     * 对象转Map
     *
     * @param obj 对象
     * @return Map对象，失败时返回空Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> objectToMap(Object obj) {
        if (obj == null) {
            return java.util.Collections.emptyMap();
        }
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        try {
            return objectMapper.convertValue(obj, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            log.error("对象转Map失败", e);
            return java.util.Collections.emptyMap();
        }
    }

    /**
     * 获取ObjectMapper实例
     * 注意：仅在需要直接使用ObjectMapper的高级功能时使用
     *
     * @return ObjectMapper实例
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * 创建空的ObjectNode
     *
     * @return 空ObjectNode
     */
    public static JsonNode createObjectNode() {
        return objectMapper.createObjectNode();
    }
}
