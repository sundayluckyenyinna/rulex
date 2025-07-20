package com.yugrow.rulex.engine;

import com.yugrow.rulex.action.Action;
import com.yugrow.rulex.condition.Condition;
import com.yugrow.rulex.context.EvaluationContext;
import com.yugrow.rulex.domain.RuleDefinition;
import com.yugrow.rulex.domain.RuleExecutionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DefaultRuleDefinitionExecutor implements RuleDefinitionExecutor{

    private final ActionExecutor actionExecutor;

    @Override
    public RuleExecutionResult execute(RuleDefinition ruleDefinition, EvaluationContext context){
        Condition conditions = ruleDefinition.getConditions();
        List<Action> actions = ruleDefinition.getActions();

        boolean conditionMatched = conditions.evaluate(context);
        Map<String, List<String>> actionExecutionResult = null;
        if(conditionMatched) {
            actionExecutionResult = actionExecutor.executeAll(actions, context);
        }
        return RuleExecutionResult.builder()
                .conditionMatched(conditionMatched)
                .actionsExecuted(Objects.nonNull(actionExecutionResult) ? actionExecutionResult.get(ActionExecutor.ACTIONS_EXECUTED) : new ArrayList<>())
                .actionsNotExecuted(Objects.nonNull(actionExecutionResult) ? actionExecutionResult.get(ActionExecutor.ACTIONS_NOT_EXECUTED) : new ArrayList<>())
                .build();
    }
}
