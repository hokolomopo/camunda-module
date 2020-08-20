package be.yelido.camunda.module.rest;

import be.yelido.camunda.module.data.dto.Execution;
import be.yelido.camunda.module.data.dto.Incident;
import be.yelido.camunda.module.data.dto.ProcessDefinition;
import be.yelido.camunda.module.data.dto.ProcessInstance;
import be.yelido.camunda.module.data.request.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CamundaRestTemplate {
    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper mapper = new ObjectMapper();
    private final TypeFactory factory  = TypeFactory.defaultInstance();

    /**
     *
     * @param url the url of the REST service of the camunda server
     */
    public CamundaRestTemplate(String url) {
        DefaultUriTemplateHandler defaultUriTemplateHandler = new DefaultUriTemplateHandler();
        defaultUriTemplateHandler.setBaseUrl(url);

        restTemplate.setUriTemplateHandler(defaultUriTemplateHandler);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private <T> T getHTTP(String url, Class<T> responseType){
        return getHTTP(url, responseType, new HashMap<String, String>());
    }

    private <T> T getHTTP(String url, Class<T> responseType, Map<String, String> queryParameters){

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(url);
        for(Map.Entry<String, String> entry : queryParameters.entrySet())
            builder.queryParam(entry.getKey(), entry.getValue());

        return restTemplate.getForObject(builder.toUriString(), responseType);
    }

    private <T> T postHTTP(String url, Object requestObject, Class<T> responseType){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<Object>(requestObject, headers);

        return restTemplate.postForObject(url, request, responseType);
    }

    private Map<String, String> convertToMap(Object o){
        MapType type = factory.constructMapType(HashMap.class, String.class, String.class);
        return mapper.convertValue(o, type);
    }

    /**
     * Get all the process definitions deployed in the Camundna Server
     *
     * @return the process definitions
     */
    public List<ProcessDefinition> getProcessDefinitions(){
        String url = "/process-definition";

        ProcessDefinition[] resp = getHTTP(url, ProcessDefinition[].class);

        return Arrays.asList(resp);
    }

    /**
     * Start a process instance from a process id
     *
     * @param processDefinitionId the id
     * @param parameters additional parameters for starting the instance
     * @return The process instance started
     */
    public ProcessInstance startProcessInstanceById(String processDefinitionId, StartProcessInstanceParameters parameters){
        String url = String.format("/process-definition/%s/start", processDefinitionId);
        return postHTTP(url, parameters, ProcessInstance.class);
    }

    /**
     * Start a process instance from a process key
     *
     * @param processKey the key
     * @param parameters additional parameters for starting the instance
     * @return The process instance started
     */
    public ProcessInstance startProcessInstanceByKey(String processKey, StartProcessInstanceParameters parameters){
        String url = String.format("/process-definition/key/%s/start", processKey);
        return postHTTP(url, parameters, ProcessInstance.class);
    }

    /**
     * Get a process instance from its id
     *
     * @param id the id
     * @return the process instance
     */
    public ProcessInstance getProcessInstance(String id){
        String url = String.format("/process-instance/%s", id);

        return getHTTP(url, ProcessInstance.class);
    }

    /**
     * Search for a process instance
     *
     * @param query the parameters of the process instance
     * @return the process instance
     */
    public List<ProcessInstance> searchProcessInstances(ProcessInstanceQuery query){
        String url = "/process-instance";
        Map<String, String> queryParameters = convertToMap(query);

        return Arrays.asList(getHTTP(url, ProcessInstance[].class, queryParameters));
    }

    /**
     * Search for a process instance
     *
     * @param query the parameters of the process instance
     * @return the process instance
     */
    public ProcessInstance searchProcessInstancesSingleResult(ProcessInstanceQuery query){
        String url = "/process-instance";
        Map<String, String> queryParameters = convertToMap(query);

        ProcessInstance[] resp = getHTTP(url, ProcessInstance[].class, queryParameters);
        if (resp.length > 1)
            throw new IllegalStateException("Query parameters led to multiple resulting process ids from the Camunda server");
        else if (resp.length == 0)
            return null;
        return resp[0];
    }

    /**
     * Correlate a message using the Camunda Engine
     *
     * @param parameters the parameters of the message
     */
    public void sendMessage(MessageCorrelationParameters parameters){
        String url = "/message";
        postHTTP(url, parameters, Object.class);
    }

    public List<Execution> getExecution(ExecutionQuery query){
        String url = "/execution";

        Map<String, String> queryParameters = convertToMap(query);

        Execution[] executions = getHTTP(url, Execution[].class, queryParameters);

        return Arrays.asList(executions);
    }

    /**
     * Create an incident in the Camunda Engine
     *
     * @param executionId the ID of the execution in which to create an incident
     * @param parameters the parameters
     * @return the Indicent created
     */
    public Incident createIncident(String executionId, CreateIncidentParameters parameters){
        String url = String.format("/execution/%s/create-incident", executionId);
        return postHTTP(url, parameters, Incident.class);
    }

    /**
     * Send a signal to the Camunda Engine
     *
     * @param parameters the parameters
     */
    public void sendSignal(SignalParameters parameters){
        String url = "/signal";
        postHTTP(url, parameters, Void.class);
    }

}
