import be.yelido.camunda.module.util.CamundaMonitoringRunnable;
import be.yelido.camunda.module.util.CamundaThreadingService;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadingTest {
    private void event(String key){
        System.out.println(String.format("Event for Key : %s", key));
    }

    @Test
    @Ignore
    public void testThreading() throws Exception{
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CamundaThreadingService camundaThreadingService = new CamundaThreadingService();

        ArrayList<String> keyArray = new ArrayList<>();
        keyArray.add("a");
        keyArray.add("b");
        keyArray.add("c");

        camundaThreadingService.addEvent("c", () -> event("c"));
//        Runnable producer = () -> {
//            for(int i =0;i >=0;i++){
//                String key = keyArray.get(i % 2);
//                System.out.println("Producer add event to queue " + key);
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                camundaThreadingService.addEvent(key, () -> event(key));
//            }
//        };
//
//
//        executorService.submit(producer);

        for(int i =0;i <=30;i++){
            String key = keyArray.get(i % 3);
            //System.out.println("Producer add event to queue " + key);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            camundaThreadingService.addEvent(key, () -> event(key));
        }

        for(int i =0;i < 2;i++){
            CamundaMonitoringRunnable thread = new CamundaMonitoringRunnable(camundaThreadingService);
            executorService.submit(thread);
        }

        for(int i =0;i >=0;i++){
            String key = keyArray.get(i % 3);
            //System.out.println("Producer add event to queue " + key);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            camundaThreadingService.addEvent(key, () -> event(key));
        }
        Thread.sleep(30000);
    }
}
