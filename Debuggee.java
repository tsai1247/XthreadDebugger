public class Debuggee {
    public static void main(String[] args) {
        Integer The_Integer = Integer.valueOf(-1);
        TargetMethod tm = new TargetMethod(The_Integer);
        Thread[] allThreads = new Thread[3];
        for(int i=0; i<allThreads.length; i++)
            allThreads[i] = new Thread(new TargetThread(tm, i), String.valueOf((char)('A'+i*2)) + String.valueOf((char)('A'+i*2+1)) );
        for(Thread thread:allThreads)
            thread.start();
    }
}
class TargetThread implements Runnable{
    
    private TargetMethod tm;
    private Integer the_Integer;
    private int type;

    public TargetThread(TargetMethod tm, int type)
    {
        this.tm = tm;
        this.type = type;
        this.the_Integer = tm.getInteger();;
    }
    @Override
    public void run() {
        switch(type)
        {
            case 0:
                tm.A();
                tm.B();
                break;
            case 1:
                tm.C();
                tm.D();
                break;
            case 2:
                tm.E();
                tm.F();
                break;
            default:
                break;
        }
        return;
    }
}
