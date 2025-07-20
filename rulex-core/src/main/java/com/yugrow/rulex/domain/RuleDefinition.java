package com.yugrow.rulex.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yugrow.rulex.action.Action;
import com.yugrow.rulex.condition.Condition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleDefinition {

    private Condition conditions;
    private List<Action> actions = new ArrayList<>();
}
