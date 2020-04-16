package server;

import java.io.Serializable;

public class MyClass implements Serializable {
    private final int val;

    public MyClass(int val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "MyClass numer is " + val;
    }

    public int getVal() {
        return val;
    }
}
