package be.yelido.camunda.module.data.ids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccinationDTOId extends CamundaObjectIdentifier{
    private String rrnr;
    private String cnkCode;
    private String vaccineName;
}
