package com.yugrow.rulex.condition.composite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yugrow.rulex.condition.Condition;
import com.yugrow.rulex.condition.ConditionTypes;
import com.yugrow.rulex.context.EvaluationContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotCondition implements Condition {

    private String type = ConditionTypes.NOT;
    private Condition rule;

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public boolean evaluate(EvaluationContext context) {
        return Objects.nonNull(rule) && !rule.evaluate(context);
    }
}
