package com.texteditor.editor;

public class InsertCommand implements Command{
    private final Rope rope;
    private final String text; // Holds the text to insert
    private final int cursorPosition; // The position where text will be inserted

    public InsertCommand(Rope rope, String text, int cursorPosition) {
        this.rope = rope;
        this.text = text;
        this.cursorPosition = cursorPosition;
    }

    @Override
    public void execute() {
        rope.insert(cursorPosition, text);
    }

    @Override
    public void undo() {
        rope.delete(cursorPosition, cursorPosition + text.length());
    }

    public String getInsertedText() {
        return text;
    }
}
