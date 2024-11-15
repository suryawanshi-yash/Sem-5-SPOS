import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class Process {
    int pid;             // Process ID
    int arrivalTime;     // Arrival Time
    int burstTime;       // Burst Time
    int priority;        // Priority
    int completionTime;  // Completion Time
    int waitingTime;     // Waiting Time
    int turnAroundTime;  // Turnaround Time
    int startTime;       // Start Time

    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
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

public class PriorityNonPreemptiveScheduling {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time, burst time, and priority for Process " + (i + 1) + ": ");
            int arrivalTime = sc.nextInt();
            int burstTime = sc.nextInt();
            int priority = sc.nextInt();
            processes.add(new Process(i + 1, arrivalTime, burstTime, priority));
        }

        // Call the priority scheduling function
        List<GanttChartEntry> ganttChart = calculatePriorityScheduling(processes);

        // Calculate total waiting time and turnaround time
        int totalWaitingTime = 0;
        int totalTurnAroundTime = 0;

        // Display results and calculate totals for averages
        System.out.println("\nPID\tArrival Time\tBurst Time\tPriority\tCompletion Time\tTurnaround Time\tWaiting Time\tStart Time");
        for (Process process : processes) {
            System.out.println(process.pid + "\t" + process.arrivalTime + "\t\t" + process.burstTime + "\t\t" + process.priority +
                    "\t\t" + process.completionTime + "\t\t\t" + process.turnAroundTime + "\t\t" + process.waitingTime + "\t\t" + process.startTime);

            totalWaitingTime += process.waitingTime;
            totalTurnAroundTime += process.turnAroundTime;
        }

        // Calculate and display average waiting time and turnaround time
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

    public static List<GanttChartEntry> calculatePriorityScheduling(List<Process> processes) {
        List<GanttChartEntry> ganttChart = new ArrayList<>();
        int currentTime = 0;
        int completed = 0;

        // Sort processes by arrival time, and then by priority in case of tie
        processes.sort(Comparator.comparingInt((Process p) -> p.arrivalTime).thenComparingInt(p -> p.priority));

        while (completed < processes.size()) {
            Process selectedProcess = null;

            // Find process with highest priority that has arrived and is not completed
            for (Process process : processes) {
                if (process.arrivalTime <= currentTime && process.completionTime == 0) {
                    if (selectedProcess == null || process.priority < selectedProcess.priority) {
                        selectedProcess = process;
                    }
                }
            }

            if (selectedProcess != null) {
                // Start execution of selected process
                selectedProcess.startTime = currentTime;
                currentTime += selectedProcess.burstTime;
                selectedProcess.completionTime = currentTime;
                selectedProcess.turnAroundTime = selectedProcess.completionTime - selectedProcess.arrivalTime;
                selectedProcess.waitingTime = selectedProcess.turnAroundTime - selectedProcess.burstTime;

                // Add to Gantt chart
                ganttChart.add(new GanttChartEntry(selectedProcess.pid, selectedProcess.startTime, selectedProcess.completionTime));

                completed++;
            } else {
                currentTime++;
            }
        }

        return ganttChart;
    }
}


/*
  Enter number of processes: 4
Enter burst time and priority for process 1: 5 3
Enter burst time and priority for process 2: 2 4
Enter burst time and priority for process 3: 6 1
Enter burst time and priority for process 4: 4 2
 */