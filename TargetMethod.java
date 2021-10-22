public class TargetMethod {
    private Integer The_Integer;

    public TargetMethod(Integer integ) {
        this.The_Integer = integ;
    }

    public void A() {
        The_Integer = 1;
        for (int i = 0; i < 100; i++) {
            Log.LogAppend("A");
        }

        print();
    }

    public void B() {
        The_Integer = 2;
        for (int i = 0; i < 100; i++) {
            Log.LogAppend("B");
        }

        print();
    }

    public void C() {
        The_Integer = 3;
        for (int i = 0; i < 100; i++) {
            Log.LogAppend("C");
        }

        print();
    }

    public void D() {
        The_Integer = 4;
        for (int i = 0; i < 100; i++) {
            Log.LogAppend("D");
        }
        print();
    }

    public void E() {
        The_Integer = 5;
        for (int i = 0; i < 100; i++) {
            Log.LogAppend("E");
        }
        print();
    }

    public void F() {
        The_Integer = 6;
        for (int i = 0; i < 100; i++) {
            Log.LogAppend("F");
        }
        print();
    }

    public Integer getInteger() {
        return The_Integer;
    }

    private void print() {
        System.out.println("The_Integer: " + The_Integer);
    }
}
