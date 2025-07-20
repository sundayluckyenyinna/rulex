package com.yugrow.rulex;

import com.yugrow.rulex.condition.simple.EventCountCondition;
import com.yugrow.rulex.context.DefaultEvaluationContext;
import com.yugrow.rulex.context.EvaluationContext;
import com.yugrow.rulex.domain.Event;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventCountConditionTest {

    @Test
    void shouldMatchIfCountWithinDays() {
        EventCountCondition condition = new EventCountCondition();
        condition.setType("event_count");
        condition.setEvent("Login");
        condition.setOperator(">=");
        condition.setValue(2);
        condition.setWithinDays(3);

        List<Event> events = List.of(
                new Event("Login", ZonedDateTime.now().minusDays(1), "user-1", null),
                new Event("Login", ZonedDateTime.now().minusDays(2), "user-1", null)
        );

        EvaluationContext context = new DefaultEvaluationContext(events, new HashMap<>());
        assertTrue(condition.evaluate(context));
    }

    @Test
    void shouldFailIfEventCountBelowThreshold() {
        EventCountCondition condition = new EventCountCondition();
        condition.setType("event_count");
        condition.setEvent("Login");
        condition.setOperator(">=");
        condition.setValue(3);
        condition.setWithinDays(5);

        List<Event> events = List.of(
                new Event("Login", ZonedDateTime.now().minusDays(1), "user-1", null)
        );

        EvaluationContext context = new DefaultEvaluationContext(events, new HashMap<>());
        assertFalse(condition.evaluate(context));
    }
}
