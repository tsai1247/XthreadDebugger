import com.sun.jdi.*;



public class Main {
    public static void main(String[] args) throws Exception {
        
        IO.getInput();

        VMPreparation.setDebuggerInstance();

        VMPreparation.setVM();

        try {
            new Run().doCorrespondEvent();

        } catch (VMDisconnectedException e) {
            System.out.println("Virtual Machine is disconnected.");

        } finally {
            IO.printBuffer();
        }

    }
}