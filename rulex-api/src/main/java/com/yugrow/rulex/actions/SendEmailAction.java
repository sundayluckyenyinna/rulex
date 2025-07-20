package com.yugrow.rulex.actions;

import com.yugrow.rulex.action.Action;
import com.yugrow.rulex.context.EvaluationContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class SendEmailAction implements Action {

    private String type = "send_email";  // TODO: Centralize this in a class and inject it
    private String template;

    @Override
    public void execute(EvaluationContext context) {
        System.out.printf("Sending email to user with template: %s%n ....", template);
        System.out.println("Email sent to user successfully");
    }

    @Override
    public String getType() {
        return type;
    }
}
