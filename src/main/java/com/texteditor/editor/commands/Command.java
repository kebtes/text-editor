package com.texteditor.editor.commands;

public interface Command {
    void execute();
    void undo();
}
