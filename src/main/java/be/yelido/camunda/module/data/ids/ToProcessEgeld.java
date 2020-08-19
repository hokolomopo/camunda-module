package be.yelido.camunda.module.data.ids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToProcessEgeld extends CamundaObjectIdentifier{
    private String rrnr;
    private String userId;
}
