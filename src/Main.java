public class Main {
    public static void main(String[] args) {
    
        // This is for checking if algorithm for task 1 works or not.
        // You can change the value and try and see if the algorithm works or not.
        // Except for RR, timeQuantum's value do not matter. But make sure it is not null.
        int algorithmNumber = 4;
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
    }
}