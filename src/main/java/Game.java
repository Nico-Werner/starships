import collider.MyCollider;
import controller.AsteroidController;
import controller.PickupController;
import dto.AsteroidDTO;
import dto.PlayerDTO;
import edu.austral.dissis.starships.collision.CollisionEngine;
import edu.austral.dissis.starships.file.ImageLoader;
import edu.austral.dissis.starships.game.*;
import factory.AsteroidFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import lombok.Setter;
import model.Asteroid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import player.Player;
import serializer.GameSerializer;
import serializer.GameState;
import utils.Config;

import java.io.IOException;
import java.util.ArrayList;
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

    MenuBox menu;

    public GameManager(RootSetter rootSetter, GameContext gameContext) {
        this.rootSetter = rootSetter;
        this.context = gameContext;
    }

    boolean isIntro = true;

    Parent init() throws IOException {

        return isIntro ? loadIntro(null) : loadGame(null);
    }

    private Parent loadIntro(@Nullable GameState gameState) throws IOException {
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

        MenuItem resumeGame = new MenuItem("RESUME GAME");
        resumeGame.setOnMouseClicked(event -> {
            isIntro = !isIntro;
            try {
                if(gameState == null) {
                    rootSetter.setRoot(init());
                }
                rootSetter.setRoot(loadGame(GameSerializer.loadGame()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuItem save = new MenuItem("SAVE GAME");
        save.setOnMouseClicked(event -> GameSerializer.saveGame(gameState));

        MenuItem newGame = new MenuItem("NEW GAME");
        newGame.setOnMouseClicked(event -> {
            isIntro = !isIntro;
            try {
                rootSetter.setRoot(loadGame(null));
            }
            catch (IOException e) {
                e.printStackTrace();
            }});

        menu = new MenuBox("STARSHIPS",
                resumeGame,
                newGame,
                save,
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

        if(state == null) {
            Player[] players = new Player[Config.PLAYERS];
            for (int i = 0; i < Config.PLAYERS; i++) {

                players[i] = new Player(i, 0, Config.LIVES, Objects.requireNonNull(Config.getPlayerShips())[i],
                        Config.PLAYER_KEYS[i][0],
                        Config.PLAYER_KEYS[i][1],
                        Config.PLAYER_KEYS[i][2],
                        Config.PLAYER_KEYS[i][3],
                        Config.PLAYER_KEYS[i][4]);

                pane.getChildren().add(players[i].getShipController().getShipView().getImageView());
                pane.getChildren().add(players[i].getShipController().getShipView().getHealthView());
                pane.getChildren().add(players[i].getShipController().getShipView().getPoints());
            }

            AsteroidController asteroidController = new AsteroidController();
            PickupController pickupController = new PickupController();
            if(mainTimer == null) mainTimer = new MainTimer(players, context.getKeyTracker(), imageLoader, pane, asteroidController, pickupController);
            mainTimer.setPlayers(players);
            mainTimer.setKeyTracker(context.getKeyTracker());
            mainTimer.setImageLoader(imageLoader);
            mainTimer.setPane(pane);
            mainTimer.setAsteroidController(asteroidController);
            mainTimer.setPickupController(pickupController);

            pane.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.P) {
                    isIntro = !isIntro;
                    try {
                        mainTimer.stop();
                        rootSetter.setRoot(loadIntro(new GameState(List.of(players), asteroidController.getAsteroids(), pickupController.getPickups())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
        } else {
            List<Player> players = state.getPlayers().stream().map(PlayerDTO::toPlayer).toList();
            for (Player player : players) {
                pane.getChildren().add(player.getShipController().getShipView().getImageView());
                pane.getChildren().add(player.getShipController().getShipView().getHealthView());
                pane.getChildren().add(player.getShipController().getShipView().getPoints());

                //TODO bullets keep moving when paused
                pane.getChildren().addAll(player.getShipController().getBulletController().renderBullets());
            }

            AsteroidController asteroidController = new AsteroidController(state.getAsteroids().stream().map(AsteroidDTO::toAsteroid).collect(Collectors.toList()));
            pane.getChildren().addAll(asteroidController.getViews());
            PickupController pickupController = new PickupController();
            if(mainTimer == null) mainTimer = new MainTimer(players.toArray(Player[]::new), context.getKeyTracker(), imageLoader, pane, asteroidController, pickupController);
            mainTimer.setPlayers(players.toArray(Player[]::new));
            mainTimer.setKeyTracker(context.getKeyTracker());
            mainTimer.setImageLoader(imageLoader);
            mainTimer.setPane(pane);
            mainTimer.setAsteroidController(asteroidController);
            mainTimer.setPickupController(pickupController);

            pane.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.P) {
                    isIntro = !isIntro;
                    try {
                        mainTimer.stop();
                        rootSetter.setRoot(loadIntro(new GameState(players, asteroidController.getAsteroids(), pickupController.getPickups())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
        mainTimer.start();
        return pane;
    }

    private static class MenuBox extends StackPane {
        public MenuBox(String title, MenuItem... items) {
            Rectangle bg = new Rectangle(300, 1080);
            bg.setOpacity(0.2);

            DropShadow shadow = new DropShadow(7, 5, 0, Color.BLACK);
            shadow.setSpread(0.8);

            bg.setEffect(shadow);

            Text text = new Text(title + " ");
            text.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
            text.setFill(Color.WHITE);

            Line hSep = new Line();
            hSep.setEndX(250);
            hSep.setStroke(Color.DARKRED);
            hSep.setOpacity(0.4);

            Line vSep = new Line();
            vSep.setStartX(300);
            vSep.setEndX(300);
            vSep.setEndY(1080);
            vSep.setStroke(Color.DARKRED);
            vSep.setOpacity(0.4);

            VBox vBox = new VBox();
            vBox.setAlignment(Pos.TOP_RIGHT);
            vBox.setPadding(new Insets(60, 0, 0, 0));
            vBox.getChildren().addAll(text, hSep);
            vBox.getChildren().addAll(items);

            setAlignment(Pos.TOP_RIGHT);
            getChildren().addAll(bg, vSep, vBox);
        }
    }

    private static class MenuItem extends StackPane {
        public MenuItem(String name) {
            Rectangle bg = new Rectangle(300, 24);

            LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.BLACK),
                    new Stop(0.2, Color.DARKGRAY));

            bg.setFill(gradient);
            bg.setVisible(false);
            bg.setEffect(new DropShadow(5, 0, 5, Color.BLACK));

            Text text = new Text(name + "    ");
            text.setFill(Color.LIGHTGRAY);
            text.setFont(Font.font(20));

            setAlignment(Pos.CENTER_RIGHT);
            getChildren().addAll(bg, text);

            setOnMouseEntered(event -> {
                bg.setVisible(true);
                text.setFill(Color.WHITE);
            });

            setOnMouseExited(event -> {
                bg.setVisible(false);
                text.setFill(Color.LIGHTGRAY);
            });

            setOnMousePressed(event -> {
                bg.setFill(Color.WHITE);
                text.setFill(Color.BLACK);
            });

            setOnMouseReleased(event -> {
                bg.setFill(gradient);
                text.setFill(Color.WHITE);
            });
        }
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
        pane.requestFocus();
        updatePosition(secondsSinceLastFrame);
        updateHealths();
        updateDeaths();
        spawnAsteroid();
        spawnPickup();
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

        List<MyCollider> colliders = new ArrayList<>();
        colliders.addAll(asteroidController.getAsteroids());
        colliders.addAll(pickupController.getPickups());
        for (Player player : players) {
            colliders.add(player.getShipController().getShip());
            colliders.addAll(player.getShipController().getBulletController().getBullets());
        }
        collisionEngine.checkCollisions(colliders);
    }

    private void updateDeaths() {
        for(Player player : players) {
            pane.getChildren().remove(player.getShipController().updateDeath());
            pane.getChildren().removeAll(player.getShipController().getBulletController().removeDeadBullets(pane.getWidth(), pane.getHeight()));
        }
        asteroidController.killOutOfBounds(pane.getWidth(), pane.getHeight());
        pane.getChildren().removeAll(asteroidController.updateDeaths());
        pane.getChildren().removeAll(pickupController.getInactive());
    }
}
