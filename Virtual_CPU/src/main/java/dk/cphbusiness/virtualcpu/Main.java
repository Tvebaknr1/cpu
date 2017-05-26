package dk.cphbusiness.virtualcpu;

import java.util.Iterator;

public class Main
{

    public static void main(String[] args)
    {
        System.out.println("Welcome to the awesome CPU program");
        //Program program = new Program("00101001", "00001111", "10101010", "MOV B +3");
        Program program1 = new Program(
                "MOV 5 A", "PUSH A", "ALWAYS", "CALL #6", "POP A", "HALT", "MOV +1 A", "NZERO", "JMP 12", "MOV 1 A", "MOV A +1", "RTN +0", "PUSH A", "DEC",
                "PUSH A", "ALWAYS", "CALL #6", "POP B", "POP A", "MUL", "MOV A +1", "RTN +0");
        Machine machine = new Machine();
        machine.load(program1);
        machine.print(System.out);
        
        while(machine.tick()){
            machine.print(System.out);
        }
        
       

       

    }

}
