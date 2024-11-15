//Write a program to implement by Optimal Page replacement algorithm

import java.util.*;

public class optimal{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input: number of frames
        System.out.print("Enter number of frames: ");
        int numFrames = scanner.nextInt();

        // Input: page reference string
        System.out.print("Enter number of pages: ");
        int numPages = scanner.nextInt();
        int[] pageReference = new int[numPages];
        System.out.print("Enter the page reference string: ");
        for (int i = 0; i < numPages; i++) {
            pageReference[i] = scanner.nextInt();
        }

        // Initialize frames with -1 (indicating empty)
        int[] frames = new int[numFrames];
        Arrays.fill(frames, -1);
        int pageFaults = 0;

        // Process each page in the reference string
        for (int i = 0; i < numPages; i++) {
            int currentPage = pageReference[i];
            boolean isPageInFrames = false;

            // Check if page is already in frames
            for (int frame : frames) {
                if (frame == currentPage) {
                    isPageInFrames = true;
                    break;
                }
            }

            // If page is not in frames, replace a page
            if (!isPageInFrames) {
                pageFaults++;

                // Find the frame to replace based on the optimal strategy
                int farthestIndex = -1;
                int indexToReplace = -1;

                for (int j = 0; j < numFrames; j++) {
                    int framePage = frames[j];
                    int nextUse = Integer.MAX_VALUE;  // Assume page not used in future

                    for (int k = i + 1; k < numPages; k++) {
                        if (pageReference[k] == framePage) {
                            nextUse = k;
                            break;
                        }
                    }

                    // Update the frame to replace if it has the farthest use in future
                    if (nextUse > farthestIndex) {
                        farthestIndex = nextUse;
                        indexToReplace = j;
                    }
                }

                // Replace the page in the selected frame
                frames[indexToReplace == -1 ? 0 : indexToReplace] = currentPage;
            }

            // Print the current state of frames
            System.out.print("Frames after inserting page " + currentPage + ": ");
            for (int frame : frames) {
                if (frame == -1) System.out.print("[ ] ");
                else System.out.print("[" + frame + "] ");
            }
            System.out.println();
        }

        System.out.println("Total page faults: " + pageFaults);
        scanner.close();
    }
}

/*
Enter number of frames: 4
Enter number of pages: 20
Enter the page reference string: 7 0 1 2 0 3 0 4 2 3  0 3 2 1 2 0 1 7 0 1
 */