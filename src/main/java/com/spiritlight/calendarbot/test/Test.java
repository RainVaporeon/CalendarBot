package com.spiritlight.calendarbot.test;

import com.spiritlight.fishutils.misc.Option;

public class Test {

    public static void main(String[] args) {
        Option.register("test", true);
        Option.register("int", 10);
        Option.register("double", 10.0);
        Option.register("string", "value");

        Test.acceptBoolean(Option.auto("test"));

        Test.acceptInt(Option.auto("int"));

        Test.acceptDouble(Option.auto("double"));

        Test.acceptString(Option.auto("string"));
    }

    private static void acceptBoolean(boolean b) {
        System.out.println(b);
    }

    private static void acceptInt(int i) {
        System.out.println(i);
    }

    private static void acceptDouble(double d) {
        System.out.println(d);
    }

    private static void acceptString(String s) {
        System.out.println(s);
    }
}
