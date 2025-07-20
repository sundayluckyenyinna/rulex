package com.yugrow.rulex;

import com.yugrow.rulex.condition.composite.OrCondition;
import com.yugrow.rulex.condition.simple.AttributeMatchCondition;
import com.yugrow.rulex.context.DefaultEvaluationContext;
import com.yugrow.rulex.context.EvaluationContext;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OrConditionTest {

    @Test
    void shouldMatchIfAnyConditionTrue() {
        AttributeMatchCondition cond1 = new AttributeMatchCondition();
        cond1.setType("attribute_match");
        cond1.setField("status");
        cond1.setOperator("==");
        cond1.setValue("active");

        AttributeMatchCondition cond2 = new AttributeMatchCondition();
        cond2.setType("attribute_match");
        cond2.setField("score");
        cond2.setOperator(">=");
        cond2.setValue(20);

        OrCondition orCondition = new OrCondition();
        orCondition.setType("or");
        orCondition.setRules(Arrays.asList(cond1, cond2));

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("status", "active");
        attributes.put("score", 15);
        EvaluationContext context = new DefaultEvaluationContext(List.of(), attributes);
        assertTrue(orCondition.evaluate(context));
    }

    @Test
    void shouldNotMatchIfAllConditionsFalse() {
        AttributeMatchCondition cond1 = new AttributeMatchCondition();
        cond1.setType("attribute_match");
        cond1.setField("status");
        cond1.setOperator("==");
        cond1.setValue("inactive");

        AttributeMatchCondition cond2 = new AttributeMatchCondition();
        cond2.setType("attribute_match");
        cond2.setField("score");
        cond2.setOperator(">=");
        cond2.setValue(20);

        OrCondition orCondition = new OrCondition();
        orCondition.setType("or");
        orCondition.setRules(Arrays.asList(cond1, cond2));

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("status", "active");
        attributes.put("score", 15);
        EvaluationContext context = new DefaultEvaluationContext(List.of(), attributes);
        assertFalse(orCondition.evaluate(context));
    }

    @Test
    void shouldNotMatchIfNoConditions() {
        OrCondition orCondition = new OrCondition();
        orCondition.setType("or");
        orCondition.setRules(List.of());
        EvaluationContext context = new DefaultEvaluationContext(List.of(), new HashMap<>());
        assertFalse(orCondition.evaluate(context));
    }
} 