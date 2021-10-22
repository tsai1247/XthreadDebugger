import java.io.*;
import java.util.*;
import com.sun.jdi.*;

public class IO {
    static Scanner input = new Scanner(System.in);
    public static void getInput() {
        /*
         * input format: classname line_number break_point_type
         * end of input: End of file or "eof"
         * for example:
            * classname1 8 1
            * classname2 20 2
            * classname2 25 2
            * debuggeeclass 10 1
            * debuggeeclass2 12 3
            * end of file
         */

        Log.LogAppend("start");
        System.out.println("Please input steps:");
        
        String input_classname, input_threadname, tmp;                          // tow possible input: 
        int input_linenum, input_type;                                          // input_classname input_linenum input_type
                                                                                // input_classname input_threadname
        while (input.hasNext()) 
        {
            input_classname = input.next();
            if (input_classname.toLowerCase().equals("eof"))
                break;

            tmp = input.next();
            
            if (!tmp.chars().allMatch(Character::isDigit))              // format: ClassName ThreadName
            {
                input_threadname = tmp;
                if (!Variables.TRP_table.containsKey(input_threadname)) {
                    Variables.TRP_table.put(input_threadname, null);
                    Variables.classname_map.put(input_classname, new HashSet<Integer>());
                }
            }
            else                                                              // format: ClassName LineNumber BreakPointType
            {
                input_linenum = Integer.valueOf(tmp);
                input_type = input.nextInt();

                if (!Variables.linenum_to_classname.containsKey(input_linenum))
                {
                    Variables.linenum_to_classname.put(input_linenum, input_classname);
                    Variables.linenum_to_typenum.put(input_linenum, input_type);
                    Variables.classname_map.get(input_classname).add(input_linenum);
                }
                
            }

        }
    }

    public static void printBuffer() {
        try {
            InputStreamReader reader = new InputStreamReader(Variables.vm.process().getInputStream());
            OutputStreamWriter writer = new OutputStreamWriter(System.out);
            char[] buf = new char[512];
            reader.read(buf);
            writer.write(buf);
            writer.flush();

        } catch (IOException ioe) {}
    }

    public static void printAllthreadStatus(VirtualMachine vm, String title, ThreadReference tr) {
        System.out.println("-" + title + "-");
        // System.out.println(">> threadcount " + threadcnt.get(tr.name()));
        for (Map.Entry<String, ThreadReference> entry : Variables.TRP_table.entrySet()) {
            System.out.print(entry.getKey() + ": ");
            if (entry.getValue().isSuspended()) {
                System.out.println("Suspend");
            } else {
                System.out.println("Resume");
            }

        }
        // System.out.println("---------------");
        // System.out.println(">> Queue Size: " + threadSteps.size());
        // System.out.println("---------------\n");
    }
}

// Excetption after line 49
/*
                else                                                        // Exception
                {
                    String comfirm = "";
                    while (comfirm.equals("")) {
                        System.out.println("Input Data \"" + input_classname + " " + input_linenum + " " + input_type
                                + "\" " + "exists, do you want to overwrite it? (Y/N)");
                        comfirm = input.next();
                        comfirm = comfirm.toLowerCase();
                        if (comfirm.equals("y")) {
                            if (debuggerInstance.classname_map.keySet().contains(input_classname)) {
                                debuggerInstance.classname_map
                                        .get(debuggerInstance.linenum_to_classname.get(input_linenum))
                                        .remove(input_linenum);
                                debuggerInstance.classname_map.get(input_classname).add(input_linenum);
                            } else
                                debuggerInstance.classname_map.put(input_classname,
                                        new HashSet<Integer>(input_linenum));

                            debuggerInstance.linenum_to_classname.put(input_linenum, input_classname);
                            debuggerInstance.linenum_to_typenum.put(input_linenum, input_type);
                        } else if (!comfirm.equals("n")) {
                            comfirm = "";
                        }
                    }
                }
                */
