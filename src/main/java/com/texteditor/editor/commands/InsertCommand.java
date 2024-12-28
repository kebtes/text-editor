package com.texteditor.editor.commands;

import com.texteditor.editor.Rope;

/**
 * The InsertCommand class represents an insert operation on the Rope data structure.
 * It holds the inserted text and the position where it should be inserted.
 * It provides methods to execute the insertion and undo the insertion.
 */
public class InsertCommand implements Command{
    private final Rope rope;
    private final String text; // Holds the text to insert
    private final int cursorPosition; // The position where text will be inserted

    /**
     * Creates an InsertCommand for inserting text into the Rope.
     *
     * @param rope the Rope data structure
     * @param text the text to insert
     * @param cursorPosition the position where text should be inserted
     */
    public InsertCommand(Rope rope, String text, int cursorPosition) {
        this.rope = rope;
        this.text = text;
        this.cursorPosition = cursorPosition;
    }

    /**
     * Executes the insert operation by adding the text to the Rope at the specified position.
     */
    @Override
    public void execute() {
        rope.insert(cursorPosition, text);
    }

    /**
     * Undoes the insert operation by removing the inserted text from the Rope.
     */
    @Override
    public void undo() {
        rope.delete(cursorPosition, cursorPosition + text.length());
    }

    /**
     * Gets the text that was inserted.
     *
     * @return the inserted text
     */
    public String getInsertedText() {
        return text;
    }
}
