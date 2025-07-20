package com.yugrow.rulex;

import com.yugrow.rulex.context.EvaluationContext;
import com.yugrow.rulex.domain.RuleDefinition;
import com.yugrow.rulex.domain.RuleExecutionResult;
import com.yugrow.rulex.engine.RuleDefinitionExecutor;
import com.yugrow.rulex.parser.RuleDefinitionParser;
import com.yugrow.rulex.payload.RuleEvaluationRequestPayload;
import com.yugrow.rulex.service.RuleDefinitionTestServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class RuleEvaluationServiceImplTest {

    private final RuleDefinitionParser parser = mock(RuleDefinitionParser.class);
    private final RuleDefinitionExecutor executor = mock(RuleDefinitionExecutor.class);
    private final RuleDefinitionTestServiceImpl service = new RuleDefinitionTestServiceImpl(parser, executor);

    @Test
    void shouldEvaluateRuleSuccessfully() throws Exception {
        RuleDefinition rule = new RuleDefinition();
        RuleExecutionResult result = new RuleExecutionResult(true, new ArrayList<>(), new ArrayList<>());
        when(parser.parseRule(anyString())).thenReturn(rule);
        when(executor.execute(any(), any(EvaluationContext.class))).thenReturn(result);

        String ruleJson = "{\"conditions\":{}}";
        RuleEvaluationRequestPayload payload = new RuleEvaluationRequestPayload();
        payload.setRuleJson(ruleJson);
        payload.setEvents(new ArrayList<>());
        payload.setUserAttributes(new HashMap<>());
        RuleExecutionResult response = service.processRuleEvaluation(payload);

        assertTrue("Empty condition composition evaluated successfully", response.isConditionMatched());
        verify(parser).parseRule(anyString());
        verify(executor).execute(any(), any(EvaluationContext.class));
    }
}
