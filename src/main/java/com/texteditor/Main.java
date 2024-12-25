package com.texteditor;

import com.texteditor.editor.Rope;
import com.texteditor.ui.Page;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Rope rope = new Rope("");
        new Page(rope, primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}