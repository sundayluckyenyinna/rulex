package com.yugrow.rulex.parser;

import com.yugrow.rulex.action.Action;
import com.yugrow.rulex.condition.Condition;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ParserRegistryConfiguration {

    private final ApplicationContext applicationContext;
    public static String basePackage = "com.yugrow.rulex";


    @PostConstruct
    public void postConstruct(){
        initializeConditions();
        initializeActions();
    }

    @SuppressWarnings("unchecked")
    public void initializeConditions(){
        Map<String, Class<? extends Condition>> conditionMappings = new HashMap<>();
        Set<BeanDefinition> conditionClasses = getBeanDefinitionsOf(Condition.class);
        for(BeanDefinition beanDefinition : conditionClasses){
            try{
                Class<? extends Condition> conditionClass = (Class<? extends Condition>) Class.forName(beanDefinition.getBeanClassName());
                Condition condition = conditionClass.getDeclaredConstructor().newInstance();
                conditionMappings.put(condition.getType(), conditionClass);
                log.info("Registered condition class: {}", conditionClass.getSimpleName());
            }catch (Exception e){
                log.error("Failed to register condition class: {}. Exception is: {}", beanDefinition.getBeanClassName(), e.getMessage());
            }
        }
        ConditionTypeIdResolver.CONDITION_TO_CLASS_REGISTRY = conditionMappings;
    }

    @SuppressWarnings("unchecked")
    public void initializeActions(){
        Map<String, Class<? extends Action>> conditionMappings = new HashMap<>();
        Set<BeanDefinition> conditionClasses = getBeanDefinitionsOf(Action.class);
        for(BeanDefinition beanDefinition : conditionClasses){
            try{
                Class<? extends Action> actionClass = (Class<? extends Action>) Class.forName(beanDefinition.getBeanClassName());
                Action action = actionClass.getDeclaredConstructor().newInstance();
                conditionMappings.put(action.getType(), actionClass);
                log.info("Registered actions class: {}", actionClass.getSimpleName());
            }catch (Exception e){
                log.error("Failed to register action class: {}. Exception is: {}", beanDefinition.getBeanClassName(), e.getMessage());
            }
        }
        ActionTypeIdResolver.ACTION_TO_CLASS_REGISTRY = conditionMappings;
    }

    public Set<BeanDefinition> getBeanDefinitionsOf(Class<?> interfaceType){
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(interfaceType));
        return scanner.findCandidateComponents(basePackage);
    }
}
