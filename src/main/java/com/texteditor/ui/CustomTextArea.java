package com.texteditor.ui;

import com.texteditor.editor.*;
import com.texteditor.editor.commands.Command;
import com.texteditor.editor.commands.CommandManager;
import com.texteditor.editor.commands.DeleteCommand;
import com.texteditor.editor.commands.InsertCommand;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import java.util.List;
import java.util.ArrayList;

public class CustomTextArea extends Canvas {
    private static final int FONT_SIZE = 13;
    private final Rope rope;

    // cursor related
    private boolean cursorVisible = true;
    private double cursorX = 0;
    private double cursorY = 0;

    private TextChangeListener textChangeListener;
    private final Text textMetrics;
    private final List<LineInfo> wrappedLines = new ArrayList<>();

    CommandManager commandManager = new CommandManager();

    // Interface for listening to the text change
    public interface TextChangeListener {
        void onTextChanged(String newText);
    }

    public CustomTextArea(Rope rope) {
        final int WINDOW_WIDTH = 710;
        final int WINDOW_HEIGHT = 400;

        this.rope = rope;
        this.textMetrics = new Text();

        setWidth(WINDOW_WIDTH);
        setHeight(WINDOW_HEIGHT);
        setFocusTraversable(true);

        setupCursorBlink();
        setupEventHandlers();
        renderContent();
    }

    // Sets up a blinking animation for the cursor
    private void setupCursorBlink() {
        final int CURSOR_BLINK_SCD = 350;
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(CURSOR_BLINK_SCD),
                event -> {
                    cursorVisible = !cursorVisible;
                    renderContent();
                }
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // Helper functions to calculate the index for inserting into the Rope
    private int calculateIndexPosition() {
        // Calculate the position to insert
        // Could be in the middle
        int posToInsert = 0;

        // all chars of every line except this current one
        for (int idx = 0; idx < cursorY / FONT_SIZE - 1; idx ++) {
            posToInsert += getCharsAtY(idx);
        }

        // add all the chars on the current line up until the cursor
        posToInsert += getCharsUpToX();
        return posToInsert;
    }

    // Sets up event handlers for keyboard and mouse inputs
    private void setupEventHandlers() {
        // handles when a character is typed
        addEventHandler(KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();
            String validCharacters = ".,!?-():;/\\@#$%&*+=<>[]{}|`~^";

            if (!character.isEmpty() && (Character.isLetterOrDigit(character.charAt(0))
                    || Character.isWhitespace(character.charAt(0)) || validCharacters.contains(character)))
            {

                rope.insert(calculateIndexPosition(), character);
                updateCursorIncrementX(getTextWidth(character));
                notifyTextChanged();
                renderContent();
            }
        });

        addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            // KeyCodeCombination to manage shortcuts like Undo (Ctrl+Z) and Redo (Ctrl+R)
            KeyCombination undoCombination = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
            KeyCombination redoCombination = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);

