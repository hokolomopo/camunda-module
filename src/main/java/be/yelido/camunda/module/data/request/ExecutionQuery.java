package be.yelido.camunda.module.data.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName  = "createQuery")
public class ExecutionQuery {
    private String businessKey;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String processInstanceId;
    private String activityId;
    private String signalEventSubscriptionName;
    private String messageEventSubscriptionName;
    private Boolean suspended;
    private Boolean active;
    private String incidentId;
    private Integer maxResults;
}
