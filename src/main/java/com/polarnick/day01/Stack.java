package com.polarnick.day01;

import java.util.ArrayList;
import java.util.List;

public class Stack<T> {

    private final List<T> stack;
    private int last = -1;

    public Stack() {
        this.stack = new ArrayList<T>();
    }

    public void push(T value) {
        last++;
        if (last == stack.size()) {
            stack.add(value);
        } else {
            stack.set(last, value);
        }
    }

    public T pop() {
        T result = stack.get(last);
        stack.set(last, null);
        last--;
        return result;
    }
}
