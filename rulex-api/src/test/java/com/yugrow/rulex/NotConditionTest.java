package com.yugrow.rulex;

import com.yugrow.rulex.condition.composite.NotCondition;
import com.yugrow.rulex.condition.simple.AttributeMatchCondition;
import com.yugrow.rulex.context.DefaultEvaluationContext;
import com.yugrow.rulex.context.EvaluationContext;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class NotConditionTest {

    @Test
    void shouldReturnFalseIfSubConditionTrue() {
        AttributeMatchCondition cond = new AttributeMatchCondition();
        cond.setType("attribute_match");
        cond.setField("status");
        cond.setOperator("==");
        cond.setValue("active");

        NotCondition notCondition = new NotCondition();
        notCondition.setType("not");
        notCondition.setRule(cond);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("status", "active");
        EvaluationContext context = new DefaultEvaluationContext(List.of(), attributes);
        assertFalse(notCondition.evaluate(context));
    }

    @Test
    void shouldReturnTrueIfSubConditionFalse() {
        AttributeMatchCondition cond = new AttributeMatchCondition();
        cond.setType("attribute_match");
        cond.setField("status");
        cond.setOperator("==");
        cond.setValue("inactive");

        NotCondition notCondition = new NotCondition();
        notCondition.setType("not");
        notCondition.setRule(cond);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("status", "active");
        EvaluationContext context = new DefaultEvaluationContext(List.of(), attributes);
        assertTrue(notCondition.evaluate(context));
    }

    @Test
    void shouldHandleDoubleNegation() {
        AttributeMatchCondition cond = new AttributeMatchCondition();
        cond.setType("attribute_match");
        cond.setField("status");
        cond.setOperator("==");
        cond.setValue("active");

        NotCondition notCondition1 = new NotCondition();
        notCondition1.setType("not");
        notCondition1.setRule(cond);

        NotCondition notCondition2 = new NotCondition();
        notCondition2.setType("not");
        notCondition2.setRule(notCondition1);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("status", "active");
        EvaluationContext context = new DefaultEvaluationContext(List.of(), attributes);
        assertTrue(notCondition2.evaluate(context));
    }
} 