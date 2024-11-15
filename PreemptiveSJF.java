import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

class Process {
    int pid;             // Process ID
    int arrivalTime;     // Arrival Time
    int burstTime;       // Burst Time
    int remainingTime;   // Remaining Time
    int completionTime;  // Completion Time
    int waitingTime;     // Waiting Time
    int turnAroundTime;  // Turnaround Time

    public Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

class GanttChartEntry {
    int pid;        // Process ID
    int startTime;  // Start time of this segment
    int endTime;    // End time of this segment

    public GanttChartEntry(int pid, int startTime, int endTime) {
        this.pid = pid;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

public class PreemptiveSJF{

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time and burst time for Process " + (i + 1) + ": ");
            int arrivalTime = sc.nextInt();
            int burstTime = sc.nextInt();
            processes.add(new Process(i + 1, arrivalTime, burstTime));
        }

        // Call the SRTF scheduling function
        List<GanttChartEntry> ganttChart = calculateSRTF(processes);

        // Calculate total waiting time and turnaround time
        int totalWaitingTime = 0;
        int totalTurnAroundTime = 0;

        System.out.println("\nProcess\tArrival Time\tBurst Time\tCompletion Time\tWaiting Time\tTurnaround Time");
        for (Process process : processes) {
            System.out.println(process.pid + "\t\t" + process.arrivalTime + "\t\t" + process.burstTime +
                    "\t\t" + process.completionTime + "\t\t" + process.waitingTime + "\t\t" + process.turnAroundTime);

            totalWaitingTime += process.waitingTime;
            totalTurnAroundTime += process.turnAroundTime;
        }

        // Calculate average waiting time and turnaround time
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

    public static List<GanttChartEntry> calculateSRTF(List<Process> processes) {
        PriorityQueue<Process> queue = new PriorityQueue<>(Comparator.comparingInt(p -> p.remainingTime));
        List<GanttChartEntry> ganttChart = new ArrayList<>();

        int currentTime = 0;
        int completed = 0;
        Process lastProcess = null;

        while (completed < processes.size()) {
            // Add all processes that have arrived by the current time to the queue
            for (Process process : processes) {
                if (process.arrivalTime <= currentTime && process.remainingTime > 0 && !queue.contains(process)) {
                    queue.add(process);
                }
            }

            if (queue.isEmpty()) {
                currentTime++;
            } else {
                Process currentProcess = queue.poll();

                if (lastProcess != null && lastProcess.pid != currentProcess.pid) {
                    ganttChart.add(new GanttChartEntry(lastProcess.pid, currentTime - 1, currentTime));
                }

                // Execute the process for 1 unit time
                currentProcess.remainingTime--;
                currentTime++;

                if (lastProcess != currentProcess) {
                    ganttChart.add(new GanttChartEntry(currentProcess.pid, currentTime - 1, currentTime));
                }

                if (currentProcess.remainingTime == 0) {
                    completed++;
                    currentProcess.completionTime = currentTime;
                    currentProcess.turnAroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnAroundTime - currentProcess.burstTime;
                } else {
                    queue.add(currentProcess);
                }

                lastProcess = currentProcess;
            }
        }

        return ganttChart;
    }
}

/*
 * Enter number of processes: 4
Enter arrival time and burst time for Process 1: 0 6
Enter arrival time and burst time for Process 2: 1 2
Enter arrival time and burst time for Process 3: 2 8
Enter arrival time and burst time for Process 4: 3 4
 */