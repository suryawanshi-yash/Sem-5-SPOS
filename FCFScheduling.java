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
    int startTime;       // Start Time

    public Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
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

public class FCFScheduling{

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

        // Call the FCFS scheduling function
        List<GanttChartEntry> ganttChart = calculateFCFS(processes);

        // Display results
        System.out.println("\nPID\tArrival Time\tBurst Time\tCompletion Time\tTurnaround Time\tWaiting Time\tStart Time");
        for (Process process : processes) {
            System.out.println(process.pid + "\t" + process.arrivalTime + "\t\t" + process.burstTime + "\t\t" + process.completionTime +
                    "\t\t" + process.turnAroundTime + "\t\t" + process.waitingTime + "\t\t" + process.startTime);
        }

        // Display Gantt Chart
        System.out.println("\nGantt Chart:");
        for (GanttChartEntry entry : ganttChart) {
            System.out.println("P" + entry.pid + " (" + entry.startTime + " - " + entry.endTime + ")");
        }

        sc.close();
    }

    public static List<GanttChartEntry> calculateFCFS(List<Process> processes) {
        List<GanttChartEntry> ganttChart = new ArrayList<>();
        int currentTime = 0;
        int completed = 0;

        // Sort processes by arrival time
        processes.sort((p1, p2) -> p1.arrivalTime - p2.arrivalTime);

        while (completed < processes.size()) {
            Process selectedProcess = null;

            // Find the process that has arrived and is not completed
            for (Process process : processes) {
                if (process.arrivalTime <= currentTime && process.completionTime == 0) {
                    selectedProcess = process;
                    break; // FCFS, pick the first available process
                }
            }

            if (selectedProcess != null) {
                // Set the start and completion times for the selected process
                selectedProcess.startTime = currentTime;
                currentTime += selectedProcess.burstTime;
                selectedProcess.completionTime = currentTime;
                selectedProcess.turnAroundTime = selectedProcess.completionTime - selectedProcess.arrivalTime;
                selectedProcess.waitingTime = selectedProcess.turnAroundTime - selectedProcess.burstTime;

                // Add to Gantt chart
                ganttChart.add(new GanttChartEntry(selectedProcess.pid, selectedProcess.startTime, selectedProcess.completionTime));

                completed++;
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
Enter arrival time and burst time for Process 1: 0 2
Enter arrival time and burst time for Process 2: 1 2
Enter arrival time and burst time for Process 3: 5 3
Enter arrival time and burst time for Process 4: 6 4
 */