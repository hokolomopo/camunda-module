package be.yelido.camunda.module.data.dto;

import lombok.Data;

@Data
public class Incident {
    private String id;
    private String processDefinitionId;
    private String processInstanceId;
    private String executionId;
    private String incidentTimestamp;
    private String incidentType;
    private String activityId;
    private String causeIncidentId;
    private String rootCauseIncidentId;
    private String configuration;
    private Object tenantId;
    private String incidentMessage;
    private String jobDefinitionId;
}
