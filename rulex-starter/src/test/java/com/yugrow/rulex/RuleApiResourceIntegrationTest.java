package com.yugrow.rulex;

import com.yugrow.rulex.domain.Event;
import com.yugrow.rulex.domain.RuleExecutionResult;
import com.yugrow.rulex.payload.RuleEvaluationRequestPayload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = com.yugrow.rulex.RulexApiApplication.class
)
public class RuleApiResourceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/rule";
    }

    @Test
    void attributeMatchConditionShouldMatch() {
        String ruleJson = "{" +
                "\"conditions\": {\"type\": \"attribute_match\", \"field\": \"status\", \"operator\": \"==\", \"value\": \"active\"}," +
                "\"actions\": [{\"type\": \"send_email\", \"template\": \"welcome\"}]" +
                "}";
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("status", "active");
        RuleEvaluationRequestPayload payload = new RuleEvaluationRequestPayload(ruleJson, List.of(), userAttributes);

        ResponseEntity<RuleExecutionResult> response = restTemplate.postForEntity(getBaseUrl(), payload, RuleExecutionResult.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().isConditionMatched());
        Assertions.assertTrue(response.getBody().getActionsExecuted().contains("send_email"));
    }

    @Test
    void eventOccurredConditionShouldMatch() {
        String ruleJson = "{" +
                "\"conditions\": {\"type\": \"event_occurred\", \"event\": \"Login\", \"withinMinutes\": 60}," +
                "\"actions\": [{\"type\": \"send_email\", \"template\": \"welcome\"}]" +
                "}";
        Event event = new Event("Login", ZonedDateTime.now(), "user-1", new HashMap<>());
        RuleEvaluationRequestPayload payload = new RuleEvaluationRequestPayload(ruleJson, List.of(event), new HashMap<>());

        ResponseEntity<RuleExecutionResult> response = restTemplate.postForEntity(getBaseUrl(), payload, RuleExecutionResult.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().isConditionMatched());
        Assertions.assertTrue(response.getBody().getActionsExecuted().contains("send_email"));
    }

    @Test
    void andConditionShouldMatchIfBothTrue() {
        String ruleJson = "{" +
                "\"conditions\": {\"type\": \"and\", \"rules\": [" +
                "{\"type\": \"attribute_match\", \"field\": \"status\", \"operator\": \"==\", \"value\": \"active\"}," +
                "{\"type\": \"attribute_match\", \"field\": \"score\", \"operator\": \">=\", \"value\": 10}" +
                "]}," +
                "\"actions\": [{\"type\": \"send_email\", \"template\": \"welcome\"}]" +
                "}";
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("status", "active");
        userAttributes.put("score", 15);
        RuleEvaluationRequestPayload payload = new RuleEvaluationRequestPayload(ruleJson, List.of(), userAttributes);

        ResponseEntity<RuleExecutionResult> response = restTemplate.postForEntity(getBaseUrl(), payload, RuleExecutionResult.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().isConditionMatched());
        Assertions.assertTrue(response.getBody().getActionsExecuted().contains("send_email"));
    }

    @Test
    void andConditionShouldNotMatchIfOneFalse() {
        String ruleJson = "{" +
                "\"conditions\": {\"type\": \"and\", \"rules\": [" +
                "{\"type\": \"attribute_match\", \"field\": \"status\", \"operator\": \"==\", \"value\": \"active\"}," +
                "{\"type\": \"attribute_match\", \"field\": \"score\", \"operator\": \">=\", \"value\": 20}" +
                "]}," +
                "\"actions\": [{\"type\": \"send_email\", \"template\": \"welcome\"}]" +
                "}";
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("status", "active");
        userAttributes.put("score", 15);
        RuleEvaluationRequestPayload payload = new RuleEvaluationRequestPayload(ruleJson, List.of(), userAttributes);

        ResponseEntity<RuleExecutionResult> response = restTemplate.postForEntity(getBaseUrl(), payload, RuleExecutionResult.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertFalse(response.getBody().isConditionMatched());
        Assertions.assertTrue(response.getBody().getActionsExecuted().isEmpty());
    }

    @Test
    void invalidRuleJsonShouldReturnError() {
        String ruleJson = "{" +
                "\"conditions\": {\"type\": \"unknown_type\"}," +
                "\"actions\": [{\"type\": \"send_email\", \"template\": \"welcome\"}]" +
                "}";
        RuleEvaluationRequestPayload payload = new RuleEvaluationRequestPayload(ruleJson, List.of(), new HashMap<>());

        ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl(), payload, String.class);
        Assertions.assertTrue(response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError());
    }

    @Test
    void orConditionShouldMatchIfAnyTrue() {
        String ruleJson = "{" +
                "\"conditions\": {\"type\": \"or\", \"rules\": [" +
                "{\"type\": \"attribute_match\", \"field\": \"status\", \"operator\": \"==\", \"value\": \"inactive\"}," +
                "{\"type\": \"attribute_match\", \"field\": \"score\", \"operator\": \">=\", \"value\": 10}" +
                "]}," +
                "\"actions\": [{\"type\": \"send_email\", \"template\": \"welcome\"}]" +
                "}";
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("status", "active");
        userAttributes.put("score", 15);
        RuleEvaluationRequestPayload payload = new RuleEvaluationRequestPayload(ruleJson, List.of(), userAttributes);

        ResponseEntity<RuleExecutionResult> response = restTemplate.postForEntity(getBaseUrl(), payload, RuleExecutionResult.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().isConditionMatched());
        Assertions.assertTrue(response.getBody().getActionsExecuted().contains("send_email"));
    }

    @Test
    void orConditionShouldNotMatchIfAllFalse() {
        String ruleJson = "{" +
                "\"conditions\": {\"type\": \"or\", \"rules\": [" +
                "{\"type\": \"attribute_match\", \"field\": \"status\", \"operator\": \"==\", \"value\": \"inactive\"}," +
                "{\"type\": \"attribute_match\", \"field\": \"score\", \"operator\": \">=\", \"value\": 20}" +
                "]}," +
                "\"actions\": [{\"type\": \"send_email\", \"template\": \"welcome\"}]" +
                "}";
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("status", "active");
        userAttributes.put("score", 15);
        RuleEvaluationRequestPayload payload = new RuleEvaluationRequestPayload(ruleJson, List.of(), userAttributes);

        ResponseEntity<RuleExecutionResult> response = restTemplate.postForEntity(getBaseUrl(), payload, RuleExecutionResult.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertFalse(response.getBody().isConditionMatched());
        Assertions.assertTrue(response.getBody().getActionsExecuted().isEmpty());
    }

    @Test
    void notConditionShouldNotMatchIfSubConditionTrue() {
        String ruleJson = "{" +
                "\"conditions\": {\"type\": \"not\", \"rule\": {" +
                "\"type\": \"attribute_match\", \"field\": \"status\", \"operator\": \"==\", \"value\": \"active\"}" +
                "}}," +
                "\"actions\": [{\"type\": \"send_email\", \"template\": \"welcome\"}]" +
                "}";
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("status", "active");
        RuleEvaluationRequestPayload payload = new RuleEvaluationRequestPayload(ruleJson, List.of(), userAttributes);

        ResponseEntity<RuleExecutionResult> response = restTemplate.postForEntity(getBaseUrl(), payload, RuleExecutionResult.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertFalse(response.getBody().isConditionMatched());
        Assertions.assertTrue(response.getBody().getActionsExecuted().isEmpty());
    }


    @Test
    void sequenceConditionShouldMatchIfInOrderWithinWindow() {
        String ruleJson = "{" +
                "\"conditions\": {\"type\": \"sequence\", \"events\": [\"A\", \"B\", \"C\"], \"withinMinutes\": 60}," +
                "\"actions\": [{\"type\": \"send_email\", \"template\": \"welcome\"}]" +
                "}";
        ZonedDateTime now = ZonedDateTime.now();
        List<Event> events = List.of(
                new Event("A", now.minusMinutes(50), "user-1", new HashMap<>()),
                new Event("B", now.minusMinutes(40), "user-1", new HashMap<>()),
                new Event("C", now.minusMinutes(30), "user-1", new HashMap<>())
        );
        RuleEvaluationRequestPayload payload = new RuleEvaluationRequestPayload(ruleJson, events, new HashMap<>());

        ResponseEntity<RuleExecutionResult> response = restTemplate.postForEntity(getBaseUrl(), payload, RuleExecutionResult.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().isConditionMatched());
        Assertions.assertTrue(response.getBody().getActionsExecuted().contains("send_email"));
    }

    @Test
    void sequenceConditionShouldNotMatchIfOutOfOrder() {
        String ruleJson = "{" +
                "\"conditions\": {\"type\": \"sequence\", \"events\": [\"A\", \"B\", \"C\"], \"withinMinutes\": 60}," +
                "\"actions\": [{\"type\": \"send_email\", \"template\": \"welcome\"}]" +
                "}";
        ZonedDateTime now = ZonedDateTime.now();
        List<Event> events = List.of(
                new Event("B", now.minusMinutes(50), "user-1", new HashMap<>()),
                new Event("A", now.minusMinutes(40), "user-1", new HashMap<>()),
                new Event("C", now.minusMinutes(30), "user-1", new HashMap<>())
        );
        RuleEvaluationRequestPayload payload = new RuleEvaluationRequestPayload(ruleJson, events, new HashMap<>());

        ResponseEntity<RuleExecutionResult> response = restTemplate.postForEntity(getBaseUrl(), payload, RuleExecutionResult.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertFalse(response.getBody().isConditionMatched());
        Assertions.assertTrue(response.getBody().getActionsExecuted().isEmpty());
    }

    @Test
    void notOccurredConditionShouldMatchIfEventNotOccurred() {
        String ruleJson = "{" +
                "\"conditions\": {\"type\": \"not_occurred\", \"event\": \"Login\", \"withinMinutes\": 60}," +
                "\"actions\": [{\"type\": \"send_email\", \"template\": \"welcome\"}]" +
                "}";
        List<Event> events = List.of(
                new Event("Login", ZonedDateTime.now().minusMinutes(120), "user-1", new HashMap<>())
        );
        RuleEvaluationRequestPayload payload = new RuleEvaluationRequestPayload(ruleJson, events, new HashMap<>());

        ResponseEntity<RuleExecutionResult> response = restTemplate.postForEntity(getBaseUrl(), payload, RuleExecutionResult.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().isConditionMatched());
        Assertions.assertTrue(response.getBody().getActionsExecuted().contains("send_email"));
    }

    @Test
    void notOccurredConditionShouldNotMatchIfEventOccurred() {
        String ruleJson = "{" +
                "\"conditions\": {\"type\": \"not_occurred\", \"event\": \"Login\", \"withinMinutes\": 60}," +
                "\"actions\": [{\"type\": \"send_email\", \"template\": \"welcome\"}]" +
                "}";
        List<Event> events = List.of(
                new Event("Login", ZonedDateTime.now().minusMinutes(30), "user-1", new HashMap<>())
        );
        RuleEvaluationRequestPayload payload = new RuleEvaluationRequestPayload(ruleJson, events, new HashMap<>());

        ResponseEntity<RuleExecutionResult> response = restTemplate.postForEntity(getBaseUrl(), payload, RuleExecutionResult.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertFalse(response.getBody().isConditionMatched());
        Assertions.assertTrue(response.getBody().getActionsExecuted().isEmpty());
    }
} 