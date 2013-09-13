package com.polarnick.day01;

import android.util.Pair;

import java.util.*;

public class Profiler<T extends Enum<T>> {

    private final static boolean PERFORM_IN_OUT_NAME_CHECKS = true;
    private final static long MIN_TIME_OF_PROFILING = 0;
    private final static int LOG_STEPS_PERIOD = 128;

    private final Map<T, Long> maxTimeForMethod;
    private final Map<T, Long> minTimeForMethod;
    private final Map<T, Long> sumTimeForMethod;
    private final Map<T, Long> methodCallsCount;

    private final Stack<Long> timeIn;
    private final Stack<T> nameIn;

    private final String className;

    private int step = 0;

    public Profiler(Class clazz, String name) {
        this(clazz.getName() + "(" + name + ")");
    }

    public Profiler(Class clazz) {
        this(clazz.getName());
    }

    public Profiler(String className) {
        this.className = className;
        this.sumTimeForMethod = new HashMap<T, Long>();
        this.minTimeForMethod = new HashMap<T, Long>();
        this.maxTimeForMethod = new HashMap<T, Long>();
        this.methodCallsCount = new HashMap<T, Long>();
        this.timeIn = new Stack<Long>();
        if (PERFORM_IN_OUT_NAME_CHECKS) {
            this.nameIn = new Stack<T>();
        } else {
            this.nameIn = null;
        }
    }

    public void in(T function) {
        if (PERFORM_IN_OUT_NAME_CHECKS) {
            nameIn.push(function);
        }
        timeIn.push(System.currentTimeMillis());
    }

    public void out(T function) {
        if (PERFORM_IN_OUT_NAME_CHECKS) {
            checkThatInThisFunction(function);
        }
        long timeIn = this.timeIn.pop();
        long now = System.currentTimeMillis();
        long delta = now - timeIn;
        if (timeIn > MIN_TIME_OF_PROFILING) {
            Long sumTime = sumTimeForMethod.get(function);
            Long min = minTimeForMethod.get(function);
            Long max = maxTimeForMethod.get(function);
            Long calls = methodCallsCount.get(function);
            if (sumTime == null || calls == null) {
                if (sumTime != null || calls != null) {
                    throw new IllegalStateException("Function " + generateName(function) + " profiled incorrectly!");
                }
                min = Long.MAX_VALUE;
                max = Long.MIN_VALUE;
                sumTime = 0L;
                calls = 0L;
            }
            sumTime += delta;
            ++calls;
            min = Math.min(min, delta);
            max = Math.max(max, delta);
            minTimeForMethod.put(function, min);
            maxTimeForMethod.put(function, max);
            sumTimeForMethod.put(function, sumTime);
            methodCallsCount.put(function, calls);
        }
    }

    public List<Pair<T, Long>> getAverangeTimes() {
        List<Pair<T, Long>> result = new ArrayList<Pair<T, Long>>();
        for (T function : sumTimeForMethod.keySet()) {
            result.add(new Pair<T, Long>(function, sumTimeForMethod.get(function) / methodCallsCount.get(function)));
        }
        Collections.sort(result, new Comparator<Pair<T, Long>>() {
            private final static int SLOW_FIRST = 1;

            @Override
            public int compare(Pair<T, Long> one, Pair<T, Long> two) {
                long deltaOneTwo = one.second - two.second;
                if (deltaOneTwo > 0) {
                    return -SLOW_FIRST;
                }
                if (deltaOneTwo < 0) {
                    return SLOW_FIRST;
                }
                return 0;
            }
        });
        return result;
    }

    private String generateName(T function) {
        return className + "." + function.toString();
    }

    private void checkThatInThisFunction(T function) {
        T last = nameIn.pop();
        if (!last.equals(function)) {
            throw new IllegalStateException("We leave function \"" + generateName(function) + "\", " +
                    "while not left function \"" + generateName(function) + "." + last + "\"! " +
                    "It seems, that this profiler used from different threads!");
        }
    }

    public boolean isToLogNextStep() {
        step = (step + 1) % LOG_STEPS_PERIOD;
        return step == 0;
    }

    public String getAverangeLog() {
        List<Pair<T, Long>> times = getAverangeTimes();
        StringBuilder res = new StringBuilder("Averange times:\n");
        for (Pair<T, Long> time : times) {
            res.append("   ").append(generateName(time.first)).append(" max/avrg/min: ")
                    .append(maxTimeForMethod.get(time.first)).append("/")
                    .append(time.second).append("/")
                    .append(minTimeForMethod.get(time.first)).append(" ms  ")
                    .append(methodCallsCount.get(time.first)).append(" count\n");
        }
        return res.toString();
    }

}
