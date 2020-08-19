package be.yelido.camunda.module.util;

import be.yelido.camunda.module.data.dto.Variable;
import be.yelido.camunda.module.data.request.CamundaType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class CamundaService {
    private CamundaThreadingService camundaThreadingService;

    public CamundaService(int numberOfThreads) {
        camundaThreadingService = new CamundaThreadingService();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        for(int i = 0;i < numberOfThreads;i++)
            executorService.submit(new CamundaMonitoringRunnable(camundaThreadingService));
    }

    public abstract void doCorrelateMessage(CamundaToken token, String msgName, Map<String, Variable> variableMap);

    public abstract void doStartProcess(String processKey, CamundaToken token, HashMap<String, Variable> variables);

    public abstract void doRaiseIncident(CamundaToken token, String activityMessageName, String message);

    public void correlateMessage(CamundaToken token, String msgName, Map<String, Variable> variableMap) {
        String key = getKey(token);
        camundaThreadingService.addEvent(key, () -> doCorrelateMessage(token, msgName, variableMap));
    }

    public void startProcess(String processKey, CamundaToken token, HashMap<String, Variable> variables) {
        String key = getKey(token);
        camundaThreadingService.addEvent(key, () -> doStartProcess(processKey, token, variables));
    }

    public void raiseIncident(CamundaToken token, String activityMessageName, String message) {
        String key = getKey(token);
        camundaThreadingService.addEvent(key, () -> doRaiseIncident(token, activityMessageName, message));
    }

    public void correlateMessage(CamundaToken token, String msgName, String variableName, String variable){
        Map<String, Variable> camundaVariables = VariableUtil.mapFromSingleVariable(variableName,
                new Variable(variable));
        this.correlateMessage(token, msgName, camundaVariables);
    }

    public void correlateMessage(CamundaToken token, String msgName, String variableName, int variable){
        Map<String, Variable> camundaVariables = VariableUtil.mapFromSingleVariable(variableName,
                new Variable(variable, CamundaType.INT));
        this.correlateMessage(token, msgName, camundaVariables);
    }

    public void correlateMessage(CamundaToken token, String msgName, String variableName, boolean variable){
        Map<String, Variable> camundaVariables = VariableUtil.mapFromSingleVariable(variableName,
                new Variable(variable, CamundaType.BOOLEAN));
        this.correlateMessage(token, msgName, camundaVariables);
    }

    public void correlateMessage(CamundaToken token, String msgName, String variableName, Collection variable){
        Map<String, Variable> camundaVariables = VariableUtil.mapFromSingleVariable(variableName,
                VariableUtil.createVariableFromCollection((variable)));
        this.correlateMessage(token, msgName, camundaVariables);
    }

    public boolean areJobsEnded(){
        return camundaThreadingService.areQueuesEmpty();
    }

    private String getKey(CamundaToken token){
        if(token == null || token.getRootProcessBK() == null)
            return "null";
        return token.getRootProcessBK();
    }
}
