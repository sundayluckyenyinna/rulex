package com.yugrow.rulex;

import com.yugrow.rulex.condition.simple.NotOccurredCondition;
import com.yugrow.rulex.context.DefaultEvaluationContext;
import com.yugrow.rulex.context.EvaluationContext;
import com.yugrow.rulex.domain.Event;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotOccurredConditionTest {

    @Test
    void shouldMatchIfEventDidNotOccurWithinWindow() {
        NotOccurredCondition condition = new NotOccurredCondition();
        condition.setType("not_occurred");
        condition.setEvent("Login");
        condition.setWithinMinutes(60);

        List<Event> events = List.of(
                new Event("Login", ZonedDateTime.now().minusMinutes(120), "user-1", null)
        );
        EvaluationContext context = new DefaultEvaluationContext(events, new HashMap<>());
        assertTrue(condition.evaluate(context));
    }

    @Test
    void shouldNotMatchIfEventDidOccurWithinWindow() {
        NotOccurredCondition condition = new NotOccurredCondition();
        condition.setType("not_occurred");
        condition.setEvent("Login");
        condition.setWithinMinutes(60);

        List<Event> events = List.of(
                new Event("Login", ZonedDateTime.now().minusMinutes(30), "user-1", null)
        );
        EvaluationContext context = new DefaultEvaluationContext(events, new HashMap<>());
        assertFalse(condition.evaluate(context));
    }

    @Test
    void shouldMatchIfNoEventsAtAll() {
        NotOccurredCondition condition = new NotOccurredCondition();
        condition.setType("not_occurred");
        condition.setEvent("Login");
        condition.setWithinMinutes(60);

        List<Event> events = List.of();
        EvaluationContext context = new DefaultEvaluationContext(events, new HashMap<>());
        assertTrue(condition.evaluate(context));
    }
} 