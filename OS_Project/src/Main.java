
public class Main {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: -S <algorithm> [quantum] [-C <cores>]");
            return;
        }

        int algorithm = -1;
        int quantum = 0;
        int cores = 1; // default
        boolean foundS = false;

        int i = 0;

        while (i < args.length) {

            switch (args[i]) {

                case "-S":
                    foundS = true;
                    i++;

                    if (i >= args.length) {
                        System.out.println("Missing algorithm number after -S");
                        return;
                    }

                    try {
                        algorithm = Integer.parseInt(args[i]);
                    } catch (Exception e) {
                        System.out.println("Missing algorithm number after -S");
                        return;
                    }
                    

                    if (algorithm < 1 || algorithm > 4) {
                        System.out.println("Algorithm must be between 1 and 4");
                        return;
                    }

                    i++;

                    // Round Robin needs quantum
                    if (algorithm == 2) {
                        if (i >= args.length) {
                            System.out.println("Round Robin requires time quantum");
                            return;
                        }

                        try {
                            quantum = Integer.parseInt(args[i]);
                        } catch (Exception e) {
                            System.out.println("Round Robin requires time quantum");
                            return;
                        }

                        if (quantum < 2 || quantum > 10) {
                            System.out.println("Time quantum must be between 2 and 10");
                            return;
                        }

                        i++;
                    }

                    break;

                case "-C":
                    i++;

                    if (i >= args.length) {
                        System.out.println("Missing core number after -C");
                        return;
                    }

                    cores = Integer.parseInt(args[i]);

                    try {
                        cores = Integer.parseInt(args[i]);
                    } catch (Exception e) {
                        System.out.println("Missing core number after -C");
                        return;
                    }

                    if (cores < 1 || cores > 4) {
                        System.out.println("Cores must be between 1 and 4");
                        return;
                    }

                    i++;
                    break;

                default:
                    System.out.println("Unknown argument: " + args[i]);
                    return;
            }
        }

        if (!foundS) {
            System.out.println("-S argument is required");
            return;
        }

        // Task selection
        if (cores == 1) {
            int[] Task1Input = {algorithm, quantum};
            Task1Main.task1Main(Task1Input);
        } else {
            if(algorithm == 4){
                System.out.println("Multicore does not work implementation for PSJF");
                return;
            } 
            int[] Task2Input = {algorithm, quantum, cores};
            Task2Main.task2Main(Task2Input);
        }
    }
}