            if (undoCombination.match(event)) {
                Command undoCommand = commandManager.undo();
                updateCursorAfterUndoRedo(undoCommand);
                renderContent();
            }
            else if (redoCombination.match(event)) {
                Command redoCommand = commandManager.redo();
                updateCursorAfterUndoRedo(redoCommand);
                renderContent();
            }
            else {
                switch (event.getCode()) {
                    case BACK_SPACE -> {
                        if (!rope.isEmpty() && calculateIndexPosition() > 0) {
                            String lastChar = rope.peakLastChar(calculateIndexPosition());
                            Command backspaceCommand = new DeleteCommand(rope, calculateIndexPosition() - 1, calculateIndexPosition());
                            commandManager.executeCommand(backspaceCommand);
                            updateCursorDecrementX(getTextWidth(lastChar));
                            notifyTextChanged();
                            renderContent();
                        }
                    }
                    case ENTER -> {
                        rope.insert(calculateIndexPosition(), "\n");
                        cursorX = 0;
                        updateCursorIncrementY();
                        notifyTextChanged();
                        renderContent();
                    }
                    case UP -> {
                        updateCursorDecrementY();
                        renderContent();
                    }
                    case DOWN -> {
                        updateCursorIncrementY();
                        renderContent();
                    }
                    case LEFT -> {
                        if (cursorX > 0) {
                            int pos = getCharsUpToX();
                            updateCursorDecrementX(getTextWidth(rope.substring(pos - 1, pos).getRopeData()));
                        }
                    }
                    case RIGHT -> {
                        int pos = getCharsUpToX();

                        if (cursorX / getTextWidth("A") < rope.getStringSize()) {
                            String nextChar = rope.substring(pos, pos + 1).getRopeData();
                            updateCursorIncrementX(getTextWidth(nextChar));
                        }
                    }
                }
            }
        });

        // handles when a mouse clicks inside the text area to update the cursor position
        setOnMouseClicked(event -> {
            requestFocus();
            double clickX = event.getX();
            double clickY = event.getY();
            updateCursorPosition(clickX, clickY);
            renderContent();
        });
    }

    private static class LineInfo {
        double x;
        double y;
        String content;

        LineInfo(double x, double y, String content) {
            this.x = x;
            this.y = y;
            this.content = content;
        }
    }

    // Renders the text content and cursor on the canvas
    private void renderContent() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        gc.setFill(Constants.BACKGROUND_COLOR);
        gc.fillRect(0, 0, getWidth(), getHeight());

        gc.setFont(Font.font("Monospaced", FontWeight.BOLD, FONT_SIZE));
        gc.setFill(Constants.FONT_WHITE);

        wrappedLines.clear();
        String[] lines = rope.getRopeData().split("\n");
        double y = FONT_SIZE;

        for (String line : lines) {
            String[] words = line.split("(?<=\\s)");
            StringBuilder currentLine = new StringBuilder();
            double x = 0;

            for (String word : words) {
                double wordWidth = getTextWidth(word);

                // check if appending at the moment is going to result in a wordwrap
                // update the cursor positions
                if (x + wordWidth > getWidth()) {
                    wrappedLines.add(new LineInfo(0, y, currentLine.toString()));
                    gc.fillText(currentLine.toString(), 0, y);
                    y += FONT_SIZE + 5;
                    currentLine = new StringBuilder(word);
                    x = wordWidth;

                } else {
                    currentLine.append(word);
                    x += wordWidth;
                }
            }

            wrappedLines.add(new LineInfo(0, y, currentLine.toString()));
            gc.fillText(currentLine.toString(), 0, y);
            y += FONT_SIZE + 5;
        }

        // draw the cursor if its currently visible
        if (cursorVisible) {
            gc.setStroke(Color.WHITE);
            gc.strokeLine(cursorX, cursorY, cursorX, cursorY + FONT_SIZE);
        }
    }

    public void setTextChangeListener(TextChangeListener listener) {
        this.textChangeListener = listener;
    }

    // Font metrics calculation
    // Returns the width of a string in decimals
    private double getTextWidth(String text) {
        textMetrics.setText(text);
        textMetrics.setFont(Font.font("Monospaced", FONT_SIZE));
        return textMetrics.getLayoutBounds().getWidth();
    }

    // Moves the X position of the cursor to the right
    private void updateCursorIncrementX(double width) {
        if (cursorX + width > getWidth()) {
            String lastChar = rope.peakLastCharsBeforeCursor(calculateIndexPosition());
            cursorX = getTextWidth(lastChar);
            updateCursorIncrementY();
        } else {
            cursorX += width;
        }
    }

    // Moves the Y position of the cursor down one step
    private void updateCursorIncrementY() {
        cursorY += FONT_SIZE + 5;
    }

    // Moves the Y position of the cursor up one step
    private void updateCursorDecrementY() {
        if (cursorY > 0) {
            cursorY = Math.max(0, cursorY - (FONT_SIZE + 5));
        }
    }

    // Moves the X position of the cursor to the left
    private void updateCursorDecrementX(double width) {
        if (cursorX - width < 0 && cursorY > 0) {
            cursorY -= (FONT_SIZE + 5);
            cursorX = getCharsAtY((int) cursorY);

        } else {
            cursorX -= getTextWidth("A");
        }
    }

    // Returns the character count on a given row
    private int getCharsAtY(int y) {
        for (int i = 0; i < wrappedLines.size(); i++) {
            if (i == y) return wrappedLines.get(i).content.length();
        }

        return 0;
    }

    // Returns the number of characters up until the X pos of the cursor, on the current row
    // This method is HARDCODED as the font used for the text area is monospaced
    private int getCharsUpToX() {
        return (int) (cursorX / getTextWidth("A"));
    }

    // Returns the rope size
    public int getStringSize() {
        return rope.getStringSize();
    }

    private void notifyTextChanged() {
        if (textChangeListener != null) {
            textChangeListener.onTextChanged(rope.getRopeData());
        }
    }

    /**
     * Adjusts the cursor position after a command (undo/redo).
     *
     * @param command The command that was undone or redone.
     */
    private void updateCursorAfterUndoRedo(Command command) {
        if (command instanceof DeleteCommand deleteCommand) {
            String deletedText = deleteCommand.getDeletedText();
            updateCursorIncrementX(getTextWidth(deletedText));
        } else if (command instanceof InsertCommand insertCommand) {
            String insertedText = insertCommand.getInsertedText();
            updateCursorDecrementX(getTextWidth(insertedText));
        }

    }

    private void updateCursorPosition(double clickX, double clickY) {
        for (LineInfo line : wrappedLines) {
            if (Math.abs(line.y - FONT_SIZE - clickY) < (double) (FONT_SIZE + 5) / 2) {
                double totalWidth = 0;
                String currentLine = line.content;

                for (int i = 0; i < currentLine.length(); i++) {
                    double charWidth = getTextWidth(String.valueOf(currentLine.charAt(i)));
                    if (totalWidth + (charWidth / 2) > clickX) {
                        cursorX = totalWidth;
                        cursorY = line.y - FONT_SIZE;
                        return;
                    }
                    totalWidth += charWidth;
                }
                cursorX = totalWidth;
                cursorY = line.y - FONT_SIZE;
                return;
            }
        }
    }
}