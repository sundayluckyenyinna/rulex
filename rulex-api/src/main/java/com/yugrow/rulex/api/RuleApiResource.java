package com.yugrow.rulex.api;

import com.yugrow.rulex.domain.RuleExecutionResult;
import com.yugrow.rulex.payload.RuleEvaluationRequestPayload;
import com.yugrow.rulex.service.RuleDefinitionTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/rule", produces = MediaType.APPLICATION_JSON_VALUE)
public class RuleApiResource {

    private final RuleDefinitionTestService ruleDefinitionTestService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RuleExecutionResult> evaluateRule(@Validated @RequestBody RuleEvaluationRequestPayload requestPayload){
        return ResponseEntity.ok(ruleDefinitionTestService.processRuleEvaluation(requestPayload));
    }
}
