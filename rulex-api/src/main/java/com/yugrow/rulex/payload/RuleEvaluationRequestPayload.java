package com.yugrow.rulex.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yugrow.rulex.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleEvaluationRequestPayload {

    private String ruleJson;
    private List<Event> events = new ArrayList<>();  // TODO: Use proper dto classes with proper spring validation annotations
    private Map<String, Object> userAttributes = new HashMap<>(); // TODO: Use proper dto for user profile and covert dto to map for the engine using java reflection api
}
