package be.yelido.camunda.module.data.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProcessInstance {
    //TODO only keep minimum info?
    private List<Link> links = null;
    private String id;
    private String definitionId;
    private String businessKey;
    private String caseInstanceId;
    private Boolean ended;
    private Boolean suspended;
    private String tenantId;
}
