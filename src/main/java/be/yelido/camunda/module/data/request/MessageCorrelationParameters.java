package be.yelido.camunda.module.data.request;

import be.yelido.camunda.module.data.dto.Variable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class MessageCorrelationParameters {
    private String messageName;
    private String businessKey;
    private String processInstanceId;

    private Map<String, Variable> correlationKeys;
    private Map<String, Variable> processVariables;

    public MessageCorrelationParameters(String messageName, String processInstanceId, Map<String, Variable> processVariables) {
        this.messageName = messageName;
        this.processInstanceId = processInstanceId;
        this.processVariables = processVariables;
    }

    public MessageCorrelationParameters(String messageName, Map<String, Variable> processVariables) {
        this.messageName = messageName;
        this.processVariables = processVariables;
    }
}
