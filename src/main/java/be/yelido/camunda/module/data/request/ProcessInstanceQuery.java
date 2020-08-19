package be.yelido.camunda.module.data.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName  = "createQuery")
public class ProcessInstanceQuery {
    private String processInstanceIds;
    private String businessKey;
    private String superProcessInstance;
    private String processDefinitionKey;
    private boolean active;
}
