package be.yelido.camunda.module.data.request;

import be.yelido.camunda.module.data.dto.Variable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class CamundaInstruction {
    public static final String START_BEFORE_ACTIVITY = "startBeforeActivity";
    public static final String START_AFTER_ACTIVITY = "startAfterActivity";

    private String type;
    private String activityId;
    HashMap<String, Variable> variables;
}
