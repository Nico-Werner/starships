package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.BulletController;
import controller.ShipController;
import edu.austral.dissis.starships.file.ImageLoader;
import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.input.KeyCode;
import lombok.SneakyThrows;
import model.Ship;
import strategy.impl.SingleShooting;
import view.ShipView;

import java.io.File;
import java.io.IOException;

public class Config {
    private static final ImageLoader imageLoader = new ImageLoader();

    public static int PLAYERS;
    public static int LIVES;

    public static KeyCode[][] PLAYER_KEYS;

    public static String[] SHIP_NAMES;

    public static ShipController[] getPlayerShips() {
        try {
            return new ShipController[]{
                    new ShipController(new ShipView(imageLoader.loadFromResources(SHIP_NAMES[0], 100, 100), 200, 200), new Ship(200.0, new SingleShooting(), Vector2.vector(200, 200), 100), new BulletController()),
                    new ShipController(new ShipView(imageLoader.loadFromResources(SHIP_NAMES[1], 100, 100), 1000, 200), new Ship(200.0, new SingleShooting(), Vector2.vector(1000, 200), 100), new BulletController())
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setShip(int i, String shipName) {
        SHIP_NAMES[i] = shipName;
    }

    @SneakyThrows
    public static void reloadConfig() {
        ObjectMapper mapper = new ObjectMapper();
        ConfigJson configJson = mapper.readValue(new File("config.json"), ConfigJson.class);
        PLAYERS = configJson.PLAYERS;
        LIVES = configJson.LIVES;
        PLAYER_KEYS = configJson.PLAYER_KEYS;
        SHIP_NAMES = configJson.SHIP_NAMES;
    }
}
