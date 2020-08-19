package be.yelido.camunda.module.data.dto;

import lombok.Data;

@Data
public class Execution {
    private String id;
    private String processInstanceId;
    private boolean ended;
    private String tenantId;
}
