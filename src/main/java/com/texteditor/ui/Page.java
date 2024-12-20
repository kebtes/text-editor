package com.texteditor.ui;

import com.texteditor.editor.Rope;

import javax.swing.*;
import java.awt.*;

public class Page extends JFrame {
    Rope rope;

    public Page(Rope rope) {
        this.rope = rope;

        int FRAME_WIDTH = 800;
        int FRAME_HEIGHT = 838;
        CustomTextArea textArea;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLayout(new BorderLayout());
        setResizable(false);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setFont(UiConstants.getArialRegularFont(8));
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        menuBar.setBorderPainted(false);

        UiConstants.menuBarItems.forEach((menuString, menuItems) -> {
            JMenu menu = new JMenu(menuString);
            menu.setBackground(UiConstants.ACCENT_BLUE);
            menu.setForeground(UiConstants.BLUE_COLOR);
            menu.setFont(UiConstants.getArialRegularFont(12));
            menu.setForeground(Color.BLACK);

            for (String item : menuItems) {
                JMenuItem menuItem = new JMenuItem(item);
                menuItem.setOpaque(true);
                menu.add(menuItem);
            }

            menuBar.add(menu);
        });

        setJMenuBar(menuBar);

        // body
        JPanel body = new JPanel();
        body.setBackground(UiConstants.WHITE_COLOR);
        body.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - 100));
        body.setLayout(null);

//      TITLE LABEL SECTION
        JLabel titleTextLabel = new JLabel("TITLE");
        titleTextLabel.setFont(UiConstants.getGeistSemiBold(9));
        titleTextLabel.setForeground(UiConstants.TEXT_LIGHT_GREY_COLOR);
        titleTextLabel.setBounds(33, 46, 25, 12);

        JLabel documentTitleTextLabel = new JLabel("Sample Document Title");
        documentTitleTextLabel.setFont(UiConstants.getGeistSemiBold(18));
        documentTitleTextLabel.setForeground(UiConstants.BLUE_COLOR);
        documentTitleTextLabel.setBounds(32, 70, 300, 17);

        JSeparator horizontalLine = new JSeparator();
        horizontalLine.setBackground(UiConstants.TEXT_LIGHT_GREY_COLOR);
        horizontalLine.setBounds(33, 100, 710, 1);

//        custom text area field
        textArea = new CustomTextArea(rope);
        textArea.setForeground(UiConstants.TEXT_AREA_COLOR);
        textArea.setBounds(33, 145, 710, 513);
        textArea.setBackground(Color.WHITE);

        body.add(textArea);
        body.add(horizontalLine);
        body.add(titleTextLabel);
        body.add(documentTitleTextLabel);

        // footer
        JPanel footer = new JPanel();
        footer.setBackground(UiConstants.BLUE_COLOR);
        footer.setPreferredSize(new Dimension(FRAME_WIDTH, 40));
        footer.setLayout(null);

        JLabel characterCount = new JLabel();
        characterCount.setText(textArea.getStringSize() + " chars");
        characterCount.setForeground(Color.WHITE);
        characterCount.setFont(UiConstants.getGeistLight(11));
        characterCount.setBounds(600, 18, 180, 10);
        characterCount.setHorizontalAlignment(SwingConstants.RIGHT);

        textArea.setTextChangeListener(newText -> {
            characterCount.setText(newText.length() + " chars");
        });

        footer.add(characterCount);

//        add(header, BorderLayout.NORTH);
        add(body, BorderLayout.NORTH);
        add(footer, BorderLayout.SOUTH);

        revalidate();
        setVisible(true);
    }


}
