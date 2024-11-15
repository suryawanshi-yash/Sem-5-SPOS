

import java.util.*;
public class lru{
 public static void main(String[] args) {
 Scanner scanner = new Scanner(System.in);
 System.out.print("Enter the number of frames: ");
 int numberOfFrames = scanner.nextInt();
 System.out.print("Enter the number of pages: ");
 int numberOfPages = scanner.nextInt();
 System.out.print("Enter the page reference string (space-separated): ");
 int[] pageReferenceString = new int[numberOfPages];
 for (int i = 0; i < numberOfPages; i++) {
 pageReferenceString[i] = scanner.nextInt();
 }
 LinkedList<Integer> frames = new LinkedList<>();
 int pageFaults = 0;
 for (int page : pageReferenceString) {
 if (!frames.contains(page)) {
 if (frames.size() >= numberOfFrames) {
 frames.removeFirst(); // Remove the least recently used page
 }
 frames.addLast(page);
 pageFaults++;
 } else {
 frames.remove(frames.indexOf(page)); // Move the used page to the end
 frames.addLast(page);
 }
System.out.print("Frames: ");
 for (int frame : frames) {
 System.out.print(frame + " ");
 }
 System.out.println();
 }
 System.out.println("Total Page Faults: " + pageFaults);
 System.out.println("Page Faults ratio: " + pageFaults + ":"+numberOfPages);
 scanner.close();
 }
}

/*
Enter the number of frames: 4
Enter the number of pages: 20
Enter the page reference string (space-separated): 7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1
 */