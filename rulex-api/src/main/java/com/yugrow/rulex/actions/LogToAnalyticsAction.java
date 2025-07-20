package com.yugrow.rulex.actions;

import com.yugrow.rulex.action.Action;
import com.yugrow.rulex.context.EvaluationContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class LogToAnalyticsAction implements Action {

    private String type = "log_to_analytics";
    private String event;

    @Override
    public void execute(EvaluationContext context) {
        System.out.printf("Analytics of event: %s logged successfully%n", event);
    }

    @Override
    public String getType() {
        return type;
    }
}
