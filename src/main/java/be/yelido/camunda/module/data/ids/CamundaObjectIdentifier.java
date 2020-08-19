package be.yelido.camunda.module.data.ids;

import be.yelido.camunda.module.data.dto.Variable;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.util.*;

public abstract class CamundaObjectIdentifier {

    public CamundaObjectIdentifier() {}

    public Map<String, Variable> toVariableMap(Map<String, String> stringMap)  {

        Map<String, Variable> varMap = new HashMap<>();
        for(Map.Entry<String, String> e : stringMap.entrySet())
            varMap.put(e.getKey(), new Variable(e.getValue()));
        return varMap;
    }

    public List<String> getAllVariablesNames(ObjectMapper objectMapper){
        Map<String, String> stringMap = objectMapper.convertValue(this, Map.class);
        return new ArrayList<>(stringMap.keySet());
    }
}
