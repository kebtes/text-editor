package com.texteditor.ui;

import com.texteditor.editor.Rope;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomTextArea extends JPanel{
//    private String customText = "";
    private int fontSize = 14;
    private boolean cursorVisible = true;
    private Rope rope = new Rope("");

    private FontMetrics fontMetrics;

    private int cursorX = 0;
    private int cursorY = 0;

    private TextChangeListener textChangeListener;

    @FunctionalInterface
    public interface TextChangeListener {
        void onTextChanged(String newText);
    }

    public void setTextChangeListener(TextChangeListener listener){
        this.textChangeListener = listener;
    }

    private void handleTextInput(char typedChar){
        if (Character.isLetterOrDigit(typedChar) || Character.isWhitespace(typedChar)){
            try{
                rope.append(Character.toString(typedChar));

                if (fontMetrics != null) {
                    updateCursorIncrementX(fontMetrics.stringWidth(Character.toString(typedChar)));
                }

                if (textChangeListener != null){
                    textChangeListener.onTextChanged(rope.getRopeData());
                }

                repaint();

            } catch (Exception e) {
                System.err.println("Error handling text input: " + e.getMessage());
            }
        }
    }

    private void handleBackSpace() {
        if (!rope.isEmpty()){
            try{
                String lastChar = rope.peakLastChar();
                System.out.println(lastChar);

                rope.backspace();
                System.out.println("YES");

                if (fontMetrics != null && !lastChar.isEmpty()) {
                    updateCursorDecrementX(fontMetrics.stringWidth(lastChar));
                }

                if (textChangeListener != null) {
                    textChangeListener.onTextChanged(rope.getRopeData());
                }

                repaint();
            } catch (StringIndexOutOfBoundsException e) {
                System.err.println("Index out of bound while trying to backspace: " + e.getMessage());
            }

            catch (Exception e){
                System.err.println("Error handling backspace: " + e.getMessage());
            }
        }
    }

    private void renderText(Graphics g) {
        System.out.println(rope.getRopeData());

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(UiConstants.getGeistLight(fontSize));
        g2d.setColor(UiConstants.TEXT_AREA_COLOR);

        fontMetrics = g.getFontMetrics();
        int lineHeight = fontMetrics.getHeight();
        int availableWidth = getWidth();

//        System.out.println(availableWidth);
        int totalStringSize = rope.getStringSize();
        String[] words = rope.getRopeData().split(" ");
        StringBuilder currentLine = new StringBuilder();

//        StringBuilder textUpToCursor = new StringBuilder();
        int y = lineHeight;
        int totalChars = 0;

        for (String word : words) {
            int wordWidth = fontMetrics.stringWidth(word + " ");
            int lineWidth = fontMetrics.stringWidth(currentLine.toString());

            if (lineWidth + wordWidth > availableWidth) {
                System.out.println("Wrapped");

                // Draw the current line
                g.drawString(currentLine.toString(), 0, y);

                // Update cursor position if it's in this line
                if (totalChars + currentLine.length() >= totalStringSize) {
                    cursorX = fontMetrics.stringWidth(currentLine.toString());
                    cursorY = y - lineHeight + 1;
                }

                // Move to next line
                y += lineHeight;
                currentLine = new StringBuilder(word + " ");

            } else {
                currentLine.append(word).append(" ");
            }

            totalChars += word.length() + 1;
        }

        // Draw the last line
        if (!currentLine.isEmpty()) {
            g.drawString(currentLine.toString(), 0, y);

            // Update cursor position if it's in the last line
            if (totalChars >= totalStringSize) {
                cursorX = fontMetrics.stringWidth(currentLine.toString());
                cursorY = y - lineHeight + 2;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderText(g);

        // Draw cursor
        if (cursorVisible) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(cursorX, cursorY, 1, fontMetrics.getHeight());
        }
    }

    public CustomTextArea(Rope rope){

//        timer for the cursor
//        blinking effect

        new Timer(350, e -> {
            cursorVisible = !cursorVisible;
            repaint();
        }).start();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                TODO use this method for substring selection

                System.out.println(e.getPoint());
            }

            @Override
            public void mousePressed(MouseEvent e) {
//              TODO use this to set the layout active and ready it up for text editing
                int pointX = e.getX();
                int pointY = e.getY();

//                updateCursorPosition(pointX, pointY);
                repaint();

            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                handleTextInput(e.getKeyChar());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_BACK_SPACE -> handleBackSpace();
                    case KeyEvent.VK_UP -> updateCursorDecrementY();

                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
//                TODO do more research on this interface

                int typedKeyCode = e.getKeyCode();

                if (typedKeyCode == KeyEvent.VK_BACK_SPACE) {
                    repaint();
                }
            }
        });

    }

    private void updateCursorPosition(int pointX, int pointY) {
        FontMetrics fontMetrics = getFontMetrics(getFont());
        int lineHeight = fontMetrics.getHeight();
        int availableWidth = getWidth();

        // Calculate which line the click corresponds to
        int lineNumber = pointY / lineHeight;
//        int lineStartX = 0;
        cursorY = lineHeight * lineNumber;

        String[] words = rope.getRopeData().split(" ");
        StringBuilder line = new StringBuilder();
        int y = lineHeight;

        for (String word : words) {
            int wordWidth = fontMetrics.stringWidth(word);
            int lineWidth = fontMetrics.stringWidth(line.toString());

            if (lineWidth + wordWidth > availableWidth) {
                y += lineHeight;
                line = new StringBuilder(word + " ");
            } else {
                line.append(word).append(" ");
            }

            cursorX = lineWidth + wordWidth;
            if (y > pointX) {
                break;
            }

            cursorY = y;
        }
    }

    private void updateCursorIncrementX(int charWidth){
        if (cursorX == 0 && cursorY == 0) {
            return;
        }

        int fontHeight = fontMetrics.getHeight();
        int availableWidth = getWidth();

        if (cursorX + charWidth > getWidth()) {
            cursorY += fontMetrics.getHeight();
            cursorX = 0;
        } else {
            cursorX += charWidth;
        }

    }

    private void updateCursorIncrementY(){
        int fontHeight = fontMetrics.getHeight();
        int availableWidth = getWidth();

//        TODO  scroll thingy if the y axis is out of visible range

        cursorY += fontHeight;
    }

    private void updateCursorDecrementY(){
        int fontHeight = fontMetrics.getHeight();

        cursorY -= fontHeight;
    }


    private void updateCursorDecrementX(int charWidth){
        System.out.println(charWidth);

        if (cursorX == 0 && cursorY == 0){
            return;
        }

        int fontHeight = fontMetrics.getHeight();

        if (cursorX - charWidth < 0){
            cursorX = Math.max(0, cursorY - fontHeight);
            cursorY -= getLastLineWidth();

        }else {
            cursorX -= charWidth;
        }
    }

    private int getLastLineWidth(){
        if (rope.isEmpty()) return 0;

        String[] lines = rope.getRopeData().split("\n");
        String lastLine = lines[lines.length - 1];
        return fontMetrics.stringWidth(lastLine);
    }

    public void setText(String customText){
        rope = new Rope(customText);
    }

    public int getStringSize(){
        return rope.getStringSize();
    }

    @Override
    public boolean isFocusable() {
        return true;
    }


}