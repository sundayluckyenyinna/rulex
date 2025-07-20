package com.yugrow.rulex.context;

import com.yugrow.rulex.domain.Event;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

public interface EvaluationContext {
    Object getAttribute(String field);

    List<Event> getAllEventsByName(String name);

    List<Event> getAllEventsByNameSorted(String name);

    List<Event> getAllEventsWithNameInAndSorted(List<String> names);

    List<Event> getAllEventsByNameWithinTimeWindow(String name, Duration duration);

    ZonedDateTime getStartTimeWindow(Duration duration);
}
