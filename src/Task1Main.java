import java.util.*;
import java.util.concurrent.Semaphore;

public class Task1Main {
    public static List<Task1Task> readyQueue;
    public static Semaphore cpuSem = new Semaphore(0);
    public static Semaphore dispatcherSem = new Semaphore(0);
    public static int algorithmNumber;
    public static int timeQuantum;
    public static int T;
    public static Random rand = new Random();
    public static Semaphore taskSendingSem = new Semaphore(0);
    public static boolean allTaskGenerated = false;

    public static void task1Main(int[] args) {
        String[] algorithms = { "FCFS", "RR", "NSJF", "PSJF" };
        // 1 for FCFS
        // 2 for RR
        // 3 for NSJF
        // 4 for PSJF
        algorithmNumber = args[0];
        // This value will be needed for RoundRobin
        timeQuantum = args[1];

        T = rand.nextInt(25) + 1; // [1, 25]
        readyQueue = new ArrayList<>(T);
        
        System.out.println("--- [Single Core Task1CPU] Scheduler Algorithm: " + algorithms[algorithmNumber - 1] + " ---");
        System.out.println("[Task1Main] Generating " + T + " tasks");

        Task1TaskSendingThread sender = new Task1TaskSendingThread(algorithmNumber);
        sender.start();

        try {
            dispatcherSem.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        dispatcher();
        System.out.println("[Task1Main] All tasks completed");
    }

    private static void dispatcher() {
        while (!readyQueue.isEmpty()) {
            // System.out.println("Dispatcher: I am at line 41."); // Debugging line
            int taskIndex = selectTaskIndex();
            System.out.println("[Dispatcher] Dispatching Task1Task " + (readyQueue.get(taskIndex)).taskId);
            StringBuilder qb = new StringBuilder();
            qb.append("[Dispatcher] + ReadyQueue: ");
            for (Task1Task qt : Task1Main.readyQueue) {
                qb.append("(" + qt.taskId + ":" + qt.remainingTime + ") ");
            }
            System.out.println(qb.toString());            
            Task1CPU.run(readyQueue, algorithmNumber, taskIndex);
            // System.out.println("Dispatcher: I am already at line 48"); // Debugging line
            
            try {
                if (algorithmNumber != 4)
                    dispatcherSem.acquire();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // System.out.println("Dispatcher: I am already at line 57"); // Debugging line

            if (readyQueue.get(taskIndex).finished) {
                readyQueue.remove(taskIndex);
            } else if (algorithmNumber == 2) {
                // Move to end of queue for Round Robin
                Task1Task taskToMove = readyQueue.remove(taskIndex); // Returns the original task
                readyQueue.add(taskToMove); // Adds the SAME task object
            }
            //System.out.println("Dispatcher: I am at line 69.");   // Debugging line
        }
        // System.out.println("Dispatcher: I am done completion at line 61"); // Debugging line
            
    }

    private static int selectTaskIndex() {
        if (algorithmNumber == 3 || algorithmNumber == 4) {
            return readyQueue.indexOf(readyQueue.stream().min(Comparator.comparingInt(t -> t.remainingTime)).get());
        }
        // FCFS and RR just take the first item
        return 0;
    }

    public static boolean checkPreemption(Task1Task current) {
         return readyQueue.stream().anyMatch(t -> t.remainingTime < current.remainingTime);
     }
}