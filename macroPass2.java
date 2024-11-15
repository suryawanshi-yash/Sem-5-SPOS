import java.io.*;
import java.util.*;
class macroPass2{
    public static void main(String args[]) {
        pass2();
        System.out.println("Argument List Array(ALA) for Pass2");
        display(Pass1.ala, Pass1.alac, 2);
        System.out.println("Note: All tables are displayed here whereas the expanded output is stored in the file pass2_output.txt");
}
static void pass2() {
        int alap = 0, index, mdtp, flag = 0, i, j;
        String s, temp;
        try {
            BufferedReader inp = new BufferedReader(new FileReader("pass1_output.txt"));
            File op = new File("pass2_output.txt");
            if (!op.exists()) {
                op.createNewFile();
            }
            BufferedWriter output = new BufferedWriter(new FileWriter(op.getAbsoluteFile()));
            for (; (s = inp.readLine()) != null; flag = 0) {
                StringTokenizer st = new StringTokenizer(s);
                String str[] = new String[st.countTokens()];
                for (i = 0; i < str.length; i++) {
                    str[i] = st.nextToken();
                }
                for (j = 0; j < Pass1.mntc; j++) {
                    if (str[0].equals(Pass1.mnt[j][1])) {
                        mdtp = Integer.parseInt(Pass1.mnt[j][2]);

                        st = new StringTokenizer(str[1], ",");
                        String arg[] = new String[st.countTokens()];
                        for (i = 0; i < arg.length; i++) {
                            arg[i] = st.nextToken();
                            Pass1.ala[alap++][1] = arg[i];
                        }
                        for (i = mdtp; !(Pass1.mdt[i][0].equalsIgnoreCase("MEND")); i++) { 
                            // Expand until MEND 
                            index = Pass1.mdt[i][0].indexOf("#");
                            temp = Pass1.mdt[i][0].substring(0, index);
                            temp += Pass1.ala[Integer.parseInt("" + Pass1.mdt[i][0].charAt(index + 1))][1]; 
                            //Convert char->string -> integer & append it
                            output.write(temp);
                            output.newLine();
                        }
                        flag = 1;
                    }
                }
                if (flag == 0) { // When it is not a macro
                    output.write(s);
                    output.newLine();
                }
            }
            output.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to find file ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void display(String a[][], int n, int m) {
        int i, j;
        for (i = 0; i < n; i++) {
            for (j = 0; j < m; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
    }
}

/*
 INPUT:
PRG2 START
USING *,BASE
INCR1 DATA1,DATA2
INCR2 DATA3,DATA4
FOUR DC F'4'
FIVE DC F'5'
BASE EQU 8
TEMP DS 1F
DROP 8
END




Output:
Argument List Array(ALA) for Pass2
0 DATA1
1 DATA2
2 DATA3
3 DATA4
PRG2 START
USING *,BASE
A 1,DATA1
L 2,DATA2
L 3,DATA3
ST 4,DATA4
FOUR DC F'4'
FIVE DC F'5'
BASE EQU 8
TEMP DS 1F
DROP 8
END
 */