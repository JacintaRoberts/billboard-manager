package server;

import java.io.Serializable;

// TODO: Remove this class later if not in use - this is just an example of serialisation
public class MyClass implements Serializable {
    private final int val;

    public MyClass(int val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "MyClass number is " + val;
    }

    public int getVal() {
        return val;
    }
}
