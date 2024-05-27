import java.util.ArrayList;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        BlockingQueue blockingQueue = new BlockingQueue();
        Thread workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    Runnable task = blockingQueue.get();
                    task.run();
                }
            }
        });
        workerThread.start();

        for (int i = 0; i < 15; i++) {
            Task task = new Task();
            blockingQueue.put(task);
            Task task1 = new Task();
            blockingQueue.put(task1);
            JudgeTask judgeTask = new JudgeTask();
            blockingQueue.put(judgeTask);
        }

    }

    public static class Timer implements Runnable{

        @Override
        public void run() {
            int counter = 0;
            while (counter< 5){
                System.out.println(new Date());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                counter++;
            }
            System.out.println("Таймер закончил работу");
        }
    }

    public static class JudgeTask implements Runnable{
        @Override
        public void run() {
            System.out.println("Начал cудить");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Закончил судить");
        }
    }

    public static class Task implements Runnable{
        @Override
        public void run() {
            System.out.println("Начал работу");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Работу закончил");
        }
    }

    /**
     * Класс реализующий механизм блокирующей очереди
     */

    public  static class BlockingQueue {
        public ArrayList<Runnable> tasks = new ArrayList<>();

        public synchronized Runnable get(){
            while (tasks.isEmpty()){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Runnable runnable = tasks.get(0);
            tasks.remove(0);
            return runnable;
        }

        public synchronized void put(Runnable task){
            tasks.add(task);
            notify();
        }
    }
}
