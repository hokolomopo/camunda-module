package be.yelido.camunda.module.data.request;

import be.yelido.camunda.module.data.dto.Variable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

@AllArgsConstructor
@Data
public class StartProcessInstanceParameters {
    String businessKey;
    HashMap<String, Variable> variables;

    ArrayList<CamundaInstruction> startInstructions;

    public StartProcessInstanceParameters(String businessKey, HashMap<String, Variable> variables) {
        this.businessKey = businessKey;
        this.variables = variables;
    }
}
