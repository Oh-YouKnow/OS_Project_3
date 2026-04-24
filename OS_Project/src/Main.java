public class Main {
    public static void main(String[] args) {
        // please put this condition
        // if (timeQuantum == 0 && algorithmNumber == 2) print("timeQuantum cannot be 0");
        

        
        // This is for checking if algorithm for task 1 works or not.
        // You can change the values and see if the algorithms work or not.
        // Except for RR, timeQuantum's value does not matter. But make sure it is not null.
        int algorithmNumber = 2;
        int timeQuantum = 5;
        int[] Task1Input = {algorithmNumber, timeQuantum};
        
        // For FCFS
        // int[] Task1Input = { algorithmNumber, 0 };
        
        //For RR
        // int[] Task1Input = { algorithmNumber, timeQuantum};
        
        //  For NSJF
        // int[] Task1Input = { algorithmNumber, 0 };
        
        // For PSJF
        // int[] Task1Input = { algorithmNumber, 0 };
        
        Task1Main.task1Main(Task1Input);
        
        
        
        /*

        // Algorithms for Task 2
        //change values for algorithmNumber, timeQuantum, and cores

        int algorithmNumber = 2; // 1: FCFS, 2: RR, 3:NSJF
        int timeQuantum = 1;
        int cores = 4; // 1-4 cores

        int[] Task2Input = {algorithmNumber, timeQuantum, cores};

        // For FCFS
        // int[] Task2Input = { algorithmNumber, 0, cores };

        // For RR
        // int[] Task2Input = { algorithmNumber, timeQuantum, cores };

        // For NSJF
        // int[] Task2Input = { algorithmNumber, 0, cores };


        Task2Main.task2Main(Task2Input);

        */

    }
}
