package com.texteditor.ui;

import com.texteditor.editor.Rope;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;
import java.util.List;
import java.util.ArrayList;

public class CustomTextArea extends Canvas {
    private static final int FONT_SIZE = 15;
    private final Rope rope;
    private boolean cursorVisible = true;
    private double cursorX = 0;
    private double cursorY = 0;
    private TextChangeListener textChangeListener;
    private final Text textMetrics;
    private final List<LineInfo> wrappedLines = new ArrayList<>();

    public interface TextChangeListener {
        void onTextChanged(String newText);
    }

    public CustomTextArea(Rope rope) {
        this.rope = rope;
        this.textMetrics = new Text();

        setWidth(710);
        setHeight(513);
        setFocusTraversable(true);

        setupCursorBlink();
        setupEventHandlers();
        renderContent();
    }

    private void setupCursorBlink() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(500),
                event -> {
                    cursorVisible = !cursorVisible;
                    renderContent();
                }
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void setupEventHandlers() {
        addEventHandler(KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();
            if (!character.isEmpty() && (Character.isLetterOrDigit(character.charAt(0))
                    || Character.isWhitespace(character.charAt(0))
                    || character.charAt(0) == '.'
                    || character.charAt(0) == ','
                    || character.charAt(0) == '!'
                    || character.charAt(0) == '?'
                    || character.charAt(0) == '-'
                    || character.charAt(0) == '('
                    || character.charAt(0) == ')'
                    || character.charAt(0) == ':'
                    || character.charAt(0) == ';'
                    || character.charAt(0) == '/'
                    || character.charAt(0) == '\\'
                    || character.charAt(0) == '@'
                    || character.charAt(0) == '#'
                    || character.charAt(0) == '$'
                    || character.charAt(0) == '%'
                    || character.charAt(0) == '&'
                    || character.charAt(0) == '*'
                    || character.charAt(0) == '+'
                    || character.charAt(0) == '='
                    || character.charAt(0) == '<'
                    || character.charAt(0) == '>'
                    || character.charAt(0) == '['
                    || character.charAt(0) == ']'
                    || character.charAt(0) == '{'
                    || character.charAt(0) == '}'
                    || character.charAt(0) == '|'
                    || character.charAt(0) == '`'
                    || character.charAt(0) == '~'
                    || character.charAt(0) == '^')) {
                rope.append(character);
                updateCursorIncrementX(getTextWidth(character));
                notifyTextChanged();
                renderContent();
            }
        });

        addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case BACK_SPACE -> {
                    if (!rope.isEmpty()) {
                        String lastChar = rope.peakLastChar();
                        rope.backspace();
                        updateCursorDecrementX(getTextWidth(lastChar));
                        notifyTextChanged();
                        renderContent();
                    }
                }
                case ENTER -> {
                    rope.append("\n");
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
            }
        });

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

    private void renderContent() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());

        gc.setFont(Font.font("Geist", FONT_SIZE));
        gc.setFill(Color.BLACK);

        wrappedLines.clear();
        String[] lines = rope.getRopeData().split("\n");
        double y = FONT_SIZE;

        for (String line : lines) {
            String[] words = line.split("(?<=\\s)");
            StringBuilder currentLine = new StringBuilder();
            double x = 0;

            for (String word : words) {
                double wordWidth = getTextWidth(word);

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

        updateCursorBasedOnWrappedLines();

        if (cursorVisible) {
            gc.setStroke(Color.DARKGRAY);
            gc.strokeLine(cursorX, cursorY, cursorX, cursorY + FONT_SIZE);
        }
    }

    public void setTextChangeListener(TextChangeListener listener) {
        this.textChangeListener = listener;
    }

    private double getTextWidth(String text) {
        textMetrics.setText(text);
        textMetrics.setFont(Font.font("Geist Light", FONT_SIZE));
        return textMetrics.getLayoutBounds().getWidth();
    }

    private void updateCursorIncrementX(double width) {
        if (cursorX + width >= getWidth()) {
            cursorX = 0;
            updateCursorIncrementY();
        } else {
            cursorX += width;
        }
    }

    private void updateCursorIncrementY() {
        cursorY += FONT_SIZE + 5;
    }

    private void updateCursorDecrementY() {
        if (cursorY > 0) {
            cursorY = Math.max(0, cursorY - (FONT_SIZE + 5));
        }
    }

    private void updateCursorDecrementX(double width) {
        if (cursorX - width < 0 && cursorY > 0) {
            cursorY -= FONT_SIZE + 5;
            String[] lines = rope.getRopeData().split("\n");
            int currentLine = (int) (cursorY / (FONT_SIZE + 5));
            if (currentLine >= 0 && currentLine < lines.length) {
                cursorX = getTextWidth(lines[currentLine]);
            } else {
                cursorX = 0;
            }
        } else {
            cursorX = Math.max(0, cursorX - width);
        }
    }

    private double getLastLineWidth() {
        if (rope.isEmpty()) return 0;
        String[] lines = rope.getRopeData().split("\n");
        return getTextWidth(lines[lines.length - 1]);
    }

    public int getStringSize() {
        return rope.getStringSize();
    }

    private void notifyTextChanged() {
        if (textChangeListener != null) {
            textChangeListener.onTextChanged(rope.getRopeData());
        }
    }

    private void updateCursorBasedOnWrappedLines() {
        int totalChars = 0;
        String text = rope.getRopeData();

        for (LineInfo line : wrappedLines) {
            int lineLength = line.content.length();
            if (totalChars + lineLength >= text.length()) {
                cursorX = getTextWidth(text.substring(totalChars));
                cursorY = line.y - FONT_SIZE;
                break;
            }
            totalChars += lineLength;
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