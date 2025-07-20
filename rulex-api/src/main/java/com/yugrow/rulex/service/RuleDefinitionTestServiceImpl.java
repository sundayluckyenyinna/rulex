package com.yugrow.rulex.service;

import com.yugrow.rulex.context.DefaultEvaluationContext;
import com.yugrow.rulex.context.EvaluationContext;
import com.yugrow.rulex.domain.Event;
import com.yugrow.rulex.domain.RuleDefinition;
import com.yugrow.rulex.domain.RuleExecutionResult;
import com.yugrow.rulex.engine.RuleDefinitionExecutor;
import com.yugrow.rulex.parser.RuleDefinitionParser;
import com.yugrow.rulex.payload.RuleEvaluationRequestPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RuleDefinitionTestServiceImpl implements RuleDefinitionTestService{

    private final RuleDefinitionParser ruleDefinitionParser;
    private final RuleDefinitionExecutor ruleDefinitionExecutor;

    @Override
    public RuleExecutionResult processRuleEvaluation(RuleEvaluationRequestPayload requestPayload){
        try {
            String ruleJson = requestPayload.getRuleJson();
            List<Event> events = requestPayload.getEvents();
            Map<String, Object> userAttribute = requestPayload.getUserAttributes();
            EvaluationContext context = new DefaultEvaluationContext(events, userAttribute); //TODO: clients are allowed to implement their own
            RuleDefinition ruleDefinition = ruleDefinitionParser.parseRule(ruleJson);
            return ruleDefinitionExecutor.execute(ruleDefinition, context);
        }catch (Exception e){
            // TODO: Throw custom exception to be caught in a proper @RestControllerAdvice class
            throw new RuntimeException("Failed to execute rule. Exception message is: %s".formatted(e.getMessage()), e);
        }
    }
}
