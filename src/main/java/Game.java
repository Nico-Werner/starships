import controller.AsteroidController;
import controller.GameController;
import controller.PickupController;
import dto.AsteroidDTO;
import dto.PickupDTO;
import dto.PlayerDTO;
import edu.austral.dissis.starships.file.ImageLoader;
import edu.austral.dissis.starships.game.*;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import player.Input;
import player.Player;
import serializer.GameSerializer;
import serializer.GameState;
import ui.MenuBox;
import ui.MenuItem;
import utils.Config;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Game extends GameApplication {

    @Override
    public @NotNull WindowSettings setupWindow() {
        return WindowSettings.fromTitle("Starships!").withSize(1920, 1080);
    }

    @Override
    public Parent initRoot(@NotNull GameContext context) {

        try {
            Config.reloadConfig();
            return new GameManager(this, context).init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class GameManager {

    final RootSetter rootSetter;
    final GameContext context;
    MainTimer mainTimer;

    public GameManager(RootSetter rootSetter, GameContext gameContext) {
        this.rootSetter = rootSetter;
        this.context = gameContext;
    }

    boolean isMenu = true;

    Parent init() throws IOException {

        return isMenu ? loadMenu(null) : loadGame(null);

    }

    private Parent loadMenu(@Nullable GameState gameState) throws IOException {
        Pane pane = new Pane();
        pane.setPrefSize(1920, 1080);

        ImageLoader imageLoader = new ImageLoader();
        Image image = imageLoader.loadFromResources("background.png", 100.0, 100.0);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(1920);
        imageView.setFitHeight(1080);

        pane.getChildren().add(imageView);

        MenuItem exit = new MenuItem("EXIT");
        exit.setOnMouseClicked(event -> System.exit(0));

        MenuItem loadSaved = new MenuItem("LOAD GAME");
        loadSaved.setOnMouseClicked(event -> {
            isMenu = !isMenu;
            try {
                if(gameState == null) {
                    rootSetter.setRoot(init());
                }
                rootSetter.setRoot(loadGame(GameSerializer.loadGame()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuItem resumeGame = new MenuItem("RESUME GAME");
        resumeGame.setOnMouseClicked(event -> {
            isMenu = !isMenu;
            try {
                if(gameState == null) {
                    rootSetter.setRoot(init());
                }
                rootSetter.setRoot(loadGame(gameState));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuItem save = new MenuItem("SAVE GAME");
        save.setOnMouseClicked(event -> GameSerializer.saveGame(gameState));

        MenuItem newGame = new MenuItem("NEW GAME");
        newGame.setOnMouseClicked(event -> {
            isMenu = !isMenu;
            try {
                rootSetter.setRoot(loadGame(null));
            }
            catch (IOException e) {
                e.printStackTrace();
            }});

        MenuItem change_ship = new MenuItem("CHANGE SHIP");
        change_ship.setOnMouseClicked(event -> {
            try {
                rootSetter.setRoot(loadShipSelect(0, gameState));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuBox menu = new MenuBox("STARSHIPS",
                resumeGame,
                loadSaved,
                newGame,
                save,
                change_ship,
                exit);

        pane.getChildren().add(menu);

        return pane;
    }

    private Parent loadGame(@Nullable GameState state) throws IOException {
        ImageLoader imageLoader = new ImageLoader();

        Pane pane = new Pane();

        BackgroundImage myBI= new BackgroundImage(imageLoader.loadFromResources("background.png", 1920, 1080),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        pane.setBackground(new Background(myBI));

        Player[] players;
        AsteroidController asteroidController;
        PickupController pickupController;

        if(state == null) {
            players = new Player[Config.PLAYERS];
            for (int i = 0; i < Config.PLAYERS; i++) {
                players[i] = new Player(i, 0, Config.LIVES, Objects.requireNonNull(Config.getPlayerShips())[i],
                        new Input(
                                Config.PLAYER_KEYS[i][0],
                                Config.PLAYER_KEYS[i][1],
                                Config.PLAYER_KEYS[i][2],
                                Config.PLAYER_KEYS[i][3],
                                Config.PLAYER_KEYS[i][4]
                        ));
            }
            asteroidController = new AsteroidController();
            pickupController = new PickupController();
        } else {
            players = state.getPlayers().stream().map(PlayerDTO::toPlayer).toArray(Player[]::new);
            asteroidController = new AsteroidController(state.getAsteroids().stream().map(AsteroidDTO::toAsteroid).collect(Collectors.toList()));
            pickupController = new PickupController(state.getPickups().stream().map(PickupDTO::toPickup).collect(Collectors.toList()));
        }

        for (int i = 0, playersLength = players.length; i < playersLength; i++) {
            Player player = players[i];
            player.updateShipStyle(Config.SHIP_NAMES[player.getId()]);

            Group healthView = player.getShipController().getShipView().getHealthView();
            healthView.setLayoutX(10 + 120 * i);
            healthView.setLayoutY(10);
            pane.getChildren().add(healthView);

            Text points = player.getShipController().getShipView().getPoints();
            points.setLayoutX(10 + 120 * i);
            points.setLayoutY(30);
            pane.getChildren().add(points);
        }

        if (mainTimer == null)
            mainTimer = new MainTimer(players, context.getKeyTracker(), pane, asteroidController, pickupController);
        mainTimer.loadController(players, context.getKeyTracker(), imageLoader, pane, asteroidController, pickupController);

        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.P) {
                isMenu = !isMenu;
                try {
                    mainTimer.stop();
                    mainTimer.setPaused(true);
                    rootSetter.setRoot(loadMenu(new GameState(List.of(players), asteroidController.getAsteroids(), pickupController.getPickups())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        mainTimer.start();
        return pane;
    }

    private Parent loadShipSelect(int i, GameState gameState) throws IOException {
        ImageLoader imageLoader = new ImageLoader();

        Pane pane = new Pane();

        BackgroundImage myBI= new BackgroundImage(imageLoader.loadFromResources("background.png", 1920, 1080),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        pane.setBackground(new Background(myBI));

        MenuItem blue = new MenuItem("BLUE");
        blue.setOnMouseClicked(event -> Config.setShip(i, "starship.gif"));

        MenuItem green = new MenuItem("GREEN");
        green.setOnMouseClicked(event -> Config.setShip(i, "green-ship.png"));

        MenuItem next = new MenuItem("NEXT PLAYER");
        next.setOnMouseClicked(event -> {
            try {
                if(i == Config.PLAYERS - 1) rootSetter.setRoot(loadShipSelect(0, gameState));
                else rootSetter.setRoot(loadShipSelect(i + 1, gameState));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuItem back = new MenuItem("BACK TO MENU");
        back.setOnMouseClicked(event -> {
            try {
                rootSetter.setRoot(loadMenu(gameState));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuBox menu = new MenuBox("PLAYER: " + i, blue, green, next, back);

        pane.getChildren().add(menu);

        return pane;
    }

}

@Setter
class MainTimer extends GameTimer {

    GameController gameController;

    boolean paused = false;

    public MainTimer(Player[] players, KeyTracker keyTracker, Pane pane, AsteroidController asteroidController, PickupController pickupController) {
        gameController = new GameController(players, keyTracker, pane, asteroidController, pickupController);
    }

    @Override
    public void nextFrame(double secondsSinceLastFrame) {
        if(paused) {
            secondsSinceLastFrame = 0;
            paused = false;
        }

        if(gameController.isOver()) {
            stop();
            gameController.gameOver();
        }

        gameController.update(secondsSinceLastFrame);
    }

    public void loadController(Player[] players, KeyTracker keyTracker, ImageLoader imageLoader, Pane pane, AsteroidController asteroidController, PickupController pickupController) {
        gameController = new GameController(players, keyTracker, pane, asteroidController, pickupController);
    }
}
