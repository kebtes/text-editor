package com.texteditor.editor;

public class DeleteCommand implements Command{
    private final Rope rope;
    private final int startPos; // Start position of the deletion
    private final int endPos; // End position of the deletion
    private String deletedText; // The text to delete (saved for undo operations)

    public DeleteCommand(Rope rope, int startPos, int endPos) {
        this.rope = rope;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    @Override
    public void execute() {
        deletedText = rope.substring(startPos, endPos).getRopeData();
        rope.delete(startPos, endPos);
    }

    @Override
    public void undo() {
        rope.insert(startPos, deletedText);
    }

    public String getDeletedText() {
        return deletedText;
    }
}
