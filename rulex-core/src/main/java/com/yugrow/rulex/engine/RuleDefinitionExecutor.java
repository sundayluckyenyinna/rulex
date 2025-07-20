package com.yugrow.rulex.engine;

import com.yugrow.rulex.context.EvaluationContext;
import com.yugrow.rulex.domain.RuleDefinition;
import com.yugrow.rulex.domain.RuleExecutionResult;

public interface RuleDefinitionExecutor {
    RuleExecutionResult execute(RuleDefinition ruleDefinition, EvaluationContext context);
}
