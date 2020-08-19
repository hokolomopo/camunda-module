package be.yelido.camunda.module.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VariableValueInfo {
    public final static String JSON = "application/json";
    public final static String XML = "application/XML";

    private String objectTypeName;
    private String serializationDataFormat;
}
