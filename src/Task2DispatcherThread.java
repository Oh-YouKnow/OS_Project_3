import java.util.List;

public class Task2DispatcherThread extends Thread{
    int coreID;

    public Task2DispatcherThread(int coreID){
        this.coreID = coreID;
    }

    @Override
    public void run(){
        System.out.println("[Dispatcher-" + coreID + "] started");

        while(true){
            try {
                //wait until at least one task available
                Task2Main.tasksAvailable.acquire();
            }  catch (InterruptedException e) {
                System.out.println("[Dispatcher-" + coreID + "] Interrupted");
            }

            //if queue empty and all generated then exit
            try {
                Task2Main.queueLock.acquire();

                if (Task2Main.readyQueue.isEmpty()) {
                    if (Task2Main.allTaskGenerated) {
                        Task2Main.queueLock.release();
                        break;
                    } else {
                        //no tasks right now
                        Task2Main.queueLock.release();
                        continue;
                    }
                }

                //select task index based on algorithm
                int taskIndex = 0;
                if (Task2Main.algorithmNumber == 3) { // NSJF
                    List<Task1Task> queue = Task2Main.readyQueue;
                    Task1Task min = null;
                    int minRemaining = Integer.MAX_VALUE;
                    for (Task1Task t : queue) {
                        if (t.remainingTime < minRemaining) {
                            min = t;
                            minRemaining = t.remainingTime;
                        }
                    }
                    if (min != null) taskIndex = Task2Main.readyQueue.indexOf(min);
                }

                // remove chosen task from ready queue while holding lock
                Task1Task task = Task2Main.readyQueue.remove(taskIndex);

                // print ready queue contents (after removal)
                StringBuilder qb = new StringBuilder();
                qb.append("[Dispatcher-" + coreID + "] ReadyQueue: ");
                for (Task1Task qt : Task2Main.readyQueue) {
                    qb.append("(" + qt.taskId + ":" + qt.remainingTime + ") ");
                }
                System.out.println(qb.toString());

                // decide time slice
                if (Task2Main.algorithmNumber == 1 || Task2Main.algorithmNumber == 3) {
                    // FCFS and NSJF are non-preemptive here: run for the task's remaining time
                    task.timeSlice = task.remainingTime;
                } else if (Task2Main.algorithmNumber == 2) {
                    // Round Robin: cap time slice to remaining time
                    task.timeSlice = Math.min(Task2Main.timeQuantum, task.remainingTime);
                }

                System.out.println("[Dispatcher-" + coreID + "] Dispatching Task " + task.taskId + " (burst=" + task.burstTime + ") to core " + coreID + " for " + task.timeSlice + " cycles");

                Task2Main.queueLock.release();

                // run the task by releasing its cpuSemaphore
                task.cpuSem.release();

                // wait until task completes its time slice
                try {
                    task.doneSem.acquire();
                } catch (InterruptedException e) {
                    System.out.println("[Dispatcher-" + coreID + "] waiting interrupted");
                }

                //after time slice, acquire lock to update queue
                try {
                    Task2Main.queueLock.acquire();
                    if (task.finished) {
                        System.out.println("[Dispatcher-" + coreID + "] Task " + task.taskId + " finished on core " + coreID);
                    } else if (Task2Main.algorithmNumber == 2) {
                        // Round Robin: move to end of queue
                        Task2Main.readyQueue.add(task);
                        // signal availability for next round
                        Task2Main.tasksAvailable.release();
                    } else {
                        // For FCFS/NSJF (non-preemptive) we expect task to be finished; if not, re-add
                        Task2Main.readyQueue.add(task);
                        Task2Main.tasksAvailable.release();
                    }
                } finally {
                    Task2Main.queueLock.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("[Dispatcher-" + coreID + "] exiting");
    }
}

