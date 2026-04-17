import java.util.concurrent.Semaphore;

public class Task1Task extends Thread {
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
                // Signal dispatcher that cycle/burst is over
                Task1Main.dispatcherSem.release();
            }
            finished = true;
        } catch (Exception e) {
            // Not needed
        }
    }
}