//Write a program to implement by FIFO Page replacement algorithm
import java.util.*;

public class Fifo{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.print("enter number of frames:");
        int numofframes=sc.nextInt();
        System.out.print("enter number of pages:");
        int numofpages=sc.nextInt();
        System.out.print("enter page reference string(space -separated):");
        int [] pagereferencestring=new int[numofpages];
        for (int i=0;i<numofpages;i++){
            pagereferencestring[i]=sc.nextInt();
        } 
        int [] frames=new int[numofframes];
        Arrays.fill(frames,-1);
        int pagefaults=0;
        int currentindex=0;
        for (int page:pagereferencestring){
            boolean pagehit=false;
            for(int frame:frames){
                if(frame==page){
                    pagehit=true;
                    break;
                }
            }
            if(!pagehit){
                frames[currentindex]=page;
                currentindex=(currentindex+1)%numofframes;
                pagefaults++;
            }
            System.out.print("Frames:");
            for(int frame:frames){
                System.out.print(frame + " ");
            }
            System.out.println();
        }
        System.out.println("total page faults:"+pagefaults);
        System.out.println("page fault ratio:"+pagefaults+":"+numofpages);
        sc.close();

    }
    
}

/*
enter number of frames:3
enter number of pages:15
enter page reference string(space -separated):7 0 1 2 0 3 0 4 2 3 0 3 1 2 0
 */