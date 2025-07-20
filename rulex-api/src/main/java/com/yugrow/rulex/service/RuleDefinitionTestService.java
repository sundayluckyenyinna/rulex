package com.yugrow.rulex.service;

import com.yugrow.rulex.domain.RuleExecutionResult;
import com.yugrow.rulex.payload.RuleEvaluationRequestPayload;

public interface RuleDefinitionTestService {
    RuleExecutionResult processRuleEvaluation(RuleEvaluationRequestPayload requestPayload);
}
