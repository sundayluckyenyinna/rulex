package com.yugrow.rulex.condition.composite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yugrow.rulex.condition.Condition;
import com.yugrow.rulex.condition.ConditionTypes;
import com.yugrow.rulex.context.EvaluationContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AndCondition implements Condition {

    private String type = ConditionTypes.AND;
    private List<Condition> rules = new ArrayList<>();

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public boolean evaluate(EvaluationContext context) {
        return rules.stream().allMatch(condition -> condition.evaluate(context));
    }
}
