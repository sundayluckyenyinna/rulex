package com.yugrow.rulex.context;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yugrow.rulex.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultEvaluationContext implements EvaluationContext{

    private List<Event> events = new ArrayList<>();
    private Map<String, Object> evaluationProperties = new HashMap<>();

    @Override
    public Object getAttribute(String field){
        return evaluationProperties.get(field);
    }

    @Override
    public List<Event> getAllEventsByName(String name){
        return events.stream().filter(event -> event.getName().equalsIgnoreCase(name)).toList();
    }

    @Override
    public List<Event> getAllEventsByNameSorted(String name){
        return getAllEventsByName(name).stream()
                .sorted(Comparator.comparing(Event::getTimestamp)).toList();
    }

    @Override
    public List<Event> getAllEventsWithNameInAndSorted(List<String> names){
        return events.stream()
                .filter(event -> names.contains(event.getName()))
                .sorted(Comparator.comparing(Event::getTimestamp))
                .toList();
    }

    @Override
    public List<Event> getAllEventsByNameWithinTimeWindow(String name, Duration duration){
        ZonedDateTime startTime = getStartTimeWindow(duration);
        return getAllEventsByName(name).stream().filter(event -> event.getTimestamp().isAfter(startTime)).toList();
    }

    @Override
    public ZonedDateTime getStartTimeWindow(Duration duration){
        ZonedDateTime now = ZonedDateTime.now();
        return now.minus(duration);
    }
}
