package be.yelido.camunda.module.data.request;

import be.yelido.camunda.module.data.dto.Variable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class SignalParameters {
    private String name;
    private String executionId;

    private Map<String, Variable> variables;

    public SignalParameters(String signalName, Map<String, Variable> processVariables) {
        this.name = signalName;
        this.variables = processVariables;
    }

    public SignalParameters(String signalName) {
        this.name = signalName;
    }
}
