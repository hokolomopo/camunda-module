package be.yelido.camunda.module.data.ids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditableT1Id extends CamundaObjectIdentifier{
    private String uuidT1;
    private String rrnr;
    private String sisnumber;
    private String nameT1;
    private String wnnumber;
}
