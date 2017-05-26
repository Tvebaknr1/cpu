package dk.cphbusiness.virtualcpu;

import java.util.Iterator;

public class Program implements Iterable<Integer>
{

    private String[] lines;

    public Program(String... lines)
    {
        this.lines = lines;
    }

    public int get(int index)
    {
        
        String line = lines[index];
        if (line.charAt(0) == '0' || line.charAt(0) == '1')
        {
            return Integer.parseInt(line, 2);
        } else if (line.startsWith("NOP"))
        {
            return 0b0000_0000;
        } else if (line.startsWith("ADD"))
        {
            return 0b0000_0001;
        } else if (line.startsWith("MUL"))
        {
            return 0b0000_0010;
        } else if (line.startsWith("DIV"))
        {
            return 0b0000_0011;
        } else if (line.startsWith("ZERO"))
        {
            return 0b0000_0100;
        } else if (line.startsWith("NEG"))
        {
            return 0b0000_0101;
        } else if (line.startsWith("POS"))
        {
            return 0b0000_0110;
        } else if (line.startsWith("NZERO"))
        {
            return 0b0000_0111;
        } else if (line.startsWith("EQ"))
        {
            return 0b0000_1000;
        } else if (line.startsWith("LT"))
        {
            return 0b0000_1001;
        } else if (line.startsWith("GT"))
        {
            return 0b0000_1010;
        } else if (line.startsWith("NEQ"))
        {
            return 0b0000_1011;
        } else if (line.startsWith("ALWAYS"))
        {
            return 0b0000_1100;
        } else if (line.startsWith("HALT"))
        {
            return 0b0000_1111;
        } else if (line.startsWith("INC"))
        {
            return 0b0001_0110;
        } else if (line.startsWith("DEC"))
        {
            return 0b0001_0111;
        } else if (line.startsWith("PUSH "))
        {
            String[] parts = line.split(" ");
            int r = parts[1].equals("B") ? 1 : 0;
            return 0b0001_0000 | r;
        } else if (line.startsWith("POP "))
        {
            String[] parts = line.split(" ");
            int r = parts[1].equals("B") ? 1 : 0;
            return 0b0001_0010 | r;
        } else if (line.startsWith("MOV "))
        {
            //need a b mov and o r mov
            // A B
            if (line.equals("MOV A B"))
            {
                return 0b0001_0100;
            } // B A
            else if (line.equals("MOV B A"))
            {
                return 0b0001_0101;
            } else
            {
                String[] parts = line.split(" ");
                // R o   ( r er 0 eller 1 som er svarende til a og b) (o er et tal fra 0 - 7 med et + foran fx +0)
                if ((parts[1].equals("A") || parts[1].equals("B")) && parts[2].startsWith("+"))
                {
                    int r = parts[1].equals("B") ? 1 : 0;
                    int o = Integer.parseInt(parts[2].substring(1));
                    return 0b0010_0000 | (r << 3) | o;
                } // o R
                else if ((parts[2].equals("A") || parts[2].equals("B")) && parts[1].startsWith("+"))
                {
                    int r = parts[2].equals("B") ? 1 : 0;
                    int o = Integer.parseInt(parts[1].substring(1));
                    return 0b0011_0000 | r | (o << 1);
                } // v r   (v er -16 - 15
                else if (parts[2].equals("A") || parts[2].equals("B"))
                {
                    int v;
                    try
                    {

                        if (parts[1].startsWith("-"))
                        {
                            v = 0 - Integer.parseInt(parts[1].substring(1));
                        } else
                        {
                            v = Integer.parseInt(parts[1])+16;
                        }
                        int r = (parts[2].equals("A") ? 0 : 1);
                        return 0b0100_0000 | (v << 1) | r;
                    } catch (Exception ex)
                    {

                    }

                }

            }

        } else if (line.startsWith("RTN "))
        {

            String[] parts = line.split(" ");
            int o = Integer.parseInt(parts[1].substring(1));;

            return 0b0001_1000 | o;
        } else if (line.startsWith("JMP "))
        {

            String[] parts = line.split(" ");
            int a = Integer.parseInt(parts[1]);

            return 0b1000_0000 | a;
        } else if (line.startsWith("CALL "))
        {

            String[] parts = line.split(" ");
            int a = Integer.parseInt(parts[1].substring(1));

            return 0b1100_0000 | a;
        } else
        {
            throw new UnsupportedOperationException("Don't know " + line);
        }
        return 0b0000_0000;
    }

    public String toBite(int V)
    {

        String bite = (V < 0 ? "0" : "1");

        return bite;
    }

    @Override
    public Iterator<Integer> iterator()
    {
        return new ProgramIterator();
    }

    private class ProgramIterator implements Iterator<Integer>
    {

        private int current = 0;

        @Override
        public boolean hasNext()
        {
            return current < lines.length;

        }

        @Override
        public Integer next()
        {
            return get(current++);
        }

    }

}
