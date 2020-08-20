import be.yelido.camunda.module.data.dto.Execution;
import be.yelido.camunda.module.data.dto.Incident;
import be.yelido.camunda.module.data.dto.ProcessInstance;
import be.yelido.camunda.module.data.ids.AuditableT0Id;
import be.yelido.camunda.module.data.request.*;
import be.yelido.camunda.module.data.dto.Variable;
import be.yelido.camunda.module.util.CamundaToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.Ignore;
import org.junit.Test;
import be.yelido.camunda.module.rest.CamundaRestTemplate;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@Ignore
public class MyTests {
    public static final String url = "http://localhost:8083/rest";

    public static final String testProcessKey = "TestMessageMonitoring";
    public static final String testMessageName = "continue";


    @Test
    public void getProcessDefinitons() {

        CamundaRestTemplate restTemplate = new CamundaRestTemplate(url);
        restTemplate.getProcessDefinitions();
    }

    @Test
    public void createProcessInstance() {

        CamundaRestTemplate restTemplate = new CamundaRestTemplate(url);

        // Create Process Instance
        ProcessInstance p = restTemplate.startProcessInstanceByKey(testProcessKey, null);


        assertNotNull(p);
    }

    @Test
    public void createProcessInstanceWithVariables() {

        CamundaRestTemplate restTemplate = new CamundaRestTemplate(url);

        HashMap<String, Variable> variables = new HashMap<String, Variable>();
        variables.put("TestVar", new Variable("This is a test variable", CamundaType.STRING));
        variables.put("TestVar2", new Variable(3, CamundaType.INT));

        StartProcessInstanceParameters parameters = new StartProcessInstanceParameters(null, variables);

        ProcessInstance p = restTemplate.startProcessInstanceByKey(testProcessKey, parameters);

        assertNotNull(p);
    }

    @Test
    public void getProcessInstance() {

        CamundaRestTemplate restTemplate = new CamundaRestTemplate(url);

        // Create Process Instance
        ProcessInstance p = restTemplate.startProcessInstanceByKey(testProcessKey, null);

        assertNotNull(p);

        ProcessInstance r = restTemplate.getProcessInstance(p.getId());
        assertEquals(p.getId(), r.getId());
        assertEquals(p.getDefinitionId(), r.getDefinitionId());
        assertEquals(p.getBusinessKey(), r.getBusinessKey());
        assertEquals(p.getCaseInstanceId(), r.getCaseInstanceId());
    }

    @Test
    public void searchProcessInstance() {

        CamundaRestTemplate restTemplate = new CamundaRestTemplate(url);

        // Create Process Instance
        StartProcessInstanceParameters parameters = new StartProcessInstanceParameters("testBK", null);
        ProcessInstance p = restTemplate.startProcessInstanceByKey(testProcessKey, parameters);

        assertNotNull(p);

        ProcessInstanceQuery query = ProcessInstanceQuery.createQuery().active(true).processInstanceIds(p.getId()).businessKey(p.getBusinessKey()).build();
        List<ProcessInstance> rs = restTemplate.searchProcessInstances(query);
        assertEquals(1, rs.size());

        ProcessInstance r = rs.get(0);
        assertEquals(p.getId(), r.getId());
        assertEquals(p.getDefinitionId(), r.getDefinitionId());
        assertEquals(p.getBusinessKey(), r.getBusinessKey());
        assertEquals(p.getCaseInstanceId(), r.getCaseInstanceId());
    }




    @Test
    public void executionQuery() {
        CamundaRestTemplate restTemplate = new CamundaRestTemplate(url);

        // Create Process Instance
        ProcessInstance p = restTemplate.startProcessInstanceByKey(testProcessKey, null);

        assertNotNull(p);

        ExecutionQuery query = ExecutionQuery.createQuery()
                .processInstanceId(p.getId())
                .messageEventSubscriptionName(testMessageName)
                .build();


        List<Execution> executions = restTemplate.getExecution(query);

        assertEquals(1, executions.size());
        assertEquals(p.getId(), executions.get(0).getProcessInstanceId());
    }

    @Test
    public void sendMessage() {
        CamundaRestTemplate restTemplate = new CamundaRestTemplate(url);

        // Create Process Instance
        ProcessInstance p = restTemplate.startProcessInstanceByKey(testProcessKey, null);
        assertNotNull(p);

        // Send message
        HashMap<String, Variable> variables = new HashMap<String, Variable>();
        variables.put("MessageVar", new Variable("This variables was send along with a message", CamundaType.STRING));
        MessageCorrelationParameters parameters = new MessageCorrelationParameters(testMessageName, p.getId(), variables);
        restTemplate.sendMessage(parameters);

        // Check if the process have moved
        ExecutionQuery query = ExecutionQuery.createQuery()
                .processInstanceId(p.getId())
                .activityId("Wait")
                .build();

        List<Execution> executions = restTemplate.getExecution(query);

        assertEquals(1, executions.size());
        assertEquals(p.getId(), executions.get(0).getProcessInstanceId());
    }


    @Test
    public void createIncident() {
        CamundaRestTemplate restTemplate = new CamundaRestTemplate(url);

        // Create Process Instance
        ProcessInstance p = restTemplate.startProcessInstanceByKey(testProcessKey, null);

        assertNotNull(p);

        // Get execution
        ExecutionQuery query = ExecutionQuery.createQuery()
                .processInstanceId(p.getId())
                .messageEventSubscriptionName(testMessageName)
                .build();

        List<Execution> executions = restTemplate.getExecution(query);

        assertEquals(1, executions.size());
        Execution e = executions.get(0);

        CreateIncidentParameters parameters = new CreateIncidentParameters("TestType", null, "This is an incident.");
        Incident incident = restTemplate.createIncident(e.getId(), parameters);

        assertNotNull(incident);
        assertEquals(e.getId(), incident.getExecutionId());

    }

}
