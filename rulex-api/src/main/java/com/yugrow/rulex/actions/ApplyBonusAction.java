package com.yugrow.rulex.actions;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.yugrow.rulex.action.Action;
import com.yugrow.rulex.context.EvaluationContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApplyBonusAction implements Action {

    private String type = "apply_bonus";
    private String bonusType;
    @Override
    public void execute(EvaluationContext context) {
        System.out.printf("Bonus of type: %s applied successfully for user%n", bonusType);
    }

    @Override
    public String getType() {
        return type;
    }
}
