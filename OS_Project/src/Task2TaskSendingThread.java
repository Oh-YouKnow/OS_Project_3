import java.util.Random;

public class Task2TaskSendingThread extends Thread{
    int algorithmNumber;
    int R;
    Random rand = new Random();

    public Task2TaskSendingThread(int algorithmNumber, int R){
        this.algorithmNumber = algorithmNumber;
        this.R = R;
    }

    public void run(){
        System.out.println("[Thread] Creating " + R + " tasks for Task 2");
        for (int i = 1; i <= R; i++) {
            int B = rand.nextInt(50) + 1; //rand number from 1-50
            Task1Task t = new Task1Task(i, B);

            try {
                //acquire queue lock, add task, then release
                Task2Main.queueLock.acquire();
                Task2Main.readyQueue.add(t);
                System.out.println("[Thread] Task " + i + " created with Burst: " + B);
                t.start();
            } catch (InterruptedException e) {
                System.out.println("[Task Creating Thread] Interrupted");
            } finally {
                Task2Main.queueLock.release();
            }

            //release task
            Task2Main.tasksAvailable.release();
        }

        Task2Main.allTaskGenerated = true;
        //release dispatchers if they are waiting and queue is done
        for (int i = 0; i < Task2Main.cores; i++){
            Task2Main.tasksAvailable.release();
        }
    }
}

