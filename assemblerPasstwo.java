import java.io.*;
public class assemblerPasstwo {
    public static void main(String[] args) throws Exception {
        BufferedReader inputReader = new BufferedReader(new FileReader("intermediate.txt"));
        BufferedReader symReader = new BufferedReader(new FileReader("SYMTAB.txt"));
        BufferedReader litReader = new BufferedReader(new FileReader("LITTAB.txt"));
        BufferedWriter outputWriter = new BufferedWriter(new FileWriter("machine_code.txt"));

        String inputLine;
        String symLine = null;
        String litLine = null;

        while ((inputLine = inputReader.readLine()) != null) {
            String[] tokens = inputLine.split("\\)\\(");
            StringBuilder outputLine = new StringBuilder();

            for (String token : tokens) {
                token = token.replaceAll("[\\(\\)]", "");

                if (token.startsWith("S,")) {
                    if (symLine == null) {
                        symLine = symReader.readLine();
                    }
                    if (symLine != null) {
                        String[] symTokens = symLine.split("\t");
                        if (symTokens.length > 1) {
                            int address = Integer.parseInt(symTokens[1]);
                            token = token.replace("S,", "");
                            token = getFormattedMachineCode(token, address);
                        }
                    }
                } else if (token.startsWith("L,")) {
                    if (litLine == null) {
                        litLine = litReader.readLine();
                    }
                    if (litLine != null) {
                        String[] litTokens = litLine.split("\t");
                        if (litTokens.length > 1) {
                            int address = Integer.parseInt(litTokens[1]);
                            token = token.replace("L,", "");
                            token = getFormattedMachineCode(token, address);
                        }
                    }
                }

                outputLine.append(token.replace(",", "")).append(" ");
            }
            outputLine = new StringBuilder(outputLine.toString().replaceAll("[A-Za-z]", ""));
            outputWriter.write(outputLine.toString().trim());
            outputWriter.newLine();
        }

        inputReader.close();
        symReader.close();
        litReader.close();
        outputWriter.close();
    }

    private static String getFormattedMachineCode(String instruction, int address) {
        String[] parts = instruction.split(",");
        String opcode = parts[0];
        String[] operands = parts.length > 1 ? parts[1].split(" ") : new String[0];

        String registerNumbers = "";
        for (String operand : operands) {
            int regIndex = Integer.parseInt(operand);
            registerNumbers += getRegisterNumber(regIndex);
        }

        return opcode + " " + registerNumbers + " " + address;
        
    }

    private static String getRegisterNumber(int index) {
        String[] regNumbers = {"01", "02", "03", "04"};
        if (index >= 0 && index < regNumbers.length) {
            return regNumbers[index];
        }
        return "";
    }
}

/*
input
INTERMEDIATE CODE:
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


OUTPUT
04 01 205
05 01 214
04 02 206
00 00 005
00 00 002
01 01 210
02 02 211
07 02 202
00 00 001
00 00 002 
03 03 215
00 00 000
00 00 004
 */