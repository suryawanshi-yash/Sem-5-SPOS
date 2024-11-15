
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Process {
    int pid;             // Process ID
    int arrivalTime;     // Arrival Time
    int burstTime;       // Burst Time
    int completionTime;  // Completion Time
    int waitingTime;     // Waiting Time
    int turnAroundTime;  // Turnaround Time
    int startTime;       // Start Time (for Gantt Chart purposes)
    boolean isCompleted; // Track if process is completed

    public Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.isCompleted = false;
    }
}

class GanttChartEntry {
    int pid;
    int startTime;
    int endTime;

    public GanttChartEntry(int pid, int startTime, int endTime) {
        this.pid = pid;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

public class NonPreemptiveSJF {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time and burst time for Process " + (i + 1) + ": ");
            int arrivalTime = sc.nextInt();
            int burstTime = sc.nextInt();
            processes.add(new Process(i + 1, arrivalTime, burstTime));
        }

        // Call the non-preemptive SJF scheduling function
        List<GanttChartEntry> ganttChart = calculateNonPreemptiveSJF(processes);

        // Display process information
        System.out.println("\nProcess\tArrival Time\tBurst Time\tCompletion Time\tWaiting Time\tTurnaround Time");
        
        int totalWaitingTime = 0;
        int totalTurnAroundTime = 0;

        for (Process process : processes) {
            System.out.println(process.pid + "\t\t" + process.arrivalTime + "\t\t" + process.burstTime + "\t\t" 
                               + process.completionTime + "\t\t" + process.waitingTime + "\t\t" + process.turnAroundTime);
            
            totalWaitingTime += process.waitingTime;
            totalTurnAroundTime += process.turnAroundTime;
        }

        // Calculate average waiting time and average turnaround time
        double avgWaitingTime = (double) totalWaitingTime / n;
        double avgTurnAroundTime = (double) totalTurnAroundTime / n;

        System.out.println("\nAverage Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnAroundTime);

        // Display Gantt Chart
        System.out.println("\nGantt Chart:");
        for (GanttChartEntry entry : ganttChart) {
            System.out.println("P" + entry.pid + " (" + entry.startTime + " - " + entry.endTime + ")");
        }

        sc.close();
    }

    public static List<GanttChartEntry> calculateNonPreemptiveSJF(List<Process> processes) {
        List<GanttChartEntry> ganttChart = new ArrayList<>();
        int currentTime = 0;
        int completed = 0;

        while (completed < processes.size()) {
            Process shortestJob = null;

            // Find the process with the shortest burst time that has arrived and is not completed
            for (Process process : processes) {
                if (process.arrivalTime <= currentTime && !process.isCompleted) {
                    if (shortestJob == null || process.burstTime < shortestJob.burstTime) {
                        shortestJob = process;
                    }
                }
            }

            if (shortestJob == null) {
                currentTime++;
            } else {
                // Start executing the selected process
                shortestJob.startTime = currentTime;
                ganttChart.add(new GanttChartEntry(shortestJob.pid, currentTime, currentTime + shortestJob.burstTime));
                
                currentTime += shortestJob.burstTime;
                shortestJob.completionTime = currentTime;
                shortestJob.turnAroundTime = shortestJob.completionTime - shortestJob.arrivalTime;
                shortestJob.waitingTime = shortestJob.turnAroundTime - shortestJob.burstTime;
                shortestJob.isCompleted = true;

                completed++;
            }
        }

        return ganttChart;
    }
}


/*
Enter number of processes: 4
Enter arrival time and burst time for Process 1: 0 5
Enter arrival time and burst time for Process 2: 0 2
Enter arrival time and burst time for Process 3: 0 6
Enter arrival time and burst time for Process 4: 0 4 
*/

/*
Enter number of processes: 4
Enter arrival time and burst time for Process 1: 1 3
Enter arrival time and burst time for Process 2: 2 4
Enter arrival time and burst time for Process 3: 1 2
Enter arrival time and burst time for Process 4: 4 4
 */