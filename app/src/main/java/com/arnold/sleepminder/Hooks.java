package com.arnold.sleepminder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Callable;

public class Hooks {
    public static final int RECORDING_LIST_UPDATE = 0;

    private static HashMap<Integer, ArrayList<Callable<Integer>>> hooks;

    public Hooks() {
        hooks = new HashMap<Integer, ArrayList<Callable<Integer>>>();
    }

    public static void bind (int hook, Callable<Integer> callback) {
        if (hooks.containsKey(hook)) {
            Objects.requireNonNull(hooks.get(hook)).add(callback);
        } else {
            hooks.put(hook, new ArrayList<Callable<Integer>>());
        }
    }

    public static void clear () {
        hooks = new HashMap<Integer, ArrayList<Callable<Integer>>>();
    }

    public static void  remove (int hook) {
        hooks.remove(hook);
    }

    public static void trigger (int hook) {
        if (hooks.containsKey(hook)) {
            for (Callable<Integer> callback : Objects.requireNonNull(hooks.get(hook))) {
                try {
                    callback.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
