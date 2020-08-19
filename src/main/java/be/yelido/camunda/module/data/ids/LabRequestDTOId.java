package be.yelido.camunda.module.data.ids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabRequestDTOId extends CamundaObjectIdentifier{
    private String id;
    private String label;
    private String number;
}
