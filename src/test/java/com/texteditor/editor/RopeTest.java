package com.texteditor.editor;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;

class RopeTest {

    // Test for insert operation
    @ParameterizedTest
    @CsvSource({
            "Hello, 0, 'Hi ', Hi Hello",
            "Hello, 5, ' World', Hello World",
            "Hello, 3, 'lo ', Hello lo",
            "Hello, 2, 'lo ', Helo llo"
    })
    void testInsert(String initial, int index, String toInsert, String expected) {
        Rope rope = new Rope(initial);
        rope.insert(index, toInsert);
        assertEquals(expected, rope.getRopeData());
    }

    // Test for concat operation
    @ParameterizedTest
    @CsvSource({
            "Hello, World, HelloWorld",
            "Hi, there, Hithere",
            "Good, Morning, GoodMorning",
            "a, b, ab"
    })
    void testConcat(String str1, String str2, String expected) {
        Rope rope1 = new Rope(str1);
        Rope rope2 = new Rope(str2);
        rope1.concat(rope2);
        assertEquals(expected, rope1.getRopeData());
    }

    // Test for substring operation
    @ParameterizedTest
    @CsvSource({
            "Hello World, 0, 5, Hello",
            "Hello World, 6, 11, World",
            "Java Programming, 0, 4, Java",
            "Good Morning, 5, 12, Morning"
    })
    void testSubstring(String initial, int start, int end, String expected) {
        Rope rope = new Rope(initial);
        String result = rope.substring(start, end).getRopeData();
        assertEquals(expected, result);
    }

    // Test for delete operation
    @ParameterizedTest
    @CsvSource({
            "Hello World, 0, 5, World",
//            "Hello World, 6, 11, Hello",
//            "Java Programming, 5, 16, Java",
//            "Good Morning, 4, 12, Good"
    })
    void testDelete(String initial, int start, int end, String expected) {
        Rope rope = new Rope(initial);
        rope.delete(start, end);
        assertEquals(expected, rope.getRopeData());
    }

    @ParameterizedTest
    @CsvSource({
            "Hello World, 6, 11, Hello",
            "Java Programming, 5, 16, Java",
            "Good Morning, 4, 12, Good"
    })
    void testDeleteInvalid(String initial, int start, int end, String expected){
        Rope rope = new Rope(initial);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            rope.delete(start, end);
        });
    }

    // Test for insert with empty string
    @ParameterizedTest
    @CsvSource({
            "Hello, 0, '', Hello",
            "Hello, 5, '', Hello"
    })
    void testInsertEmptyString(String initial, int index, String toInsert, String expected) {
        Rope rope = new Rope(initial);
        rope.insert(index, toInsert);
        assertEquals(expected, rope.getRopeData());
    }

    // Test for insert at the same index with the same string (no change)
    @ParameterizedTest
    @CsvSource({
            "Hello, 0, 'Hello', HelloHello",
            "World, 5, 'World', WorldWorld"
    })
    void testInsertSameString(String initial, int index, String toInsert, String expected) {
        Rope rope = new Rope(initial);
        rope.insert(index, toInsert);
        assertEquals(expected, rope.getRopeData());
    }

    // Test for empty Rope string
    @Test
    void testEmptyRope() {
        Rope rope = new Rope("");
        assertEquals("", rope.getRopeData());
    }

    // Test for concat with empty string
    @ParameterizedTest
    @CsvSource({
            "Hello, '', Hello",
            "'', World, World"
    })
    void testConcatEmptyString(String initial, String toConcat, String expected) {
        Rope rope = new Rope(initial);
        rope.concat(new Rope(toConcat));
        assertEquals(expected, rope.getRopeData());
    }

    // Test for substring with invalid range
    @ParameterizedTest
    @CsvSource({
            "Hello World, 5, 2, ''",
//            "Java Programming, 6, 6, 'r'"
    })
    void testInvalidSubstring(String initial, int start, int end, String expected) {
        Rope rope = new Rope(initial);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            rope.substring(start, end);
        });
    }

    // Test for delete with invalid range
    @ParameterizedTest
    @CsvSource({
            "Hello World, 11, 10, Hello World",
            "Java Programming, 16, 10, Java Programming"
    })
    void testInvalidDelete(String initial, int start, int end, String expected) {
        Rope rope = new Rope(initial);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            rope.delete(start, end);
        });
    }

    // Test for single character insert
    @Test
    void testSingleCharacterInsert() {
        Rope rope = new Rope("Hello");
        rope.insert(5, "!");
        assertEquals("Hello!", rope.getRopeData());
    }

    // Test for single character delete
    @Test
    void testSingleCharacterDelete() {
        Rope rope = new Rope("Hello");
        rope.delete(0, 0);
        assertEquals("ello", rope.getRopeData());
    }

    // Test for large string concatenation
    @Test
    void testLargeConcatenation() {
        String str = "Hello".repeat(1000);
        Rope rope = new Rope(str);
        rope.concat(new Rope(" World"));
        assertEquals(str + " World", rope.getRopeData());
    }

    // Test for substring with same start and end
    @Test
    void testSubstringSameStartEnd() {
        Rope rope = new Rope("Hello");
        String result = rope.substring(1, 1).getRopeData();
        assertEquals("", result);
    }

    // Test for multiple operations
    @Test
    void testMultipleOperations() {
        Rope rope = new Rope("Hello");
        rope.insert(5, " World");
        rope.delete(0, 5);
        rope.concat(new Rope(" Java"));
        assertEquals("World Java", rope.getRopeData());
    }

    // Test for Rope of length 1
    @Test
    void testRopeLengthOne() {
        Rope rope = new Rope("A");
        assertEquals("A", rope.getRopeData());
    }

    // Test for deleting entire string
    @Test
    void testDeleteEntireString() {
        Rope rope = new Rope("Hello");
        rope.delete(0, 4);
        assertEquals("", rope.getRopeData());
    }
}
