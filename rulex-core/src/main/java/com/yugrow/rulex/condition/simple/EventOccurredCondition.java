package com.yugrow.rulex.condition.simple;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yugrow.rulex.common.TimeBoundAware;
import com.yugrow.rulex.condition.Condition;
import com.yugrow.rulex.condition.ConditionTypes;
import com.yugrow.rulex.context.EvaluationContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventOccurredCondition implements Condition, TimeBoundAware {

    private String type = ConditionTypes.EVENT_OCCURRED;
    private String event;
    private int withinMinutes;

    @Override
    public Duration getTimeWindow() {
        return Duration.ofMinutes(this.getWithinMinutes());
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean evaluate(EvaluationContext context) {
        return !context.getAllEventsByNameWithinTimeWindow(event, this.getTimeWindow()).isEmpty();
    }
}
