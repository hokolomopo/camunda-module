package be.yelido.camunda.module.data.ids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditableT0Id extends CamundaObjectIdentifier{
    private String uuidT0;
    private String userId;
    private String wgnr;
    private String nameT0;
}
