import com.sun.jdi.connect.*;
import com.sun.jdi.request.*;
import com.sun.jdi.event.*;
import java.util.Map;
import java.util.Vector;

import com.sun.jdi.*;

public class VMPreparation {
    public static void setDebuggerInstance() {
        Debugger.debuggerInstance.setDebugClass(Debuggee.class);
    }

    public static void enableClassPrepareRequest() {

        for (String classname : Variables.classname_map.keySet()) {
            ClassPrepareRequest classPrepareRequest = Variables.vm.eventRequestManager().createClassPrepareRequest();
            classPrepareRequest.addClassFilter(classname);
            System.out.println("enbale className: " + classname);
            classPrepareRequest.enable();
        }
    }

    public static void enableThreadStartRequest() {
        ThreadStartRequest tsr = Variables.vm.eventRequestManager().createThreadStartRequest();
        tsr.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
        tsr.enable();
    }

    public static void enableThreadDeathRequest() {
        ThreadDeathRequest tdr = Variables.vm.eventRequestManager().createThreadDeathRequest();
        tdr.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
        tdr.enable();
    }

    public static VirtualMachine connectAndLaunchVM() throws Exception {
        LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager().defaultConnector();
        Map<String, Connector.Argument> arguments = launchingConnector.defaultArguments();
        arguments.get("main").setValue(Debugger.debuggerInstance.debugClass.getName());
        System.out.println("Debuggee:" + Debugger.debuggerInstance.debugClass.getName());
        return launchingConnector.launch(arguments);

    }

    public static void setBreakPoints(ClassPrepareEvent event) throws AbsentInformationException {
        ClassType classType = (ClassType) event.referenceType();
        Vector<Integer> newBreakPointLines = new Vector<Integer>();
        for (int linenum : Variables.classname_map.get(classType.name())) {
            newBreakPointLines.add(linenum);
        }
        for (int lineNumber : newBreakPointLines) {
            Location location = classType.locationsOfLine(lineNumber).get(0);
            BreakpointRequest bpReq = Variables.vm.eventRequestManager().createBreakpointRequest(location);
            bpReq.setSuspendPolicy(EventRequest.SUSPEND_ALL);
            bpReq.enable();
        }
    }

    public static void setVM() {
        try {
            Variables.vm = connectAndLaunchVM();
            enableClassPrepareRequest();
            enableThreadStartRequest();
            enableThreadDeathRequest();

            // enableThreadStartRequest(Variables.vm);
            // enableThreadDeathRequest(Variables.vm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
