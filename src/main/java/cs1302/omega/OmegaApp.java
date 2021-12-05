package cs1302.omega;

import cs1302.game.Asteroids;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class OmegaApp extends Application {

    /**
     * Constructs an {@code OmegaApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public OmegaApp() {}

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        Group root = new Group();

        // demo game provided with the starter code
        Asteroids game = new Asteroids(stage, 1280, 720);

        // setup scene
        // VBox root = new VBox(game);
        Scene scene = new Scene(root);
        root.getChildren().add(game);

        // setup stage
        stage.setTitle("Asteroids Reborn");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();

    } // start

} // OmegaApp
