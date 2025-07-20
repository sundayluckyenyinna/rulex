package com.yugrow.rulex.action;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.yugrow.rulex.common.TypeIdentifiable;
import com.yugrow.rulex.context.EvaluationContext;
import com.yugrow.rulex.parser.ActionTypeIdResolver;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CUSTOM,
        property = "type"
)
@JsonTypeIdResolver(ActionTypeIdResolver.class)
public interface Action extends TypeIdentifiable {

    void execute(EvaluationContext context);
}
