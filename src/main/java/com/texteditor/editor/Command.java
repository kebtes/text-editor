package com.texteditor.editor;

public interface Command {
    void execute();
    void undo();
}
