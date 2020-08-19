package be.yelido.camunda.module;

import be.yelido.camunda.module.data.dto.ProcessInstance;
import be.yelido.camunda.module.data.dto.Variable;
import be.yelido.camunda.module.data.request.CamundaType;
import be.yelido.camunda.module.data.request.StartProcessInstanceParameters;
import be.yelido.camunda.module.rest.CamundaRestTemplate;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        CamundaRestTemplate restTemplate = new CamundaRestTemplate("http://localhost:8083/rest");

        HashMap<String, Variable> variables = new HashMap<String, Variable>();
        variables.put("nbrOfInstances", new Variable(3, CamundaType.INT));
        StartProcessInstanceParameters parameters = new StartProcessInstanceParameters(null, variables);

        ProcessInstance p = restTemplate.startProcessInstanceByKey("testMultiInstanceSubrocess", parameters);
        System.out.println(p.getId());
    }
}
