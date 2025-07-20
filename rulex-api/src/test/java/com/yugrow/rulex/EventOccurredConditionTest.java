package com.yugrow.rulex;

import com.yugrow.rulex.condition.simple.EventOccurredCondition;
import com.yugrow.rulex.context.DefaultEvaluationContext;
import com.yugrow.rulex.context.EvaluationContext;
import com.yugrow.rulex.domain.Event;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventOccurredConditionTest {

    @Test
    void shouldMatchIfEventOccurredWithinWindow() {
        EventOccurredCondition condition = new EventOccurredCondition();
        condition.setType("event_occurred");
        condition.setEvent("Login");
        condition.setWithinMinutes(60);

        List<Event> events = List.of(
                new Event("Login", ZonedDateTime.now().minusMinutes(30), "user-1", null)
        );
        EvaluationContext context = new DefaultEvaluationContext(events, new HashMap<>());
        assertTrue(condition.evaluate(context));
    }

    @Test
    void shouldNotMatchIfEventOccurredOutsideWindow() {
        EventOccurredCondition condition = new EventOccurredCondition();
        condition.setType("event_occurred");
        condition.setEvent("Login");
        condition.setWithinMinutes(10);

        List<Event> events = List.of(
                new Event("Login", ZonedDateTime.now().minusMinutes(30), "user-1", null)
        );
        EvaluationContext context = new DefaultEvaluationContext(events, new HashMap<>());
        assertFalse(condition.evaluate(context));
    }

    @Test
    void shouldNotMatchIfNoEventOccurred() {
        EventOccurredCondition condition = new EventOccurredCondition();
        condition.setType("event_occurred");
        condition.setEvent("Login");
        condition.setWithinMinutes(60);

        List<Event> events = List.of(
                new Event("Logout", ZonedDateTime.now().minusMinutes(5), "user-1", null)
        );
        EvaluationContext context = new DefaultEvaluationContext(events, new HashMap<>());
        assertFalse(condition.evaluate(context));
    }

    @Test
    void shouldMatchIfMultipleEventsAndOneWithinWindow() {
        EventOccurredCondition condition = new EventOccurredCondition();
        condition.setType("event_occurred");
        condition.setEvent("Login");
        condition.setWithinMinutes(60);

        List<Event> events = List.of(
                new Event("Login", ZonedDateTime.now().minusMinutes(120), "user-1", null),
                new Event("Login", ZonedDateTime.now().minusMinutes(30), "user-1", null)
        );
        EvaluationContext context = new DefaultEvaluationContext(events, new HashMap<>());
        assertTrue(condition.evaluate(context));
    }
} 