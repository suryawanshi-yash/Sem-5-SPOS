import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;
class assemblerPassone{
    static Scanner in = new Scanner(System.in);
    static String is[] = { "STOP","ADD","SUB","MULT","MOVER","MOVEM","COMP","BC","DIV","READ","PRINT"};
    static String ad[] = { "START","END","ORIGIN","EQU","LTORG"};
    static String dl[] = { "DC" , "DS" };
    static String cc[] = { "LT","LE","EQ","GT","GE","ANY"};
    static int symcounter =0 ;
    static int litcounter =0 ;
    static String sym[][] = new String[100][2];
    static String lit[][] = new String[100][2];
    static String ptab[][] = new String[100][2];
    public static void main(String args[]) throws Exception{
        int locate=0;
        int litcount = 0;
        BufferedReader reader = new BufferedReader(new FileReader("input.asm")); //FOR INPUT FILE
        BufferedWriter writer= new BufferedWriter(new FileWriter("intermediate.txt")); //FOR OUTPUT FILE
        BufferedWriter writer1= new BufferedWriter(new FileWriter("SYMTAB.txt")); //FOR SYMBOL TABLE FILE
        BufferedWriter writer2= new BufferedWriter(new FileWriter("LITTAB.txt")); //FOR LITERAL TABLE FILE

        String st;
        String y,prev=null;
        int stp = 0;
        String ans;
        int k = 0;
        String buffer = ""; //FOR INPUT FILE-INTERMEDIATE CODE
        String buffer1 = ""; //FOR SYMBOL TABLE
        String buffer2 = ""; //FOR LITERAL TABLE

        while((st = reader.readLine())!=null){
            int isflag=0;
            k++;
            StringTokenizer splitted = new StringTokenizer(st);
            ans="";
            while(splitted.hasMoreTokens()){
                y = splitted.nextToken();
                if(y.equals("START")){
                    locate = Integer.parseInt(splitted.nextToken());
                    ans="(AD,01)(C,"+locate+")";
                    break;
                }
                else{
                    if(searchis(y)){
                        if(y.equals("STOP")){
                            stp=1;
                        }
                        ans+="(IS,"+Integer.toString(indexis(y))+")";
                        isflag=1;
                        locate+=1;
                    }
                    else if(searchad(y)){
                        if(y.equals("LTORG")){
                            locate+=litcount;
                            ans="(AD,05)\n";
                            while(litcount>0){
                                lit[litcounter-litcount][1] = Integer.toString(locate-litcount);
                                int len=lit[litcounter-litcount][0].length();
                                String temp = lit[litcounter-litcount][0].substring(2,len-1);
                                ans+="(DL,02)(C,"+temp+")";
                                litcount--;
                                if(litcount!=0)
                                    ans+="\n";
                            }
                        }
                        if(y.equals("ORIGIN")){
                            y=splitted.nextToken();
                            String[] words = y.split("\\+");
                            int location = Integer.parseInt(sym[indexsym(words[0])][1]);
                            locate=location+Integer.parseInt(words[1]);
                            ans="(AD,03)(S,"+Integer.toString(indexsym(words[0])+1)+")+"+words[1];
                        }
                        if(y.equals("END") && litcount!=0){
                            locate+=litcount;
                            ans="(AD,02)\n";
                            while(litcount>0){
                                lit[litcounter-litcount][1] = Integer.toString(locate-litcount);
                                int len=lit[litcounter-litcount][0].length();
                                String temp = lit[litcounter-litcount][0].substring(2,len-1);
                                ans+="(DL,02)(C,"+temp+")\n";
                                litcount--;
                                if(litcount!=0)
                                    ans+="\n";
                            }
                        }
                        if(y.equals("EQU")){
                            int temp = indexsym(splitted.nextToken());
                            y=prev;
                            sym[indexsym(y)][1]=  sym[temp][1];
                            ans="";
                        }
                    }
                    else if(searchdl(y)){
                        if(y.equals("DS")){
                            ans="";
                            ans+="(DL,1)(C,"+splitted.nextToken()+")";
                        }
                        if(y.equals("DC")){
                            ans="";
                            ans+="(DL,2)(C,"+splitted.nextToken()+")";
                        }
                        locate+=1;
                    }
                    else{
                        prev=y;
                        char[] x=y.toCharArray();
                        if(x[0]=='='){
                            int z=litcounter;
                            ans+="(L,"+(z+1)+")";
                            lit[litcounter++][0]=y;
                            litcount++;
                        }
                        else if(y.equals("AREG")){
                            ans+="(R,1)";
                        }
                        else if(y.equals("BREG")){
                            ans+="(R,2)";
                        }
                        else if(y.equals("CREG")){
                            ans+="(R,3)";
                        }
                        else if(y.equals("DREG")){
                            ans+="(R,4)";
                        }
                        else if(searchcc(y)){
                            ans+="("+Integer.toString(indexcc(y)+1)+")";
                        }
                        else{
                            if(!searchsym(y) && isflag==0 && stp==0){
                                sym[symcounter][0] = y;
                                sym[symcounter++][1] = Integer.toString(locate);
                                ans+="(S,"+Integer.toString(indexsym(y)+1)+")";
                                if(splitted.hasMoreTokens())
                                    ans="";
                            }
                            else if(!searchsym(y) && isflag==1 && stp==0 ){//if instruction has passed on the line then only add the symbol not the address
                                sym[symcounter++][0] = y;
                                ans+="(S,"+Integer.toString(indexsym(y)+1)+")";
                            }
                            else if(searchsym(y) && isflag==0){
                                sym[indexsym(y)][1]= Integer.toString(locate);
                                ans+="(S,"+Integer.toString(indexsym(y)+1)+")";
                                if(splitted.hasMoreTokens())
                                    ans="";
                                prev=y;
                            }
                            else{
                                if(!splitted.hasMoreTokens())
                                    ans+="(S,"+Integer.toString(indexsym(y)+1)+")";
                                continue;
                            }
                        }
                    }
                }
            }
            ans=ans+"\n";
            buffer+=ans;
        }
        System.out.println(buffer+"\n"); //OUTPUT FILE-INTERMEDIATE CODE

        String ans1 = ""; //SYMBOL TABLE
        for(int i=0;i<symcounter;i++){
            ans1 += sym[i][0]+"\t";
            ans1 += sym[i][1]+"\n";
        }
        buffer1+=ans1;
        System.out.println();

        String ans2 = ""; //LITERAL TABLE
        for(int i=0;i<litcounter;i++){
            ans2 += lit[i][0]+"\t";
            ans2 += lit[i][1]+"\n";
        }
        buffer2+=ans2;
        System.out.println();

        writer.write(buffer);
        writer1.write(buffer1);
        writer2.write(buffer2);

        reader.close();
        writer.close();
        writer1.close();
        writer2.close();
        System.out.println("Program finished................");
    }
    public static boolean searchis(String s){
        boolean flag = false;
        int i=0;
        while(i<11){
            if(is[i].equals(s)){
                flag=true;
                break;
            }
            i++;
        }
        return flag;
    }
    public static boolean searchad(String s){
        boolean flag = false;
        int i=0;
        while(i<5){
            if(ad[i].equals(s)){
                flag=true;
                break;
            }
            i++;
        }
        return flag;
    }
    public static boolean searchdl(String s){
        boolean flag = false;
        int i=0;
        while(i<2){
            if(dl[i].equals(s)){
                flag=true;
                break;
            }
            i++;
        }
        return flag;
    }
    public static boolean searchsym(String s){
        boolean flag = s.equals("BREG") || s.equals("AREG") || s.equals("CREG") || s.equals("DREG") || s.equals(",") ||s.equals("LE") || s.equals("LT") ||s.equals("ANY") ||s.equals("EQ") ||s.equals("GT") ||s.equals("GE");
        int i=0;
        while(i<symcounter ){
            if(sym[i][0].equals(s)){
                flag=true;
                break;
            }
            i++;
        }
        return flag;
    }
    public static boolean searchcc(String s){
        boolean flag = false;
        int i=0;
        while(i<6){
            if(cc[i].equals(s)){
                flag=true;
                break;
            }
            i++;
        }
        return flag;
    }
    public static int indexsym(String s){
        int c = 0;
        int i=0;
        while(i<symcounter){
            if(sym[i][0].equals(s)){
                c=i;
                break;
            }
            i++;
        }
        return i;
    }
    public static int indexlit(String s){
        int c = 0;
        int i=0;
        while(i<litcounter){
            if(lit[i][0].equals(s)){
                c=i;
                break;
            }
            i++;
        }
        return i;
    }
    public static int indexis(String s){
        int i=0;
        while(i<11){
            if(is[i].equals(s)){
                break;
            }
            i++;
        }
        return i;
    }
    public static int indexad(String s){
        int i=0;
        while(i<5){
            if(ad[i].equals(s)){
                break;
            }
            i++;
        }
        return i;
    }
    public static int indexdl(String s){
        int i=0;
        while(i<2){
            if(dl[i].equals(s)){
                break;
            }
            i++;
        }
        return i;
    }
    public static int indexcc(String s){
        int i=0;
        while(i<6){
            if(cc[i].equals(s)){
                break;
            }
            i++;
        }
        return i;
    }

}

