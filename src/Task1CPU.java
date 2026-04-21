import java.util.*;

public class Task1CPU {

    public static void run(List<Task1Task> readyQueue, int algorithmNumber, int taskIndex) {
        if(algorithmNumber == 1)
            runFCFS(readyQueue,  taskIndex);
        else if (algorithmNumber == 2)
            runRR(readyQueue,  taskIndex);
        else if (algorithmNumber == 3)
            runNSJF(readyQueue,  taskIndex);
        else if (algorithmNumber == 4)
            runPSJF(readyQueue,  taskIndex);
    }

    private static void runFCFS(List<Task1Task> readyQueue, int taskIndex) {
        Task1Task task = readyQueue.get(taskIndex);
        task.timeSlice = task.burstTime;
        System.out.println("[Task1CPU] Running Task1Task " + task.taskId + " for " + task.timeSlice + " cycles/burstTime");
        (readyQueue.get(taskIndex)).cpuSem.release();
    }

    private static void runRR(List<Task1Task> readyQueue, int taskIndex) {
        Task1Task task = readyQueue.get(taskIndex);
        task.timeSlice = Task1Main.timeQuantum;
        System.out.println("[Task1CPU] Running Task1Task " + task.taskId + " for " + task.timeSlice + " cycles/burstTime");
        (readyQueue.get(taskIndex)).cpuSem.release();
    }

    private static void runNSJF(List<Task1Task> readyQueue, int taskIndex) {
        Task1Task task = readyQueue.get(taskIndex);
        task.timeSlice = task.burstTime;
        System.out.println("[Task1CPU] Running Task1Task " + task.taskId + " for " + task.timeSlice + " cycles/burstTime");
        (readyQueue.get(taskIndex)).cpuSem.release();
    }

    private static void runPSJF(List<Task1Task> readyQueue, int taskIndex) {
        Task1Task task = readyQueue.get(taskIndex);
        System.out.println("[Task1CPU] Running Task1Task " + task.taskId + " for " + task.remainingTime
                + " cycles/bursts if no premption");
        while (!Task1Main.checkPreemption(task) && !task.finished) {
            task.timeSlice = 1;
            (readyQueue.get(taskIndex)).cpuSem.release();
            try {
                Task1Main.dispatcherSem.acquire();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (Task1Main.algorithmNumber == 4 && readyQueue.size() < Task1TaskSendingThread.thresoldForPremptive && !Task1Main.allTaskGenerated) {
                Task1TaskSendingThread.sendTask();
            }
        }
        if (!task.finished) {
            System.out.println("------------------------------------------------------------");
            System.out.println("[Task1CPU] Premption has occured when Task " + task.taskId + " was executing");
        } 
        else {
            System.out.println("[Task1CPU] Task " + readyQueue.get(taskIndex).taskId + " is completed");
        }
    }
    
}
