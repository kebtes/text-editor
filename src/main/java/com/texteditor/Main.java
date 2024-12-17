package com.texteditor;

import com.texteditor.editor.RopeNode;
import com.texteditor.editor.Rope;

public class Main {
    public static void main(String[] args) {
//        Rope rp = new Rope("Java Programming");
//
//        Rope substring = rp.substring(6, 6);
//
//        System.out.println(substring.getRopeData());

//        Rope rope = new Rope("Hello");
//        rope.insert(5, " World");
//        rope.delete(0, 5);
//        rope.concat(new Rope(" Java"));
//        assertEquals(" World Java", rope.getRopeData());
//    }

        Rope rope = new Rope("Hello World");
//        rope.insert(5, " World");
        System.out.println(rope.getRopeData());
        rope.delete(0, 5);
        System.out.println(rope.getRopeData());

    }
}