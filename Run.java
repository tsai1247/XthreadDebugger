import java.util.*;
import java.lang.String;
import com.sun.jdi.*;
import com.sun.jdi.event.*;

public class Run {

    Scanner input = new Scanner(System.in);

    public void doBlockingAssertionPoint(BreakpointEvent event) {
        String thread_name = Variables.thread_name_table.get(event.thread());
        if (Variables.TRP_table.get(thread_name) == null) {
            System.out.println("This thread isn't marked");
        } else {
            System.out.println("----Thread " + thread_name + " start----");
            Timer timer = new Timer();
            timer.schedule(new ThreadTimerTask(event), 1000);

        }
    }

    public void doContextSwitchPoint(BreakpointEvent event) {
        for (ThreadReference cur_thread : Variables.TRP_table.values()) {
            if (!cur_thread.isSuspended())
                cur_thread.suspend();
        }
        String thread_name = Variables.thread_name_table.get(event.thread());
        System.out.println("thread:" + thread_name + " do CSP");
        IO.printAllthreadStatus(Variables.vm, "ALL THREADS IN TRP", event.thread());
        System.out.println("Please select a thread to continue:");
        // for (Map.Entry<String, ThreadReference> entry : TRP_table.entrySet())
        // // System.out.println("Key = " + entry.getKey() + ", Value = " +
        // // entry.getValue());
        // System.out.println(entry.getKey());
        String selected_thread = input.next();

        System.out.println();
        for (ThreadReference cur_thread : Variables.TRP_table.values()) {
            if (Variables.thread_name_table.get(cur_thread).equals(selected_thread) && cur_thread.isSuspended()) {
                cur_thread.resume();
                // System.out.println(cur_thread.isSuspended());
                IO.printAllthreadStatus(Variables.vm, cur_thread.name() + " resume", event.thread());
                // IO.printBuffer();
                System.out.println();
            }
        }

    }

    public void doNamingPoint(BreakpointEvent event) {
        String thread_name = Variables.thread_name_table.get(event.thread());
        if (thread_name == null) {
            thread_name = event.thread().name();
        }
        System.out.println("Current thread name: " + thread_name);
        System.out.println("Change the thread name to: ");
        String new_name = input.next();
        System.out.println();
        Variables.thread_name_table.put(event.thread(), new_name);
        if (Variables.TRP_table.containsKey(thread_name)) {
            ThreadReference obj = Variables.TRP_table.remove(thread_name);
            Variables.TRP_table.put(new_name, obj);
        }
    }


    public void doCorrespondEvent()
            throws IncompatibleThreadStateException, AbsentInformationException, InterruptedException {
        EventSet eventSet = null;
        while ((eventSet = Variables.vm.eventQueue().remove()) != null) {
            for (Event event : eventSet) {
                if (event instanceof ClassPrepareEvent) {
                    VMPreparation.setBreakPoints((ClassPrepareEvent) event);
                }
                if (event instanceof BreakpointEvent) {
                    // System.out.println("check vm.allthreads() in bp event");
                    // for (ThreadReference cur_thread : vm.allThreads()) {
                    // System.out.println(cur_thread.name());
                    // }
                    // debuggerInstance.enableStepRequest(vm, (BreakpointEvent) event);
                    LocatableEvent curEvent = (LocatableEvent) event;
                    if (Variables.linenum_to_classname.containsKey(curEvent.location().lineNumber())) {

                        int curType = Variables.linenum_to_typenum.get(curEvent.location().lineNumber());
                        switch (curType) {
                            case 1:
                                // TODO: NMP

                                System.out.println("----catch NMP----");
                                doNamingPoint((BreakpointEvent) event);
                                break;

                            case 2:
                                // TODO: CSP
                                /*
                                 * TargetThread 29 2 TargetThread 30 2 TargetThread 33 2 TargetThread 34 2
                                 * TargetThread 37 2 TargetThread 38 2
                                 */
                                System.out.println("----catch CSP at line:" + curEvent.location().lineNumber() + "----");
                                doContextSwitchPoint((BreakpointEvent) event);
                                // System.out.println("TEST");
                                break;

                            case 3:
                                /*
                                 * test input for Eric_Chao: TargetThread 29 3 TargetThread 33 3 TargetThread 37
                                 * 3 eof
                                 */
                                // System.out.println("----catch TRP----");
                                // doThreadRegistrationPoint((BreakpointEvent) event);
                                // break;

                            case 4:
                                // TODO: BAP
                                doBlockingAssertionPoint((BreakpointEvent) event);
                                break;
                            default:
                                // error
                                break;
                        }
                    }
                }
                if (event instanceof ThreadStartEvent) {
                    ThreadStartEvent curEvent = (ThreadStartEvent) event;
                    // System.out.println("ThreadStartEvent:" + curEvent.thread().name());
                    if (Variables.TRP_table.containsKey(curEvent.thread().name())) {
                        System.out.println("----catch TRP----");
                        Variables.TRP_table.put(curEvent.thread().name(), curEvent.thread());
                        System.out.println("thread \"" + curEvent.thread().name() + "\" registered sucessful.");
                    }
                    if (!Variables.thread_name_table.containsKey(curEvent.thread())) {
                        Variables.thread_name_table.put(curEvent.thread(), curEvent.thread().name());
                    }
                }

                if (event instanceof ThreadDeathEvent) {
                    ThreadDeathEvent curEvent = (ThreadDeathEvent) event;
                    // System.out.println("ThreadDeathEvent:" + curEvent.thread().name());
                }
                if (event instanceof StepEvent) {

                }

                Variables.vm.resume();
            }
        }
        input.close();
    }
}
