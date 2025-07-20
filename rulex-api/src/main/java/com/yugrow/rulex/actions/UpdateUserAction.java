package com.yugrow.rulex.actions;

import com.yugrow.rulex.action.Action;
import com.yugrow.rulex.context.EvaluationContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class UpdateUserAction implements Action {

    private String type = "update_user_status";
    private String status;

    // TODO: Inject @Autowired UserService

    @Override
    public void execute(EvaluationContext context) {
        System.out.printf("Updating user status to :%s%n", status);
        System.out.println("User status updated successfully");
    }

    @Override
    public String getType() {
        return type;
    }
}
