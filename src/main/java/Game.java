import collider.MyCollider;
import controller.AsteroidController;
import controller.PickupController;
import dto.AsteroidDTO;
import dto.PickupDTO;
import dto.PlayerDTO;
import edu.austral.dissis.starships.collision.CollisionEngine;
import edu.austral.dissis.starships.file.ImageLoader;
import edu.austral.dissis.starships.game.*;
import factory.AsteroidFactory;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Setter;
import model.Asteroid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import player.Player;
import serializer.GameSerializer;
import serializer.GameState;
import ui.MenuBox;
import ui.MenuItem;
import utils.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
            return new GameManager(this, context).init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class GameManager {

    RootSetter rootSetter;
    GameContext context;
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
                        Config.PLAYER_KEYS[i][0],
                        Config.PLAYER_KEYS[i][1],
                        Config.PLAYER_KEYS[i][2],
                        Config.PLAYER_KEYS[i][3],
                        Config.PLAYER_KEYS[i][4]);
            }
            asteroidController = new AsteroidController();
            pickupController = new PickupController();
        } else {
            players = state.getPlayers().stream().map(PlayerDTO::toPlayer).toArray(Player[]::new);
            asteroidController = new AsteroidController(state.getAsteroids().stream().map(AsteroidDTO::toAsteroid).collect(Collectors.toList()));
            pane.getChildren().addAll(asteroidController.getViews());
            pickupController = new PickupController(state.getPickups().stream().map(PickupDTO::toPickup).collect(Collectors.toList()));
            pane.getChildren().addAll(pickupController.getViews());
        }

        for (Player player : players) {
            player.updateShipStyle(Config.SHIP_NAMES[player.getId()]);
            pane.getChildren().add(player.getShipController().getShipView().getImageView());
            pane.getChildren().add(player.getShipController().getShipView().getHealthView());
            pane.getChildren().add(player.getShipController().getShipView().getPoints());
            pane.getChildren().addAll(player.getShipController().getBulletController().renderBullets());
        }

        if(mainTimer == null) mainTimer = new MainTimer(players, context.getKeyTracker(), imageLoader, pane, asteroidController, pickupController);
        mainTimer.setPlayers(players);
        mainTimer.setKeyTracker(context.getKeyTracker());
        mainTimer.setImageLoader(imageLoader);
        mainTimer.setPane(pane);
        mainTimer.setAsteroidController(asteroidController);
        mainTimer.setPickupController(pickupController);

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
    Player[] players;
    AsteroidController asteroidController;
    PickupController pickupController;
    KeyTracker keyTracker;
    ImageLoader imageLoader;
    Pane pane;
    AsteroidFactory asteroidFactory = new AsteroidFactory();
    CollisionEngine collisionEngine = new CollisionEngine();

    boolean paused = false;

    public MainTimer(Player[] players, KeyTracker keyTracker, ImageLoader imageLoader, Pane pane, AsteroidController asteroidController, PickupController pickupController) {
        this.players = players;
        this.keyTracker = keyTracker;
        this.imageLoader = imageLoader;
        this.pane = pane;
        this.asteroidController = asteroidController;
        this.pickupController = pickupController;
    }

    @Override
    public void nextFrame(double secondsSinceLastFrame) {
        if(paused) {
            secondsSinceLastFrame = 0;
            paused = false;
        }
        pane.requestFocus();

        reviveDeadPlayers();
        updatePosition(secondsSinceLastFrame);
        collisionEngine.checkCollisions(getColliders());
        updateHealths();
        updateDeaths();
        spawnAsteroid();
        spawnPickup();

        if(Arrays.stream(players).allMatch(Player::isDead)) {
            stop();
            StackPane gameOverRectangle = createGameOverRectangle();
            gameOverRectangle.translateXProperty().setValue(pane.getWidth() / 2 - gameOverRectangle.getPrefWidth() / 2);
            gameOverRectangle.translateYProperty().setValue(pane.getHeight() / 2 - gameOverRectangle.getPrefHeight() / 2);
            pane.getChildren().add(gameOverRectangle);
        }
    }

    private void reviveDeadPlayers() {
        for (Player player : players) {
            if (player.getLives() > 0 && player.getShipController().getShip().getHealth() <= 0) {
                player.setShipController(Objects.requireNonNull(Config.getPlayerShips())[player.getId()]);
                pane.getChildren().add(player.getShipController().getShipView().getImageView());
                pane.getChildren().add(player.getShipController().getShipView().getHealthView());
                pane.getChildren().add(player.getShipController().getShipView().getPoints());
            }
        }
    }

    private StackPane createGameOverRectangle() {
        Rectangle rectangle = new Rectangle(600, 200);
        rectangle.setFill(Color.BLACK);
        rectangle.setOpacity(0.4);
        rectangle.setLayoutX(150);
        rectangle.setLayoutY(150);

        Text text = new Text("GAME OVER");
        text.setFont(Font.font(100));
        text.setFill(Color.WHITE);
        text.setX(150);
        text.setY(150);

        StackPane stackPane = new StackPane(rectangle, text);
        stackPane.setPrefSize(600, 200);

        return stackPane;
    }

    private void updateHealths() {
        for (Player player : players) {
            player.getShipController().updateHealth();
        }
    }

    private void spawnPickup() {
        if(Math.random() * 1000 < 3 && pickupController.getPickups().size() < 5) {
            ImageView imageView = pickupController.spawnPickup(imageLoader, pane.getWidth(), pane.getHeight());
            pane.getChildren().add(imageView);
        }
    }

    private void spawnAsteroid() {
        if(Math.random() * 100.0 < 5) {
            Asteroid asteroid = asteroidFactory.createAsteroid();
            ImageView imageView = asteroidController.spawnAsteroid(asteroid, imageLoader, pane.getWidth(), pane.getHeight());
            pane.getChildren().add(imageView);
        }
    }

    private void updatePosition(Double secondsSinceLastFrame) {
        for(Player player : players) {
            player.updateInput(pane, keyTracker, secondsSinceLastFrame);
            player.getShipController().getBulletController().updatePositions(secondsSinceLastFrame);
        }
        asteroidController.updatePositions(secondsSinceLastFrame);
    }

    private List<MyCollider> getColliders() {
        List<MyCollider> colliders = new ArrayList<>();
        for (Player player : players) {
            colliders.add(player.getShipController().getShip());
            colliders.addAll(player.getShipController().getBulletController().getBullets());
        }
        colliders.addAll(asteroidController.getAsteroids());
        colliders.addAll(pickupController.getPickups());
        return colliders;
    }

    private void updateDeaths() {
        for(Player player : players) {
            if(player.isShipDead()) {
                pane.getChildren().remove(player.updateDeath());
            }
            pane.getChildren().removeAll(player.getShipController().getBulletController().removeDeadBullets(pane.getWidth(), pane.getHeight()));
        }
        asteroidController.killOutOfBounds(pane.getWidth(), pane.getHeight());
        pane.getChildren().removeAll(asteroidController.updateDeaths());
        pane.getChildren().removeAll(pickupController.getInactive());
    }
}
