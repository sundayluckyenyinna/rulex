package com.yugrow.rulex;

import com.yugrow.rulex.condition.simple.AttributeMatchCondition;
import com.yugrow.rulex.context.DefaultEvaluationContext;
import com.yugrow.rulex.context.EvaluationContext;
import com.yugrow.rulex.domain.Event;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AttributeMatchConditionTest {

    @Test
    void shouldMatchStringEquality() {
        AttributeMatchCondition condition = new AttributeMatchCondition();
        condition.setType("attribute_match");
        condition.setField("status");
        condition.setOperator("==");
        condition.setValue("active");

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("status", "active");
        EvaluationContext context = new DefaultEvaluationContext(List.of(), attributes);
        assertTrue(condition.evaluate(context));
    }

    @Test
    void shouldNotMatchStringInequality() {
        AttributeMatchCondition condition = new AttributeMatchCondition();
        condition.setType("attribute_match");
        condition.setField("status");
        condition.setOperator("==");
        condition.setValue("inactive");

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("status", "active");
        EvaluationContext context = new DefaultEvaluationContext(List.of(), attributes);
        assertFalse(condition.evaluate(context));
    }

    @Test
    void shouldMatchNumberGreaterThan() {
        AttributeMatchCondition condition = new AttributeMatchCondition();
        condition.setType("attribute_match");
        condition.setField("score");
        condition.setOperator(">=");
        condition.setValue(10);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("score", 15);
        EvaluationContext context = new DefaultEvaluationContext(List.of(), attributes);
        assertTrue(condition.evaluate(context));
    }

    @Test
    void shouldNotMatchNumberLessThan() {
        AttributeMatchCondition condition = new AttributeMatchCondition();
        condition.setType("attribute_match");
        condition.setField("score");
        condition.setOperator(">=");
        condition.setValue(20);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("score", 15);
        EvaluationContext context = new DefaultEvaluationContext(List.of(), attributes);
        assertFalse(condition.evaluate(context));
    }

    @Test
    void shouldHandleNullAttribute() {
        AttributeMatchCondition condition = new AttributeMatchCondition();
        condition.setType("attribute_match");
        condition.setField("missing");
        condition.setOperator("==");
        condition.setValue(null);

        Map<String, Object> attributes = new HashMap<>();
        EvaluationContext context = new DefaultEvaluationContext(List.of(), attributes);
        assertTrue(condition.evaluate(context));
    }

    @Test
    void shouldHandleNullComparison() {
        AttributeMatchCondition condition = new AttributeMatchCondition();
        condition.setType("attribute_match");
        condition.setField("missing");
        condition.setOperator("!=");
        condition.setValue(null);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("missing", "something");
        EvaluationContext context = new DefaultEvaluationContext(List.of(), attributes);
        assertTrue(condition.evaluate(context));
    }
} 