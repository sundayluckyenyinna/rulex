package com.yugrow.rulex.engine;

import com.yugrow.rulex.action.Action;
import com.yugrow.rulex.context.EvaluationContext;

import java.util.List;
import java.util.Map;

public interface ActionExecutor {
    String ACTIONS_EXECUTED = "ACTIONS_EXECUTED";
    String ACTIONS_NOT_EXECUTED = "ACTIONS_NOT_EXECUTED";

    Map<String, List<String>> executeAll(List<Action> actions, EvaluationContext context);
}
