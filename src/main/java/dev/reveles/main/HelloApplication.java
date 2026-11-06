package dev.reveles.main;

import dev.reveles.view.EightOffView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application
{
    @Override
    public void start(Stage stage)
    {
        EightOffView view = new EightOffView();

        Scene scene = new Scene(view.getRoot(), 900, 700);

        stage.setTitle("Eight Off");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
}