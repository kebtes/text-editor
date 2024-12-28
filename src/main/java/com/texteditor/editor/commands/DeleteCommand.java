package com.texteditor.editor.commands;

import com.texteditor.editor.Rope;

/**
 * The DeleteCommand class represents a delete operation on the Rope data structure.
 * It stores the deleted text for undo purposes and provides methods to execute and undo the delete action.
 */
public class DeleteCommand implements Command{
    private final Rope rope;
    private final int startPos; // Start position of the deletion
    private final int endPos; // End position of the deletion
    private String deletedText; // The text to delete (saved for undo operations)

    /**
     * Creates a DeleteCommand for deleting text from the Rope.
     *
     * @param rope the Rope data structure
     * @param startPos the starting position of the text to delete
     * @param endPos the ending position of the text to delete
     */
    public DeleteCommand(Rope rope, int startPos, int endPos) {
        this.rope = rope;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    /**
     * Executes the delete operation by removing the text from the Rope.
     * Saves the deleted text for potential undo.
     */
    @Override
    public void execute() {
        deletedText = rope.substring(startPos, endPos).getRopeData();
        rope.delete(startPos, endPos);
    }

    /**
     * Undoes the delete operation by re-inserting the previously deleted text at the original position.
     */
    @Override
    public void undo() {
        rope.insert(startPos, deletedText);
    }

    /**
     * Gets the text that was deleted.
     *
     * @return the deleted text
     */
    public String getDeletedText() {
        return deletedText;
    }
}
