# Rulex

Rulex is a modular, extensible rule engine built with Java 21 and Spring Boot 3.4.0. It allows you to define, evaluate, and execute business rules dynamically using a flexible API and a robust core engine.

---

## Table of Contents
- [Architecture Overview](#architecture-overview)
- [Key Interfaces](#key-interfaces)
- [Design Patterns](#design-patterns)
- [Modules](#modules)
- [How to Run](#how-to-run)
- [API Usage](#api-usage)
- [Extending Rulex](#extending-rulex)
- [Contributing](#contributing)

---

## Architecture Overview

Rulex is organized into three main Maven modules:

- **rulex-core**: Contains the core engine, domain models, interfaces, and rule parsing logic.
- **rulex-api**: Exposes RESTful endpoints for rule evaluation and management.
- **rulex-starter**: The Spring Boot application entry point for running the service.

---

## UML Diagram

![Rulex Core UML](rulex-core_uml.png)

---

## Design Patterns

Rulex leverages several classic design patterns to ensure extensibility, maintainability, and clarity:

- **Strategy Pattern:**
  - Used for `Action` and `Condition` interfaces, allowing different implementations to be injected and executed dynamically.
- **Composite Pattern:**
  - Conditions can be composed (e.g., AND, OR, NOT) to form complex logical trees.
- **Factory/Registry Pattern:**
  - The `ParserRegistryConfiguration` dynamically registers and resolves action and condition types.
- **Dependency Injection:**
  - Spring Boot is used for managing dependencies and wiring components.
- **Polymorphic Deserialization:**
  - Jackson annotations allow dynamic (de)serialization of actions and conditions based on their type.

---

## Modules

- **rulex-core**
  - Core interfaces: `Action`, `Condition`, `EvaluationContext`, etc.
  - Domain models: `Event`, `RuleDefinition`, `RuleExecutionResult`.
  - Engine: Executes rules and actions.
  - Parser: Parses rule definitions from JSON.

- **rulex-api**
  - REST API for rule evaluation.
  - Payload and DTO classes.
  - Service layer for orchestrating rule evaluation.

- **rulex-starter**
  - Spring Boot application entry point.

---

## How to Run

### Prerequisites
- Java 21+
- Maven 3.8+

### Build the Project
```sh
mvn clean install
```

### Run the Application
From the project root:
```sh
cd rulex-starter
mvn spring-boot:run
```
The API will be available at `http://localhost:8080`.

---

## API Usage

### Evaluate a Rule
**Endpoint:**  
`POST /rule`

**Request Body Example:**
```json
{
  "ruleJson": "{ \"conditions\": { \"type\": \"event_occurred\", \"eventName\": \"login\" }, \"actions\": [ { \"type\": \"send_email\", \"template\": \"welcome\" } ] }",
  "events": [
    {
      "name": "login",
      "timestamp": "2024-06-01T12:00:00Z",
      "userid": "user123",
      "properties": {}
    }
  ],
  "userAttributes": {
    "email": "user@example.com"
  }
}
```

**Response:**
```json
{
  "conditionMatched": true,
  "actionsExecuted": ["send_email"],
  "actionsNotExecuted": []
}
```

---

## Extending Rulex

### Add a New Action
1. Implement the `Action` interface.
2. Annotate with `@Component`.
3. Register the new action type in your JSON payloads.

### Add a New Condition
1. Implement the `Condition` interface.
2. Annotate with `@Component`.
3. Register the new condition type in your JSON payloads.

#### Example
```java
@Component
public class MyCustomAction implements Action {
    private String type = "my_custom_action";
    public void execute(EvaluationContext context) {
        // Custom logic
    }
    public String getType() { return type; }
}
```

---

## Contributing

1. Fork the repository.
2. Create a feature branch.
3. Write tests for your feature or bugfix.
4. Submit a pull request.

---

## Testing

- **Unit Tests:**
  - Located in `rulex-api/src/test/java/com/yugrow/rulex/`
  - Cover core logic, conditions, actions, and service classes.

- **Integration Tests:**
  - Located in `rulex-starter/src/test/java/com/yugrow/rulex/`
  - Cover API endpoints and full application context scenarios.

Run all tests with:
```sh
mvn test
```

---

## Key Interfaces

Below is a table explaining the purpose and role of each interface in the `rulex-core` module, along with example snippets and typical implementors:

| Interface Name             | Description & Responsibility                                                                | Main Method(s) / Signature(s)                                  | Example Implementation / Usage                                                                                 |
|----------------------------|---------------------------------------------------------------------------------------------|----------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| **TypeIdentifiable**       | Provides a way for objects to expose their type as a string, useful for polymorphic serialization and type resolution. | `String getType()`                                             | All `Action` and `Condition` implementations (e.g., `SendEmailAction`, `AttributeMatchCondition`)             |
| **AttributeAware**         | For classes that operate on a specific attribute/field, such as conditions that match on user attributes. | `String getField()`                                            | `AttributeMatchCondition` (matches a user attribute against a value)                                          |
| **TimeBoundAware**         | For classes that need to operate within a time window, such as time-based conditions.       | `Duration getTimeWindow()`                                     | `EventCountCondition`, `EventOccurredCondition`, `NotOccurredCondition`, `SequenceCondition`                  |
| **ConditionTypes**         | Holds string constants for various condition types, ensuring type safety and consistency.   | (Constants only)                                               | Used in all condition implementations to set the `type` field (e.g., `ConditionTypes.ATTRIBUTE_MATCH`)        |
| **Condition**              | Core interface for all rule conditions. Evaluates a condition against an `EvaluationContext`. Extends `TypeIdentifiable`. | `boolean evaluate(EvaluationContext context)`                  | `AttributeMatchCondition`, `EventCountCondition`, `AndCondition`, `OrCondition`, `NotCondition`, etc.         |
| **Action**                 | Core interface for all actions to be executed when a rule matches. Extends `TypeIdentifiable`. | `void execute(EvaluationContext context)`                      | `SendEmailAction`, `ApplyBonusAction`, `UpdateUserAction`                                                     |
| **EvaluationContext**      | Provides access to events and attributes for rule evaluation. Methods for querying events and attributes in various ways. | `Object getAttribute(String field)`<br>`List<Event> getAllEventsByName(String name)`<br>...         | `DefaultEvaluationContext`                                                                                    |
| **RuleDefinitionExecutor** | Declares the contract for executing a rule definition and returning the result.             | `RuleExecutionResult execute(RuleDefinition, EvaluationContext)`| `DefaultRuleDefinitionExecutor`                                                                               |
| **ActionExecutor**         | Executes a list of actions and returns execution results.                                   | `Map<String, List<String>> executeAll(List<Action>, EvaluationContext)` | `DefaultActionExecutor`                                                                                       |
| **RuleDefinitionParser**   | Parses a rule definition from a JSON string.                                                | `RuleDefinition parseRule(String ruleJson)`                    | `DefaultRuleDefinitionParser`                                                                                 |


For more details on architecture, design patterns, and how to run the project, see the earlier sections of this README. 