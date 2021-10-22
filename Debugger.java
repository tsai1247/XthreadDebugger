public class Debugger
{
    public static Debugger debuggerInstance = new Debugger();
    public Class debugClass;

    public void setDebugClass(Class<Debuggee> debugClass) {
        this.debugClass = debugClass;
    }

    
}

