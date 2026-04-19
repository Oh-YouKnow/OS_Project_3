import java.util.concurrent.Semaphore;

public class Task1Task extends Thread {
    public Semaphore doneSem = new Semaphore(0); //added for task2 dispatcherThread
    int taskId;
    int burstTime;
    int timeSlice;
    int remainingTime;
    boolean finished = false;
    public Semaphore cpuSem = new Semaphore(0);

    public Task1Task(int id, int burst) {
        this.taskId = id;
        this.burstTime = burst;
        remainingTime = burst;
    }

    @Override
    public void run() {
        try {
            while (remainingTime > 0) {
                cpuSem.acquire();

                for (int i = 0; i < timeSlice; i++) {
                    remainingTime--;
                    if (remainingTime == 0) {
                        finished = true;
                        break;
                    }
                }     
                //signal dispatcher that cycle/burst is over (for Task1)
                try {
                    Task1Main.dispatcherSem.release();
                } catch (Exception e) {
                    // ignore if not used
                }
                //signal multi-core dispatcher waiting for this task (for Task2)
                try {
                    doneSem.release();
                } catch (Exception e) {
                    // ignore
                }
            }
            finished = true;
        } catch (Exception e) {
            // Not needed
        }
    }
}