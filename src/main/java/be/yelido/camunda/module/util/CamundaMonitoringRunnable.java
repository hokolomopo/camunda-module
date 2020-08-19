package be.yelido.camunda.module.util;

import javafx.util.Pair;

import java.util.Queue;

public class CamundaMonitoringRunnable implements Runnable {

    private CamundaThreadingService camundaThreadingService;

    public CamundaMonitoringRunnable(CamundaThreadingService camundaThreadingService) {
        this.camundaThreadingService = camundaThreadingService;
    }

    @Override
    public void run() {
            while (true){
                try {
                    String key = camundaThreadingService.getQueueKey();
                    Queue<Runnable> queue = camundaThreadingService.getQueue(key);
//                    Pair<String, Queue<Runnable>> pair = camundaThreadingService.getQueue();
//                    String key = pair.getKey();
//                    Queue<Runnable> queue = pair.getValue();
//                    Thread.sleep(2);

                    while(!queue.isEmpty())
                        queue.poll().run();

                    camundaThreadingService.removeQueue(key);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }

}
