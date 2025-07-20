package com.yugrow.rulex.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yugrow.rulex.action.Action;
import com.yugrow.rulex.domain.RuleDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultRuleDefinitionParser implements RuleDefinitionParser{

    private final ApplicationContext applicationContext;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    }

    @Override
    public RuleDefinition parseRule(String ruleJson){
        try {
            RuleDefinition ruleDefinition = OBJECT_MAPPER.readValue(ruleJson, RuleDefinition.class);
            List<Action> actions = ruleDefinition.getActions();
            for(Action action : actions){
                applicationContext.getAutowireCapableBeanFactory().autowireBean(action); // This injects other spring bean in each actions
            }
            return ruleDefinition;
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid rule definition", e);
        }
    }
}
