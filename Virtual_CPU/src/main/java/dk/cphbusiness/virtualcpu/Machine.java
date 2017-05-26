package dk.cphbusiness.virtualcpu;

import java.io.PrintStream;

public class Machine
{

    private Cpu cpu = new Cpu();
    private Memory memory = new Memory();

    public void load(Program program)
    {
        int index = 0;
        for (int instr : program)
        {
            memory.set(index++, instr);
        }
    }

    public boolean tick()
    {
        int instr = memory.get(cpu.getIp());
        if (instr == 0b0000_0000)
        {
            // 0000 0000  NOP
            cpu.incIp();
            
        } else if (instr == 0b0000_0001)
        {
            // 0000 0001 ADD A B
            cpu.setA(cpu.getA() + cpu.getB());
            cpu.incIp();
        } else if (instr == 0b0000_0010)
        {
            // 0000 0001 ADD A B
            cpu.setA(cpu.getA() * cpu.getB());
            cpu.incIp();
        } else if (instr == 0b0000_0011)
        {
            // 0000 0001 ADD A B
            cpu.setA(cpu.getA() / cpu.getB());
            cpu.incIp();
        } else if (instr == 0b0000_0100)
        {
            // 0000 0001 ADD A B
            cpu.setFlag(cpu.getA() == 0);
            cpu.incIp();
        } else if (instr == 0b0000_0101)
        {
            // 0000 0001 ADD A B
            cpu.setFlag(cpu.getA() < 0);
            cpu.incIp();
        } else if (instr == 0b0000_0110)
        {
            // 0000 0001 ADD A B
            cpu.setFlag(cpu.getA() > 0);
            cpu.incIp();
        } else if (instr == 0b0000_0111)
        {
            System.out.println("Nzero");
            
            cpu.setFlag(cpu.getA() != 0);
            cpu.incIp();
        } else if (instr == 0b0000_1000)
        {
            // 0000 0001 ADD A B
            cpu.setFlag(cpu.getA() == cpu.getB());
            cpu.setIp(cpu.getIp() + 1);
        } else if (instr == 0b0000_1001)
        {
            // 0000 0001 ADD A B
            cpu.setFlag(cpu.getA() < cpu.getB());
            cpu.incIp();
        } else if (instr == 0b0000_1010)
        {
            // 0000 0001 ADD A B
            cpu.setFlag(cpu.getA() > cpu.getB());
            cpu.incIp();
        } else if (instr == 0b0000_1011)
        {
            // 0000 0001 ADD A B
            cpu.setFlag(cpu.getA() != cpu.getB());
            cpu.incIp();
        } else if (instr == 0b0000_1100)
        {
            System.out.println("always");
            // 0000 0001 ADD A B
            cpu.setFlag(true);
            cpu.incIp();
        } else if (instr == 0b0000_1101)
        {
            return false;
        } else if (instr == 0b0000_1111)
        {
           return false;
        } else if ((instr & 0b1111_1110) == 0b0001_0000)
        {
            System.out.println("push ");
            cpu.setSp(cpu.getSp() - 1);
            cpu.incIp();
            if ((instr & 0b0000_0001) == 0b0000_0000)
            {
                memory.set(cpu.getSp(), cpu.getA());
            } else
            {
                memory.set(cpu.getSp(), cpu.getB());
            }
        } else if ((instr & 0b1111_1110) == 0b0001_0010)
        {
            System.out.println("pop");
            
            if ((instr & 0b0000_0001) == 0b0000_0000)
            {
                cpu.setA(memory.get(cpu.getSp()));
            } else
            {
                cpu.setB(memory.get(cpu.getSp()));
            }
            cpu.setSp(cpu.getSp() + 1);
            cpu.incIp();
        } else if (instr == 0b0001_0100)
        {
            cpu.incIp();
            cpu.setB(cpu.getA());
        } else if (instr == 0b0001_0101)
        {
            cpu.incIp();
            cpu.setA(cpu.getB());
        } else if (instr == 0b0001_0110)
        {
            cpu.incIp();
            cpu.setA(cpu.getA() + 1);
        } else if (instr == 0b0001_0111)
        {
            cpu.incIp();
            cpu.setA(cpu.getA() - 1);
        } else if ((instr & 0b1111_1000) == 0b0001_1000)
        {
            System.out.println("RTN");
            //IP ← [SP++]; SP += o; IP++
            int o = instr & 0b0000_0111;
            
            cpu.setIp(memory.get(cpu.getSp()));
            cpu.setSp(cpu.getSp() + o + 1);
            cpu.incIp();
        } else if ((instr & 0b1111_0000) == 0b0010_0000)
        {
            // 0010 r ooo	MOV r o	   [SP + o] ← r; IP++

            // 0010 1 011 MOV B (=1) +3  [SP +3] // Move register B to memory position of SP with offset 3
            // 00101011 finding instruction
            //    and
            // 11110000
            // --------
            // 00100000
            // 00101011 finding offset
            //    and
            // 00000111
            // --------
            // 00000011 = 3
            // 00101011 finding register
            //    and
            // 00001000
            // --------
            // 00001000 = 8
            //    >> 3
            // 00000001 = 1
            System.out.println("move registery offset");
            int o = instr & 0b0000_0111;
            int r = (instr & 0b0000_1000) >> 3;
            if (r == cpu.A)
            {
                memory.set(cpu.getSp() + o, cpu.getA());
                
            } else
            {
                memory.set(cpu.getSp() + o, cpu.getB());
            }
            cpu.incIp();
        } else if ((instr & 0b1111_0000) == 0b0011_0000)
        {
            //0011 ooor	MOV o r	r ← [SP + o]; IP++
            System.out.println("mov");
            int o = (instr & 0b0000_1110) >> 1;
            int r = (instr & 0b0000_1000) >> 3;
            System.out.println("o r : " + o + " " + r);
            if (r == cpu.A)
            {
                cpu.setA(memory.get(cpu.getSp()+o));
            } else
            {
                cpu.setB(memory.get(cpu.getSp()+o));
            }
            cpu.incIp();
        } else if ((instr & 0b1100_0000) == 0b0100_0000)
        {
            //01vv vvvr	MOV v r	r ← v; IP++
            int v = ((instr & 0b0011_1110) >> 1) - 16;
            System.out.println("MOV");
            System.out.println(v);
            System.out.println("v : " +v);
            int r = (instr & 0b0000_0001);
            System.out.println("r : " + r);
            System.out.println(cpu.A);
            if (r == cpu.A)
            {
                cpu.setA(v);
            } else
            {
                cpu.setB(v);
            }
            cpu.incIp();
        } else if ((instr & 0b1100_0000) == 0b1000_0000)
        {
            //10aa aaaa	JMP #a	if F then IP ← a else IP++
            int a = (instr & 0b0011_1111);
            if (cpu.isFlag())
            {
                cpu.setIp(a);
            } else
            {
                cpu.incIp();
            }
        } else if ((instr & 0b1100_0000) == 0b1100_0000)
        {
            //11aa aaaa	CALL #a	if F then [--SP] ← IP; IP ← a else IP++
            System.out.println("Call");
            int a = (instr & 0b0011_1111);
            System.out.println(a);
            if (cpu.isFlag())
            {
                cpu.setSp(cpu.getSp()-1);
                memory.set(cpu.getSp(),cpu.getIp() - 1);
                
                cpu.setIp(a);
            } else
            {
                cpu.incIp();
            }

        }
return true;
    }

    public void print(PrintStream out)
    {
        memory.print(out);
        out.println("-------------");
        cpu.print(out);
    }

}
