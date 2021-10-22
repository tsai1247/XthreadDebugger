import java.util.*;
import com.sun.jdi.*;

public class Variables {
    public static VirtualMachine vm = null;
    public static Queue<Character> threadSteps = new LinkedList<>();
 
    
    public static Map<String, Integer> threadcnt = new HashMap<String, Integer>();
    public static Map<Integer, String> linenum_to_classname = new HashMap<>();
    public static Map<Integer, Integer> linenum_to_typenum = new HashMap<>();
    public static Map<String, Set<Integer>> classname_map = new HashMap<>();
    public static Map<ThreadReference, String> thread_name_table = new HashMap<>();
    public static Map<String, ThreadReference> TRP_table = new HashMap<>();
}
