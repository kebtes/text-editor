package com.texteditor.editor.commands;

import java.util.Stack;

/**
 * The CommandManager class is responsible for managing the execution of commands.
 * It supports undo and redo functionality by maintaining stacks for undo and redo commands.
 */
public class CommandManager {
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    /**
     * Executes a given command and stores it in the undo stack. Clears the redo stack.
     *
     * @param command the command to execute
     */
    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);

        redoStack.clear();
    }

    /**
     * Undoes the last executed command, if available, and pushes it to the redo stack.
     *
     * @return the undone command, or null if no commands are available to undo
     */
    public Command undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            return command;
        }

        return null;
    }

    /**
     * Redoes the last undone command, if available, and pushes it to the undo stack.
     *
     * @return the redone command, or null if no commands are available to redo
     */
    public Command redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            return command;
        }

        return null;
    }

}
