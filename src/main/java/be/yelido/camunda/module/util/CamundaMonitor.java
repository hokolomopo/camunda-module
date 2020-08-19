package be.yelido.camunda.module.util;

import be.yelido.camunda.module.data.dto.Execution;
import be.yelido.camunda.module.data.dto.ProcessInstance;
import be.yelido.camunda.module.data.dto.Variable;
import be.yelido.camunda.module.data.exceptions.CamundaSearchFailedException;
import be.yelido.camunda.module.data.request.*;
import be.yelido.camunda.module.rest.CamundaRestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CamundaMonitor {
    private final CamundaRestTemplate restTemplate;

    /**
     * @param url the url of the Server Camunda
     */
    public CamundaMonitor(String url){
        restTemplate = new CamundaRestTemplate(url);
    }


    /**
     * Correlate a message with the Camunda engine.
     *
     * @param token a token containing information to identify the process instance in Camunda. Can be null if the
     *              information are in parameters
     * @param parameters the parameters for the correlations of the message
     */
    public void correlateMessage(CamundaToken token, MessageCorrelationParameters parameters){

        if(token == null){
            restTemplate.sendMessage(parameters);
            return;
        }

        ProcessInstanceQuery.ProcessInstanceQueryBuilder builder = ProcessInstanceQuery.createQuery();
        if(token.getCurrentProcessBK() != null){
            builder.businessKey(token.getCurrentProcessBK());
        }
        if(token.getProcessDefinitionKey() != null){
            builder.processDefinitionKey(token.getProcessDefinitionKey());
        }

        try {
            ProcessInstance processInstance = restTemplate.searchProcessInstancesSingleResult(builder.build());
            parameters.setProcessInstanceId(processInstance.getId());
            restTemplate.sendMessage(parameters);
        }catch(NullPointerException e){
            throw new NullPointerException("Camunda process instance with couldn't be found from token " + token.toString());
        }
    }


    /**
     * Correlate a message with the Camunda engine.
     *
     * @param token a token containing information to identify the process instance in Camunda. Can be null if the
     *              information are in parameters
     * @param msgName the name of the message
     * @param variableMap the variables to send to the process instance
     */
    public void correlateMessage(CamundaToken token, String msgName, Map<String, Variable> variableMap) {
        MessageCorrelationParameters parameters = new MessageCorrelationParameters(msgName, null, variableMap);
        correlateMessage(token, parameters);
    }

    /**
     * Correlate a message with the Camunda engine. Take only a messageName to correlate the message,
     *  the message must thus be unique.
     *
     * @param msgName the name of the message
     * @param variableMap the variables to send to the process instance
     */
    public void correlateMessage(String msgName, Map<String, Variable> variableMap) {
        MessageCorrelationParameters parameters = new MessageCorrelationParameters(msgName, null, variableMap);
        correlateMessage(null, parameters);
    }

    /**
     * Start a Camunda process instance
     *
     * @param processKey the key of the BPMN process
     * @param token a token with the BK of the process
     * @param variables the variables of the process
     */
    public void startProcess(String processKey, CamundaToken token, HashMap<String, Variable> variables){
        StartProcessInstanceParameters p = new StartProcessInstanceParameters(token.getCurrentProcessBK(), variables);
        restTemplate.startProcessInstanceByKey(processKey, p);
    }

    public void raiseIncident(CamundaToken token, String incidentMsgName, String message){
        ExecutionQuery executionQuery = ExecutionQuery.createQuery()
                .businessKey(token.getCurrentProcessBK())
                .messageEventSubscriptionName(incidentMsgName)
                .build();
        List<Execution> executions = restTemplate.getExecution(executionQuery);

        if(executions.size() != 1)
            throw new CamundaSearchFailedException("Error when searching for Camunda execution to raise an incident." +
                    " Expected to find 1 execution for these parameters, found " + executions.size());

        CreateIncidentParameters incidentParameters = new CreateIncidentParameters("Error raised", null, message);
        restTemplate.createIncident(executions.get(0).getId(), incidentParameters);
    }

    /**
     * Send a signal to Cmaunda
     *
     * @param signalName the name of the signal
     * @param variables the variables of the process
     */
    public void sendSignal(String signalName, HashMap<String, Variable> variables){
        SignalParameters signalParameters = new SignalParameters(signalName, variables);
        restTemplate.sendSignal(signalParameters);
    }


    public CamundaRestTemplate getRestTemplate() {
        return restTemplate;
    }

}
