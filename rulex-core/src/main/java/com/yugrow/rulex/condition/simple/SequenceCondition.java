package com.yugrow.rulex.condition.simple;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yugrow.rulex.common.TimeBoundAware;
import com.yugrow.rulex.condition.Condition;
import com.yugrow.rulex.condition.ConditionTypes;
import com.yugrow.rulex.context.EvaluationContext;
import com.yugrow.rulex.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SequenceCondition implements Condition, TimeBoundAware {

    private String type = ConditionTypes.SEQUENCE;
    private List<String> events = new ArrayList<>();
    private int withinMinutes;

    @Override
    public Duration getTimeWindow() {
        return Duration.ofMinutes(this.getWithinMinutes());
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public boolean evaluate(EvaluationContext context) {
        List<Event> allEventsWithNamesInStreamSorted = context.getAllEventsWithNameInAndSorted(events);

        boolean sequenceMatch = false;
        int sequenceCounter = 0;
        Event firstEventMatch = null;

        for(Event event : allEventsWithNamesInStreamSorted){
            String currentEventInCondition = events.get(sequenceCounter);
            if(event.getName().equalsIgnoreCase(currentEventInCondition)){

                if(sequenceCounter == 0){
                    firstEventMatch = event;
                }
                sequenceCounter++;

                if(sequenceCounter == events.size()){
                    Duration betweenFirstAndLastMatch = Duration.between(firstEventMatch.getTimestamp(), event.getTimestamp());
                    sequenceMatch = betweenFirstAndLastMatch.toMinutes() <= this.getWithinMinutes();
                }
            }
        }
        return sequenceMatch;
    }
}
