public class Debuggee {
    public static void main(String[] args) throws InterruptedException {
        Thread[] allThreads = new Thread[2];
        for (int i = 0; i < allThreads.length; i++)
            allThreads[i] = new Thread(new TargetThread(i),
                    String.valueOf((char) ('A' + i * 2)) + String.valueOf((char) ('A' + i * 2 + 1)));
        for (int i = 0; i < allThreads.length; i++)
            allThreads[i].start();
        for (int i = 0; i < allThreads.length; i++)
            allThreads[i].join(1000000);
    }
}

class TargetThread implements Runnable {
    private int type;

    public TargetThread(int type) {
        this.type = type;
    }

    @Override
    public void run() {
        switch (type) {
            case 0:
                for (int i = 0; i < 100; i++)
                    Log.LogAppend("A " + i);
                for (int i = 0; i < 100; i++)
                    Log.LogAppend("B " + i);
                break;
            case 1:
                for (int i = 0; i < 100; i++)
                    Log.LogAppend("C " + i);
                for (int i = 0; i < 100; i++)
                    Log.LogAppend("D " + i);
                break;
        }
        return;
    }
}
