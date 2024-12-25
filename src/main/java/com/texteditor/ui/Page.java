package com.texteditor.ui;

import com.texteditor.editor.Rope;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class Page {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 838;
    private final Label characterCount;

    public Page(Rope rope, Stage primaryStage) {

        BorderPane root = new BorderPane();

        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);

        VBox body = new VBox(10);
        body.setPadding(new Insets(20));
        body.setStyle("-fx-background-color: white;");

        Label titleLabel = new Label("TITLE");
        titleLabel.setStyle("-fx-font-family: 'Geist'; -fx-font-size: 9px; -fx-text-fill: #666666;");

        Label documentTitle = new Label("Sample Document Title");
        documentTitle.setStyle("-fx-font-family: 'Geist'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #585BFF;");

        CustomTextArea textArea = new CustomTextArea(rope);

        BorderPane footer = new BorderPane();
        footer.setStyle("-fx-background-color: #585BFF; -fx-padding: 10px;");

        characterCount = new Label(textArea.getStringSize() + " chars");
        characterCount.setStyle("-fx-font-family: 'Geist'; -fx-font-size: 11px; -fx-text-fill: white;");
        footer.setRight(characterCount);

        textArea.setTextChangeListener(newText ->
                characterCount.setText(newText.length() + " chars")
        );

        body.getChildren().addAll(titleLabel, documentTitle, textArea);

        root.setCenter(body);
        root.setBottom(footer);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Text Editor");
        primaryStage.show();

        // Request focus for the text area
        textArea.requestFocus();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: white;");

        UiConstants.menuBarItems.forEach((menuString, menuItems) -> {
            Menu menu = new Menu(menuString);
            menu.setStyle("-fx-font-family: 'Geist'; -fx-font-size: 12px;");

            menuItems.forEach(itemString -> {
                MenuItem item = new MenuItem(itemString);
                menu.getItems().add(item);
            });

            menuBar.getMenus().add(menu);
        });

        return menuBar;
    }
}