/*
 INPUT
START 200
        MOVER   AREG , ='5'
        MOVEM   AREG , X
L1      MOVER   BREG , ='2'
        ORIGIN L1+3
        LTORG
NEXT    ADD AREG , ='1'
        SUB BREG , ='2'
        BC  LT , BACK
        LTORG
BACK    EQU L1
        ORIGIN  NEXT+5
        MULT    CREG , ='4'
        STOP
        X DS 1
        END






OUTPUT :-INTERMEDIATE CODE:
(AD,01)(C,200)
(IS,4)(R,1)(L,1)
(IS,5)(R,1)(S,1)
(IS,4)(R,2)(L,2)
(AD,03)(S,2)+3
(AD,05)
(DL,02)(C,5)
(DL,02)(C,2)
(IS,1)(R,1)(L,3)
(IS,2)(R,2)(L,4)
(IS,7)(1)(S,4)
(AD,05)
(DL,02)(C,1)
(DL,02)(C,2)
(AD,03)(S,3)+5
(IS,3)(R,3)(L,5)
(IS,0)
(DL,1)(C,1)
(AD,02)
(DL,02)(C,4)



 
LITERAL TABLE:
='5'	205
='2'	206
='1'	210
='2'	211
='4'	215
SYMBOL TABLE 
X	214
L1	202
NEXT	207
BACK	202
 */