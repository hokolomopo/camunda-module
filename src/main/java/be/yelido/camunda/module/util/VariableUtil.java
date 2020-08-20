package be.yelido.camunda.module.util;

import be.yelido.camunda.module.data.dto.Variable;
import be.yelido.camunda.module.data.dto.VariableValueInfo;
import be.yelido.camunda.module.data.request.CamundaType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.HashMap;

public class VariableUtil {
    public static Variable createVariableFromCollection(Collection c){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return new Variable(
                    mapper.writeValueAsString(c), CamundaType.OBJECT,
                    new VariableValueInfo("java.util.Collection", VariableValueInfo.JSON));
        } catch (JsonProcessingException ignored) {
            return null;
        }
    }

    public static HashMap<String, Variable> mapFromSingleVariable(String name, Variable var){
        HashMap<String, Variable> map = new HashMap<String, Variable>();
        map.put(name, var);
        return map;
    }

}
