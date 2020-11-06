package com.sebbaindustries.advancedafk.engine.profiler;

import java.util.Arrays;

public class Timings {

    public Timings() {
        Arrays.fill(delta, -1);
    }

    private final long[] delta = new long[20];
    public int playerCount = 0;

    public void addDelta(long newDelta) {
        if (delta.length - 1 >= 0) System.arraycopy(delta, 0, delta, 1, delta.length - 1);
        delta[0] = newDelta;
    }

    public long[] getDelta() {
        return delta;
    }
}
