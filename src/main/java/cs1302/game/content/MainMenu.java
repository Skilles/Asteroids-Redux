package cs1302.game.content;

import cs1302.game.Game;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MainMenu extends VBox {

    private final Game game;

    public MainMenu(Game game) {
        this.game = game;
        init();
    }

    private void init() {
        setMinSize(Globals.WIDTH, Globals.HEIGHT);
        setPrefSize(Globals.WIDTH, Globals.HEIGHT);
        setAlignment(javafx.geometry.Pos.CENTER);
        setSpacing(10);

        int buttonWidth = Globals.WIDTH / 10;

        Button playButton = new Button("Play");
        playButton.setId("play-button");
        playButton.setOnAction(e -> game.play());
        playButton.setMinWidth(buttonWidth);

        Button instructionsButton = new Button("Instructions");
        instructionsButton.setId("instructions-button");
        instructionsButton.setOnAction(e -> instructions());
        instructionsButton.setMinWidth(buttonWidth);

        Button creditsButton = new Button("Credits");
        creditsButton.setId("credits-button");
        creditsButton.setOnAction(e -> credits());
        creditsButton.setMinWidth(buttonWidth);

        Button quitButton = new Button("Quit");
        quitButton.setId("quit-button");
        quitButton.setOnAction(e -> System.exit(0));
        quitButton.setMinWidth(buttonWidth);

        getChildren().addAll(getTitleText(game.getTitle(), 120), playButton, instructionsButton, creditsButton, quitButton);
    }

    private Text getTitleText(String text, int size) {
        Text titleText = new Text(text);
        titleText.setId("title");
        titleText.setFont(Font.loadFont("file:resources/fonts/MaldiniBold.ttf", size));
        titleText.setFill(Color.WHITE);
        return titleText;
    }

    public void main() {
        if (game.getChildren().size() > 1) {
            game.getChildren().remove(1);
        }
        game.getChildren().add(1, game.getMainMenu());
    }

    private void credits() {
        game.getChildren().remove(this);
        game.getChildren().add(1, new TextMenu("Credits", "CS 1302 Final Project\n"
                + "Fall 2021\n"
                + "By: Bilal Madi"));
    }

    private void instructions() {
        game.getChildren().remove(this);
        game.getChildren().add(1, new TextMenu("Instructions", "Instructions:\n" +
                "1. Use WASD to move the player.\n" +
                "2. Use the space bar to shoot.\n" +
                "3. Avoid the asteroids.\n" +
                "4. Collect the power ups.\n"));
    }

    class TextMenu extends VBox {
        TextMenu(String title, String text) {
            setMinSize(Globals.WIDTH, Globals.HEIGHT);
            setPrefSize(Globals.WIDTH, Globals.HEIGHT);
            setAlignment(javafx.geometry.Pos.CENTER);
            setSpacing(10);

            Label creditsLabel = new Label(text);
            creditsLabel.setId("text");
            creditsLabel.setTextFill(Color.WHITE);

            Button backButton = new Button("Back");
            backButton.setId("back-button");
            backButton.setOnAction(e -> main());

            getChildren().addAll(getTitleText(title, 70), creditsLabel, backButton);
        }
    }

}
