package be.yelido.camunda.module.util;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class CamundaThreadingService {
    private final Object lock = new Object();
    private final Object lock2 = new Object();

    private final BlockingQueue<String> availableQueues = new LinkedBlockingQueue<>();
    private final Map<String, Queue<Runnable>> eventQueuesMap = new ConcurrentHashMap<>();

     public String getQueueKey() throws InterruptedException {
         String key = availableQueues.take();
         return key;
    }

    public Queue<Runnable> getQueue(String key){
            return eventQueuesMap.get(key);
    }

//    public Pair<String, Queue<Runnable>> getQueue() throws InterruptedException {
//         String key = availableQueues.take();
//         Queue<Runnable> queue = eventQueuesMap.get(key);
//         return new Pair<>(key, queue);
//    }

    public void addEvent(String key, Runnable event)  {
//        System.out.println("AddEvent " + key);
        synchronized (lock) {
            Queue<Runnable> queue = eventQueuesMap.get(key);
            if (queue == null) {
                queue = new LinkedBlockingQueue<>();
                eventQueuesMap.put(key, queue);
                queue.add(event);
                availableQueues.offer(key);
            }
            else {
                queue.add(event);
            }
        }
    }

    public void removeQueue(String key)  {
        synchronized (lock){
            if(eventQueuesMap.get(key).isEmpty())
                eventQueuesMap.remove(key);
            else {
                availableQueues.offer(key);
            }
        }
    }

    public boolean areQueuesEmpty() {
        synchronized (lock) {
            return eventQueuesMap.isEmpty();
        }
    }


}
