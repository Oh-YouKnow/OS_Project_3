public class Task1TaskSendingThread extends Thread {
    int algorithmNumber;
    static int thresoldForPremptive = 3;
    public Task1TaskSendingThread(int algorithmNumber) {
        this.algorithmNumber = algorithmNumber;
    }

    public void run() {
        if (algorithmNumber == 4 && Task1Main.T < 5) {
            System.out.println("[Task Creating Thread] PSJF needs more taks than " + Task1Main.T + " tasks");
            Task1Main.T = 10;
            System.out.println("[Task Creating Thread] will be creating a total of " + Task1Main.T + " instead");
        }
        int tasksGenerated = 0;
        for (int i = 1; i <= Task1Main.T; i++) {
            int B = Task1Main.rand.nextInt(50) + 1; // [1, 50]
            Task1Task t = new Task1Task(i, B);
            Task1Main.readyQueue.add(t);
            tasksGenerated++;
            System.out.println("[Task Creating Thread] Task " + i + " created with Burst: " + B);
            t.start();

            if (Task1Main.algorithmNumber == 4) {
                try {
                    if (i == thresoldForPremptive) {
                        Task1Main.dispatcherSem.release();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (Task1Main.readyQueue.size() >= thresoldForPremptive && !Task1Main.allTaskGenerated ) {
                    try {
                        Task1Main.taskSendingSem.acquire();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            if (tasksGenerated == Task1Main.T)
                Task1Main.allTaskGenerated = true;
        }

        if (Task1Main.algorithmNumber != 4)
            Task1Main.dispatcherSem.release();
    }
    
    public static void sendTask() {
        Task1Main.taskSendingSem.release();
    }

}