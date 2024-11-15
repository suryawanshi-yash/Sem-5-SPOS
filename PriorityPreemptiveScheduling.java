import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Process {
    int pid;             // Process ID
    int arrivalTime;     // Arrival Time    
    int burstTime;       // Burst Time
    int remainingTime;   // Remaining Time (for preemption)
    int priority;        // Priority
    int completionTime;  // Completion Time
    int waitingTime;     // Waiting Time
    int turnAroundTime;  // Turnaround Time
    int startTime = -1;  // Start Time

    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
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

public class PriorityPreemptiveScheduling {

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

        // Display results
        System.out.println("\nPID\tArrival Time\tBurst Time\tPriority\tCompletion Time\tTurnaround Time\tWaiting Time\tStart Time");
        for (Process process : processes) {
            System.out.println(process.pid + "\t" + process.arrivalTime + "\t\t" + process.burstTime + "\t\t" + process.priority +
                    "\t\t" + process.completionTime + "\t\t\t" + process.turnAroundTime + "\t\t" + process.waitingTime + "\t\t" + process.startTime);
        }

        // Calculate average waiting time and turnaround time
        double totalWaitingTime = 0;
        double totalTurnAroundTime = 0;
        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnAroundTime += process.turnAroundTime;
        }
        double avgWaitingTime = totalWaitingTime / n;
        double avgTurnAroundTime = totalTurnAroundTime / n;

        // Display averages
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
        Process currentProcess = null;

        while (completed < processes.size()) {
            Process selectedProcess = null;

            // Find process with the highest priority that has arrived and is not completed
            for (Process process : processes) {
                if (process.arrivalTime <= currentTime && process.remainingTime > 0) {
                    if (selectedProcess == null || process.priority < selectedProcess.priority) {
                        selectedProcess = process;
                    }
                }
            }

            if (selectedProcess != null) {
                // Check if this is a new process starting or preempting the current process
                if (currentProcess != selectedProcess) {
                    // If the current process was running, add its current execution to the Gantt chart
                    if (currentProcess != null && currentProcess.remainingTime > 0) {
                        ganttChart.add(new GanttChartEntry(currentProcess.pid, currentProcess.startTime, currentTime));
                    }
                    
                    // Set the start time if it's the first time this process is running
                    if (selectedProcess.startTime == -1) {
                        selectedProcess.startTime = currentTime;
                    }

                    // Switch to the new process
                    currentProcess = selectedProcess;
                }

                // Execute the selected process for one unit of time
                currentProcess.remainingTime--;
                currentTime++;

                // Check if the selected process has completed
                if (currentProcess.remainingTime == 0) {
                    completed++;
                    currentProcess.completionTime = currentTime;
                    currentProcess.turnAroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnAroundTime - currentProcess.burstTime;

                    // Add the completion to the Gantt chart
                    ganttChart.add(new GanttChartEntry(currentProcess.pid, currentProcess.startTime, currentTime));

                    // Reset current process after completion
                    currentProcess = null;
                }
            } else {
                // If no process is ready, increment the time
                currentTime++;
            }
        }

        return ganttChart;
    }
}


/*
Enter number of processes: 4
Enter arrival time, burst time, and priority for Process 1: 0 5 4
Enter arrival time, burst time, and priority for Process 2: 1 4 3
Enter arrival time, burst time, and priority for Process 3: 2 2 2
Enter arrival time, burst time, and priority for Process 4: 4 1 1
 */