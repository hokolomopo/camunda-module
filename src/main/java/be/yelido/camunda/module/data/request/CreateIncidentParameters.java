package be.yelido.camunda.module.data.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateIncidentParameters {
    private String incidentType;
    private String configuration;
    private String message;
}
