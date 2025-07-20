package com.yugrow.rulex;

import com.yugrow.rulex.condition.simple.SequenceCondition;
import com.yugrow.rulex.context.DefaultEvaluationContext;
import com.yugrow.rulex.context.EvaluationContext;
import com.yugrow.rulex.domain.Event;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SequenceConditionTest {

    @Test
    void shouldMatchIfSequenceOccursInOrderWithinWindow() {
        SequenceCondition condition = new SequenceCondition();
        condition.setType("sequence");
        condition.setEvents(Arrays.asList("A", "B", "C"));
        condition.setWithinMinutes(60);

        ZonedDateTime now = ZonedDateTime.now();
        List<Event> events = List.of(
                new Event("A", now.minusMinutes(50), "user-1", null),
                new Event("B", now.minusMinutes(40), "user-1", null),
                new Event("C", now.minusMinutes(30), "user-1", null)
        );
        EvaluationContext context = new DefaultEvaluationContext(events, new HashMap<>());
        assertTrue(condition.evaluate(context));
    }

    @Test
    void shouldNotMatchIfSequenceIsOutOfOrder() {
        SequenceCondition condition = new SequenceCondition();
        condition.setType("sequence");
        condition.setEvents(Arrays.asList("A", "B", "C"));
        condition.setWithinMinutes(60);

        ZonedDateTime now = ZonedDateTime.now();
        List<Event> events = List.of(
                new Event("B", now.minusMinutes(50), "user-1", null),
                new Event("A", now.minusMinutes(40), "user-1", null),
                new Event("C", now.minusMinutes(30), "user-1", null)
        );
        EvaluationContext context = new DefaultEvaluationContext(events, new HashMap<>());
        assertFalse(condition.evaluate(context));
    }

    @Test
    void shouldNotMatchIfNotAllEventsPresent() {
        SequenceCondition condition = new SequenceCondition();
        condition.setType("sequence");
        condition.setEvents(Arrays.asList("A", "B", "C"));
        condition.setWithinMinutes(60);

        ZonedDateTime now = ZonedDateTime.now();
        List<Event> events = List.of(
                new Event("A", now.minusMinutes(50), "user-1", null),
                new Event("B", now.minusMinutes(40), "user-1", null)
        );
        EvaluationContext context = new DefaultEvaluationContext(events, new HashMap<>());
        assertFalse(condition.evaluate(context));
    }

    @Test
    void shouldNotMatchIfSequenceOccursOutsideWindow() {
        SequenceCondition condition = new SequenceCondition();
        condition.setType("sequence");
        condition.setEvents(Arrays.asList("A", "B", "C"));
        condition.setWithinMinutes(10);

        ZonedDateTime now = ZonedDateTime.now();
        List<Event> events = List.of(
                new Event("A", now.minusMinutes(50), "user-1", null),
                new Event("B", now.minusMinutes(40), "user-1", null),
                new Event("C", now.minusMinutes(30), "user-1", null)
        );
        EvaluationContext context = new DefaultEvaluationContext(events, new HashMap<>());
        assertFalse(condition.evaluate(context));
    }
} 