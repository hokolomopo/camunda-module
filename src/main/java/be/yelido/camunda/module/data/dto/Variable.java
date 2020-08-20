package be.yelido.camunda.module.data.dto;

import be.yelido.camunda.module.data.request.CamundaType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Variable {
    Object value;
    String type;

    VariableValueInfo valueInfo;

    public Variable(Object value, String type) {
        this.value = value;
        this.type = type;
    }

    public Variable(String value){
        this.value = value;
        this.type = CamundaType.STRING;
    }

    public Variable(int value){
        this.value = value;
        this.type = CamundaType.INT;
    }

    public Variable(boolean value){
        this.value = value;
        this.type = CamundaType.BOOLEAN;
    }

}
