package com.company;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
//On my honor, I have neither given nor received any unauthorized aid on this assignment.
public class Vsim {
    static int fakePc = 256;
    static int pc = 256;
    static int[] reg = new int[32];
    static int[] mem = new int[8192];
    static ArrayList<String> instructions = new ArrayList<>();
    static FileWriter disassemblyWriter;
    static FileWriter simulationWriter;
    static boolean breakBool = false;
    static HashMap<Integer, String> hashInstructions = new HashMap<>();
    static int cycle = 0;
    static boolean notBreak = true;
    static int startMemVal;

    public static void main(String[] args) throws IOException {
        String inputFileName = args[0];

        //write a for loop  to read the file and store the instructions in an arraylist


        




        loadData(inputFileName);
        String filePath = "disassembly.txt";


        disassemblyWriter = new FileWriter(filePath, true);
        simulationWriter = new FileWriter("simulation.txt", true);


        int memVal = fakePc + 4;
        for (int i = 0; i < instructions.size(); i++) {
            if (breakBool) {
                if (instructions.get(i).charAt(0) == '1') {
                    long val = getSignedLongValue(instructions.get(i));
                    int intval = (int) val;
                    disassemblyWriter.write(instructions.get(i) + " " + fakePc + " " + val + "\n");
                    mem[memVal] = intval;
                    memVal += 4;
                } else {
                    disassemblyWriter.write(instructions.get(i) + " " + fakePc + " " + Integer.parseInt(instructions.get(i), 2) + "\n");
                    mem[memVal] = Integer.parseInt(instructions.get(i), 2);
                    memVal += 4;
                }
                fakePc += 4;

            } else {
                int instr = fakePc;

                String instruction = instructions.get(i);
                String cat = instruction.substring(30, 32);
                String opcode = instruction.substring(25, 30);

                int rd;
                int rs2;
                int rs1;
                int funct3;
                int imm;
                int imm2;
                switch (cat) {

                    case "00":
                        imm = Integer.parseInt(instruction.substring(0, 7), 2);
                        rs2 = Integer.parseInt(instruction.substring(7, 12), 2);
                        rs1 = Integer.parseInt(instruction.substring(12, 17), 2);
                        imm2 = Integer.parseInt(instruction.substring(20, 25), 2);


                        int concat = (imm << 5) | imm2;
                        int shiftedOffset = concat << 1;

                        switch (opcode) {
                            case "00000":
                                //beq
                                disassemblyWriter.write(instruction + " " + fakePc + " beq " + "x" + rs1 + ", " + "x" + rs2 + ", " + "#" + concat + "\n");
                                if (reg[rs1] == reg[rs2]) {

                                }
                                break;
                            case "00001":
                                disassemblyWriter.write(instruction + " " + fakePc + " bne " + "x" + rs1 + ", " + "x" + rs2 + ", " + "#" + concat + "\n");
                                if (reg[rs1] != reg[rs2]) {

                                }
                                //bne
                                break;
                            case "00010":
                                //blt
                                disassemblyWriter.write(instruction + " " + fakePc + " blt " + "x" + rs1 + ", " + "x" + rs2 + ", " + "#" + concat + "\n");
                                if (reg[rs1] < reg[rs2]) {

                                }
                                break;
                            case "00011":

                                disassemblyWriter.write(instruction + " " + fakePc + " sw " + "x" + rs1 + ", " + concat + "(x" + rs2 + ")" + "\n");

                                break;
                        }
                        break;
                    case "01":

                        rd = Integer.parseInt(instruction.substring(20, 25), 2);
                        rs2 = Integer.parseInt(instruction.substring(7, 12), 2);
                        rs1 = Integer.parseInt(instruction.substring(12, 17), 2);

                        switch (opcode) {
                            //add
                            case "00000":

                                disassemblyWriter.write(instruction + " " + fakePc + " add " + "x" + rd + ", " + "x" + rs1 + ", " + "x" + rs2 + "\n");

                                break;
                            //sub
                            case "00001":


                                disassemblyWriter.write(instruction + " " + fakePc + " sub " + "x" + rd + ", " + "x" + rs1 + ", " + "x" + rs2 + "\n");

                                break;
                            //and
                            case "00010":
                                // CHEEECK THIS
                                //reg[rd] = (reg[rs1] & reg[rs2]);
                                disassemblyWriter.write(instruction + " " + fakePc + " and " + "x" + rd + ", " + "x" + rs1 + ", " + "x" + rs2 + "\n");
                                break;

                            //or
                            case "00011":
                               // reg[rd] = (reg[rs1] | reg[rs2]);
                                disassemblyWriter.write(instruction + " " + fakePc + " or " + "x" + rd + ", " + "x" + rs1 + ", " + "x" + rs2 + "\n");
                                break;
                        }
                        break;

                    case "10":
                        rd = Integer.parseInt(instruction.substring(20, 25), 2);
                        imm = Integer.parseInt(instruction.substring(0, 12), 2);
                        rs1 = Integer.parseInt(instruction.substring(12, 17), 2);

                        if (instruction.charAt(0) == '1') {
                            imm = getSignedValue(instruction.substring(0, 12));
                        }


                        switch (opcode) {
                            case "00000":
                                //addi
                                //reg[rd] = reg[rs1] + imm;
                                disassemblyWriter.write(instruction + " " + fakePc + " addi " + "x" + rd + ", " + "x" + rs1 + ", " + "#" + imm + "\n");
                                break;
                            case "00001":
                                //andi
                                //reg[rd] = reg[rs1] & imm;
                                disassemblyWriter.write(instruction + " " + fakePc + " andi " + "x" + rd + ", " + "x" + rs1 + ", " + "#" + imm + "\n");
                                break;
                            case "00010":
                                //ori
                                //reg[rd] = reg[rs1] | imm;
                                disassemblyWriter.write(instruction + " " + fakePc + " ori " + "x" + rd + ", " + "x" + rs1 + ", " + "#" + imm + "\n");
                                break;
                            case "00011":
                                //sll
                                //reg[rd] = reg[rs1] << imm;
                                disassemblyWriter.write(instruction + " " + fakePc + " sll " + "x" + rd + ", " + "x" + rs1 + ", " + "#" + imm + "\n");
                                break;
                            case "00100":
                                //sra

                               // reg[rd] = reg[rs1] >> imm;
                                disassemblyWriter.write(instruction + " " + fakePc + " sra " + "x" + rd + ", " + "x" + rs1 + ", " + "#" + imm + "\n");
                                break;
                            case "00101":
                                // cheeeck this
                                //lw
                                //reg[rd] = mem[(reg[rs1] + imm)];
                                disassemblyWriter.write(instruction + " " + fakePc + " lw " + "x" + rd + ", " + imm + "(x" + rs1 + ")\n");
                                break;
                        }
                        break;

                    case "11":
                        imm = Integer.parseInt(instruction.substring(0, 20), 2);
                        rd = Integer.parseInt(instruction.substring(20, 25), 2);
                        if (instruction.charAt(0) == '1') {
                            imm = getSignedValue(instruction.substring(0, 20));
                        }
                        switch (opcode) {
                            case "00000":

                                //jal
                                //left shift imm by 1 bit
                                disassemblyWriter.write(instruction + " " + fakePc + " jal " + "x" + rd + ", " + "#" + imm + "\n");
                                imm = imm << 1;
                                // pc = pc + imm;

                                break;
                            case "11111":
                                disassemblyWriter.write(instruction + " " + fakePc + " break" + "\n");
                                breakBool = true;
                                memVal = fakePc + 4;
                                startMemVal = fakePc + 4;
                                break;

                        }
                        break;
                }


                fakePc += 4;

            }
        }

            String instruction = null;

            while(notBreak){


                        // assign cat to the last two bits of lines[i]
                instruction = hashInstructions.get(pc);



                        String cat = instruction.substring(30, 32);
                        String opcode = instruction.substring(25, 30);

                        int rd;
                        int rs2;
                        int rs1;
                        int funct3;
                        int imm;
                        int imm2;
                        switch (cat) {

                            case "00":
                                imm = Integer.parseInt(instruction.substring(0, 7), 2);
                                rs2 = Integer.parseInt(instruction.substring(7, 12), 2);
                                rs1 = Integer.parseInt(instruction.substring(12, 17), 2);
                                imm2 = Integer.parseInt(instruction.substring(20, 25), 2);


                                int concat = (imm << 5) | imm2;
                                int shiftedOffset = concat << 1;
                                // sign extend shiftedOffset from 12 bit to 32 bit

                                switch (opcode) {
                                    case "00000":
                                        //beq
                                        fileWrite(" beq " + "x" + rs1 + ", " + "x" + rs2 + ", " + "#" + concat + "\n");
                                        if (reg[rs1] == reg[rs2]) {
                                            pc += shiftedOffset;
                                            pc -= 4;
                                        }
                                        break;
                                    case "00001":
                                        fileWrite(" bne " + "x" + rs1 + ", " + "x" + rs2 + ", " + "#" + concat + "\n");
                                        if (reg[rs1] != reg[rs2]) {
                                            pc += shiftedOffset;
                                            pc -= 4;
                                        }

                                        //bne
                                        break;
                                    case "00010":
                                        //blt
                                        fileWrite(" blt " + "x" + rs1 + ", " + "x" + rs2 + ", " + "#" + concat + "\n");
                                        if (reg[rs1] < reg[rs2]) {
                                            pc += shiftedOffset;
                                            pc -= 4;
                                        }
                                        break;

                                    case "00011":

                                        int memPlace = concat + reg[rs2];
                                        mem[memPlace] = reg[rs1];
                                        fileWrite(" sw " + "x" + rs1 + ", " + concat + "(x" + rs2 + ")" + "\n");

                                        //sw
                                        break;
                                }
                                break;
                            case "01":

                                rd = Integer.parseInt(instruction.substring(20, 25), 2);
                                rs2 = Integer.parseInt(instruction.substring(7, 12), 2);
                                rs1 = Integer.parseInt(instruction.substring(12, 17), 2);

                                switch (opcode) {
                                    //add
                                    case "00000":
                                        reg[rd] = reg[rs1] + reg[rs2];
                                        fileWrite(" add " + "x" + rd + ", " + "x" + rs1 + ", " + "x" + rs2 + "\n");
                                        break;
                                    //sub
                                    case "00001":
                                        reg[rd] = reg[rs1] - reg[rs2];
                                        fileWrite(" sub " + "x" + rd + ", " + "x" + rs1 + ", " + "x" + rs2 + "\n");
                                        break;
                                    //and
                                    case "00010":
                                        // CHEEECK THIS

                                        reg[rd] = (reg[rs1] & reg[rs2]);
                                        fileWrite(" and " + "x" + rd + ", " + "x" + rs1 + ", " + "x" + rs2 + "\n");
                                        break;

                                    //or
                                    case "00011":

                                        reg[rd] = (reg[rs1] | reg[rs2]);
                                        fileWrite(" or " + "x" + rd + ", " + "x" + rs1 + ", " + "x" + rs2 + "\n");
                                        break;
                                }
                                break;

                            case "10":
                                rd = Integer.parseInt(instruction.substring(20, 25), 2);
                                imm = Integer.parseInt(instruction.substring(0, 12), 2);
                                rs1 = Integer.parseInt(instruction.substring(12, 17), 2);

                                if (instruction.charAt(0) == '1') {
                                    imm = getSignedValue(instruction.substring(0, 12));
                                }


                                switch (opcode) {
                                    case "00000":
                                        //addi
                                        reg[rd] = reg[rs1] + imm;
                                        fileWrite(" addi " + "x" + rd + ", " + "x" + rs1 + ", " + "#" + imm + "\n");
                                        break;
                                    case "00001":
                                        //andi
                                        reg[rd] = reg[rs1] & imm;
                                        fileWrite(" andi " + "x" + rd + ", " + "x" + rs1 + ", " + "#" + imm + "\n");
                                        break;
                                    case "00010":
                                        //ori
                                        reg[rd] = reg[rs1] | imm;
                                        fileWrite(" ori " + "x" + rd + ", " + "x" + rs1 + ", " + "#" + imm + "\n");
                                        break;
                                    case "00011":
                                        //sll
                                        reg[rd] = reg[rs1] << imm;

                                       fileWrite(" sll " + "x" + rd + ", " + "x" + rs1 + ", " + "#" + imm + "\n");
                                        break;
                                    case "00100":
                                        //sra

                                        reg[rd] = reg[rs1] >> imm;
                                        fileWrite(" sra " + "x" + rd + ", " + "x" + rs1 + ", " + "#" + imm + "\n");

                                        break;
                                    case "00101":
                                        // cheeeck this
                                        //lw
                                        reg[rd] = mem[(reg[rs1] + imm)];
                                        fileWrite(" lw " + "x" + rd + ", " + imm + "(x" + rs1 + ")" + "\n");

                                        break;
                                }
                                break;

                            case "11":
                                imm = Integer.parseInt(instruction.substring(0, 20), 2);
                                rd = Integer.parseInt(instruction.substring(20, 25), 2);
                                if (instruction.charAt(0) == '1') {
                                    imm = getSignedValue(instruction.substring(0, 20));
                                }

                                switch (opcode) {
                                    case "00000":
                                        reg[rd] = pc + 4;
                                        fileWrite( "jal "+ "x" + rd + ", " + "#" + imm + "\n");

                                        imm*=2;
                                        pc = pc + imm;
                                        pc-=4;

                                        break;
                                    case "11111":
                                        fileWrite("break \n");
                                        notBreak = false;
                                        break;

                                }
                                break;
                        }

                        pc +=4;
                        cycle++;
                    }
        simulationWriter.close();
        disassemblyWriter.close();
        }
        // Close the FileWriter to save changes





