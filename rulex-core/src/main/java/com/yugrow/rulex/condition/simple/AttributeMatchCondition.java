package com.yugrow.rulex.condition.simple;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yugrow.rulex.common.AttributeAware;
import com.yugrow.rulex.condition.Condition;
import com.yugrow.rulex.condition.ConditionTypes;
import com.yugrow.rulex.context.EvaluationContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttributeMatchCondition implements Condition, AttributeAware {

    private String type = ConditionTypes.ATTRIBUTE_MATCH;
    private String field;
    private String operator;
    private Object value;

    @Override
    public String getField() {
        return this.field;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public boolean evaluate(EvaluationContext context) {
        Object actualValueInEvalProperties = context.getAttribute(this.getField());
        Object valueToCompare = actualValueInEvalProperties instanceof String ? "'" + this.getValue() + "'" : this.getValue();
        String expression = String.format("#%s %s %s", this.getField(), this.getOperator(), valueToCompare);

        ExpressionParser expressionParser = new SpelExpressionParser();
        org.springframework.expression.EvaluationContext spelContext = new StandardEvaluationContext();
        spelContext.setVariable(this.getField(), actualValueInEvalProperties);
        try{
            Boolean result = expressionParser.parseExpression(expression).getValue(spelContext, Boolean.class);
            return Boolean.TRUE.equals(result);
        }catch (Exception e) {
            return false;
        }
    }
}
