package com.yugrow.rulex.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleExecutionResult {

    private boolean conditionMatched;
    private List<String> actionsExecuted = new ArrayList<>();
    private List<String> actionsNotExecuted = new ArrayList<>();
}