    static int cycles = 1;

    public static void fileWrite(String ins) throws IOException {

        simulationWriter.write("--------------------\n");
        simulationWriter.write("Cycle " + cycles + ": " + pc + " " + ins );
        simulationWriter.write("Registers \n");

        simulationWriter.write("x00:"+ "\t");
        for (int i = 0; i < 8; i++){
            simulationWriter.write(reg[i] + "\t");
        }

        simulationWriter.write("\n");
        simulationWriter.write("x08:"+ "\t");
        for (int i = 8; i < 16; i++){
            simulationWriter.write(reg[i] + "\t");
        }
        simulationWriter.write("\n");
        simulationWriter.write("x16:"+ "\t");
        for (int i = 16; i < 24; i++){
            simulationWriter.write(reg[i] + "\t");
        }
        simulationWriter.write("\n");
        simulationWriter.write("x24:"+ "\t");
        for (int i = 16; i < 24; i++){
            simulationWriter.write(reg[i] + "\t");
        }
        simulationWriter.write("\n");

        simulationWriter.write("Data\n");
        simulationWriter.write(startMemVal + ":" + "\t");
        for (int dataIndex = startMemVal; dataIndex < fakePc; dataIndex += 4) {

            simulationWriter.write("\t" + "\t" + mem[dataIndex]);
            if (dataIndex == 340) {
                simulationWriter.write("\n");
                simulationWriter.write("344:" + "\t");

            }
        }
        simulationWriter.write("\n");
        // Increment the cycle counter
        cycles++;

    }


    public static int getSignedValue(String s) {
        StringBuilder si = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if ('1' == s.charAt(i)) {
                si.append("0");
            } else {
                si.append("1");
            }

        }


        return -(Integer.parseInt(String.valueOf(si), 2) + 1);
    }
    public static long getSignedLongValue(String s) {
        StringBuilder si = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if ('1' == s.charAt(i)) {
                si.append("0");
            } else {
                si.append("1");
            }


        }

        return -(Long.parseLong(String.valueOf(si), 2) + 1);
    }

    public static void loadData(String filename) {



try{

            File file = new File(filename);


            FileReader fileReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);






            String line;
            int key = 256;
            while ((line = bufferedReader.readLine()) != null) {
                instructions.add(line);
                hashInstructions.put(key, line);
                key += 4;
            }
        bufferedReader.close();
        fileReader.close();
    } catch (IOException e) {
        e.printStackTrace();
    }




    }
}


