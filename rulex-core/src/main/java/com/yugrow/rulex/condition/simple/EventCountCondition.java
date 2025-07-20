package com.yugrow.rulex.condition.simple;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yugrow.rulex.common.TimeBoundAware;
import com.yugrow.rulex.condition.Condition;
import com.yugrow.rulex.condition.ConditionTypes;
import com.yugrow.rulex.context.EvaluationContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventCountCondition implements Condition, TimeBoundAware {

    private String type = ConditionTypes.EVENT_COUNT;
    private String event;
    private String operator;
    private int value;
    private int withinDays;

    @Override
    public Duration getTimeWindow() {
        return Duration.ofDays(this.getWithinDays());
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public boolean evaluate(EvaluationContext context) {
        long actualEventCountInStream = context.getAllEventsByNameWithinTimeWindow(event, this.getTimeWindow()).size();
        String expression = String.format("#count %s %s", this.getOperator(), this.getValue());

        ExpressionParser expressionParser = new SpelExpressionParser();
        org.springframework.expression.EvaluationContext spelContext = new StandardEvaluationContext();
        spelContext.setVariable("count", actualEventCountInStream);
        try{
            Boolean result = expressionParser.parseExpression(expression).getValue(spelContext, Boolean.class);
            return Boolean.TRUE.equals(result);
        }catch (Exception e) {
            return false;
        }
    }
}
