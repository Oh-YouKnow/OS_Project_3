import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Task2Main {
    public static List<Task1Task> readyQueue;
    public static Semaphore queueLock = new Semaphore(1);
    public static Semaphore tasksAvailable = new Semaphore(0);
    public Semaphore doneSem = new Semaphore(0);
    public static int algorithmNumber, timeQuantum, cores, R;
    public static Random rand = new Random();
    public static boolean allTaskGenerated = false;

    public static void task2Main(int[] Task2Input) {
        String[] algorithms = {"FCFS", "RR", "NSJF"};

        algorithmNumber = Task2Input[0];
        timeQuantum = Task2Input[1];
        cores = Task2Input[2];

        R = rand.nextInt(25) + 1; //rand number from 1-25

        readyQueue = new ArrayList<>();

        System.out.println("\nScheduler Algorithm Selected: " + algorithms[algorithmNumber - 1]);
        System.out.println("Generating " + R + " tasks, Cores: " + cores + ", TimeQuantum: " + timeQuantum+ "\n");

        //start task creating thread
        Task2TaskSendingThread sender = new Task2TaskSendingThread(algorithmNumber, R);
        sender.start();

        //start dispatcher threads - one per core
        Task2DispatcherThread[] dispatchers = new Task2DispatcherThread[cores];
        for (int i = 0; i < cores; i++) {
            dispatchers[i] = new Task2DispatcherThread(i);
            dispatchers[i].start();
        }

        //wait for all dispatchers to finish
        for (int i = 0; i < cores; i++) {
            try {
                dispatchers[i].join();
            } catch (InterruptedException e) {
                System.out.println("Dispatcher join interrupted");
            }
        }

        System.out.println("\nAll Tasks Completed");
    }
}
