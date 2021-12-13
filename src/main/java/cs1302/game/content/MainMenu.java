package cs1302.game.content;

import cs1302.game.Game;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * The main menu of the game.
 */
public class MainMenu extends VBox {

    private final Game game;

    /**
     * Instantiates a new Main menu.
     *
     * @param game the game
     */
    public MainMenu(Game game) {
        this.game = game;
        init();
    }

    /**
     * Initialize.
     */
    private void init() {
        setMinSize(Globals.WIDTH, Globals.HEIGHT);
        setPrefSize(Globals.WIDTH, Globals.HEIGHT);
        setAlignment(javafx.geometry.Pos.CENTER);
        setSpacing(10);

        int buttonWidth = Globals.WIDTH / 10;

        Button playButton = new Button("Play");
        playButton.setOnAction(e -> game.play());
        playButton.setMinWidth(buttonWidth);

        Button instructionsButton = new Button("Controls");
        instructionsButton.setOnAction(e -> controls());
        instructionsButton.setMinWidth(buttonWidth);

        Button creditsButton = new Button("Credits");
        creditsButton.setOnAction(e -> credits());
        creditsButton.setMinWidth(buttonWidth);

        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> System.exit(0));
        quitButton.setMinWidth(buttonWidth);

        getChildren().addAll(getTitleText(game.getTitle(), 120), playButton,
                instructionsButton, creditsButton, quitButton);
    }

    /**
     * Returns {@code text} using the game's title font 'MaldiniBold' in the specified size.
     *
     * @param text the text
     * @param size the size of the text
     * @return the formatted title text
     */
    private Text getTitleText(String text, int size) {
        Text titleText = new Text(text);
        titleText.setFont(Font.loadFont("file:resources/fonts/MaldiniBold.ttf", size));
        titleText.setFill(Color.WHITE);
        return titleText;
    }

    /**
     * Switch to the main menu.
     */
    public void main() {
        if (game.getChildren().size() > 1) {
            game.getChildren().remove(1);
        }
        game.getChildren().add(1, game.getMainMenu());
    }

    /**
     * Switch to the credits menu.
     */
    private void credits() {
        game.getChildren().remove(this);
        game.getChildren().add(1, new TextMenu("Credits", "CS 1302 Final Project\n"
                + "Fall 2021\n"
                + "By: Bilal Madi"));
    }

    /**
     * Switch to the controls menu.
     */
    private void controls() {
        game.getChildren().remove(this);
        game.getChildren().add(1, new TextMenu("Controls",
                "W: Thrust Forwards\n" +
                "A: Turn Left\n" +
                "S: Thrust Backwards\n" +
                "D: Turn Right\n" +
                "Space: Shoot\n" +
                "Shift: Hyperspace\n" +
                "Escape: Pause"));
    }

    /**
     * An abstracted class for menus that display text.
     */
    class TextMenu extends VBox {

        /**
         * Instantiates a new Text menu.
         *
         * @param title the title
         * @param text  the text
         */
        TextMenu(String title, String text) {
            setMinSize(Globals.WIDTH, Globals.HEIGHT);
            setPrefSize(Globals.WIDTH, Globals.HEIGHT);
            setAlignment(javafx.geometry.Pos.CENTER);
            setSpacing(10);

            Label creditsLabel = new Label(text);
            creditsLabel.setTextFill(Color.WHITE);

            Button backButton = new Button("Back");
            backButton.setOnAction(e -> main());

            getChildren().addAll(getTitleText(title, 70), creditsLabel, backButton);
        }
    }

}
