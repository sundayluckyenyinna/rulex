package com.yugrow.rulex.parser;

import com.yugrow.rulex.domain.RuleDefinition;

public interface RuleDefinitionParser {
    RuleDefinition parseRule(String ruleJson);
}
