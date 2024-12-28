package com.texteditor.ui;

import com.texteditor.editor.Rope;
import com.texteditor.editor.io.FileIO;
import com.texteditor.editor.io.FileType;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.io.File;

public class Page {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private final Label characterCount;

    Rope rope;

    TextField documentTitle;

    public Page(Rope rope, Stage primaryStage) {
        this.rope = rope;
        BorderPane root = new BorderPane();

        Image icon = new Image(getClass().getResource("/assets/icon.png").toExternalForm());
        primaryStage.getIcons().add(icon);

        MenuBar menuBar = createMenuBar(primaryStage);
        root.setTop(menuBar);

        VBox body = new VBox(10);
        body.setPadding(new Insets(20));
        body.setBackground(new Background(new BackgroundFill(Constants.BACKGROUND_COLOR, null, null)));

        Label titleLabel = new Label("TITLE");
        titleLabel.setStyle("-fx-font-family: 'Geist'; -fx-font-size: 10px; -fx-text-fill: #ffffff;");

        documentTitle = new TextField("File name");
        documentTitle.setStyle(
                "-fx-font-family: 'Geist'; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #ffffff !important; " +  // Ensure it takes priority
                        "-fx-border-width: 0; " +
                        "-fx-background-color: transparent;" +
                        "-fx-padding: 0;"
        );
        CustomTextArea textArea = new CustomTextArea(rope);

        BorderPane footer = new BorderPane();
        footer.setStyle("-fx-background-color: " + Constants.getCssColor(Constants.FOOTER_COLOR) + "; -fx-padding: 10px;");

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

        textArea.requestFocus();
    }

    private MenuBar createMenuBar(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: " + Constants.getCssColor(Constants.BACKGROUND_COLOR) + " !important ;");

        Menu fileMenu = new Menu("File");
        fileMenu.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 12px; -fx-text-fill: white;");

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(event -> showDirectoryChooser(primaryStage));

        fileMenu.getItems().add(saveItem);

        menuBar.getMenus().add(fileMenu);

        return menuBar;
    }

    private void showDirectoryChooser(Stage stage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            FileIO.saveFile(selectedDirectory.getAbsolutePath(), rope, FileType.TXT, documentTitle.getText());
        }
    }
}
