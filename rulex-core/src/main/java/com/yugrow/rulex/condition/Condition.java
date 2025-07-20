package com.yugrow.rulex.condition;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.yugrow.rulex.common.TypeIdentifiable;
import com.yugrow.rulex.context.EvaluationContext;
import com.yugrow.rulex.parser.ConditionTypeIdResolver;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CUSTOM,
        property = "type"
)
@JsonTypeIdResolver(ConditionTypeIdResolver.class)
public interface Condition extends TypeIdentifiable {

    boolean evaluate(EvaluationContext context);
}
