package com.yugrow.rulex.parser;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.yugrow.rulex.condition.Condition;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConditionTypeIdResolver implements TypeIdResolver {

    private JavaType javaType;
    protected static Map<String, Class<? extends Condition>> CONDITION_TO_CLASS_REGISTRY = new HashMap<>();

    @Override
    public void init(JavaType javaType) {
        this.javaType = javaType;
    }

    @Override
    public String idFromValue(Object o) {
        return idFromValueAndType(o, o.getClass());
    }

    @Override
    public String idFromValueAndType(Object o, Class<?> aClass) {
        return CONDITION_TO_CLASS_REGISTRY.entrySet().stream()
                .filter(entry -> entry.getValue().isAssignableFrom(aClass))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No id found for type: %s".formatted(aClass)));
    }

    @Override
    public String idFromBaseType() {
        return idFromValueAndType(null, javaType.getRawClass());
    }

    @Override
    public JavaType typeFromId(DatabindContext databindContext, String id) throws IOException {
        Class<? extends Condition> conditionClass = CONDITION_TO_CLASS_REGISTRY.entrySet().stream()
                .filter(entry -> entry.getKey().equals(id))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No type found for id"));
        return databindContext.constructSpecializedType(javaType, conditionClass);
    }

    @Override
    public String getDescForKnownTypeIds() {
        return null;
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
