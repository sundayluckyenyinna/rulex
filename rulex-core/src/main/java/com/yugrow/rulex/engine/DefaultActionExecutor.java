package com.yugrow.rulex.engine;

import com.yugrow.rulex.action.Action;
import com.yugrow.rulex.context.EvaluationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DefaultActionExecutor implements ActionExecutor {

    @Override
    public Map<String, List<String>> executeAll(List<Action> actions, EvaluationContext context){
        Map<String, List<String>> actionExecutionResults = new HashMap<>();
        actionExecutionResults.put(ACTIONS_EXECUTED, new ArrayList<>());
        actionExecutionResults.put(ACTIONS_NOT_EXECUTED, new ArrayList<>());
        for(Action action : actions){
            try{
                action.execute(context);
                actionExecutionResults.get(ACTIONS_EXECUTED).add(action.getType());
            }catch (Exception e){
                actionExecutionResults.get(ACTIONS_NOT_EXECUTED).add(action.getType());
                // TODO: Call error listeners, send action alerts, etc
            }
        }
        return actionExecutionResults;
    }
}
