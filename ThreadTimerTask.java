import java.util.TimerTask;
import com.sun.jdi.event.*;

class ThreadTimerTask extends TimerTask {
    BreakpointEvent EventObserver;

    ThreadTimerTask(BreakpointEvent event) {
        EventObserver = event;
    }

    @Override
    public void run() {
        try {
            if (EventObserver.thread().isSuspended()) {
                System.out.println("----Thread " + EventObserver.thread().name() + " ended normally----");
            } else {
                System.out.println("----Thread " + EventObserver.thread().name() + " time out----");
            }
        } catch (Exception e) {
            System.out.println("----Time out, VM is already closed.----");
        }
    }
}