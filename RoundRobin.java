import java.util.Scanner;

public class RoundRobin{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        int[] burstTime = new int[n];
        int[] remainingTime = new int[n];
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];
        int[] arrivalTime = new int[n];
        System.out.print("Enter time quantum: ");
        int quantum = sc.nextInt();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time and burst time for process " + (i + 1) + ": ");
            arrivalTime[i] = sc.nextInt();
            burstTime[i] = sc.nextInt();
            remainingTime[i] = burstTime[i];
        }
        int currentTime = 0;
        int completedProcesses = 0;
        String ganttChart = "";
        while (completedProcesses < n) {
            boolean found = false;
            for (int i = 0; i < n; i++) {
                if (remainingTime[i] > 0 && arrivalTime[i] <= currentTime) {
                    found = true;
                    if (remainingTime[i] > quantum) {
                        currentTime += quantum;
                        remainingTime[i] -= quantum;
                        ganttChart += "P" + (i + 1) + " ";
                    } else {
                        currentTime += remainingTime[i];
                        ganttChart += "P" + (i + 1) + " ";
                        waitingTime[i] = currentTime - burstTime[i] - arrivalTime[i];
                        turnaroundTime[i] = waitingTime[i] + burstTime[i];
                        remainingTime[i] = 0;
                        completedProcesses++;
                    }
                }
            }
            if (!found) {
                currentTime++;
                ganttChart += "idle ";
            }
        }
        // Output Gantt Chart
        System.out.println("Gantt Chart: " + ganttChart);
        // Calculate average waiting and turnaround time
        float totalWT = 0, totalTAT = 0;
        System.out.println("Process\tArrival\tBurst\tWaiting\tTurnaround");
        for (int i = 0; i < n; i++) {
            totalWT += waitingTime[i];
            totalTAT += turnaroundTime[i];
            System.out.println("P" + (i + 1) + "\t" + arrivalTime[i] + "\t" + burstTime[i] + "\t" +
                    waitingTime[i] + "\t" + turnaroundTime[i]);
        }
        System.out.println("Average Waiting Time: " + (totalWT / n));
        System.out.println("Average Turnaround Time: " + (totalTAT / n));
        sc.close();
    }
}

/*
 Enter number of processes: 4
 Enter the time quantum: 2
Enter arrival time and burst time for Process 1: 0 5
Enter arrival time and burst time for Process 2: 0 2
Enter arrival time and burst time for Process 3: 0 6
Enter arrival time and burst time for Process 4: 0 4
 */
/*
 Enter number of processes: 4
 Enter the time quantum: 2
Enter arrival time and burst time for Process 1: 0 5
Enter arrival time and burst time for Process 2: 1 4
Enter arrival time and burst time for Process 3: 2 2
Enter arrival time and burst time for Process 4: 4 1
 */