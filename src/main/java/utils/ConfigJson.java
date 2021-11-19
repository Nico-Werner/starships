package utils;

import javafx.scene.input.KeyCode;
import lombok.Data;

@Data
public class ConfigJson {

    public int PLAYERS;
    public int LIVES;
    public KeyCode[][] PLAYER_KEYS;
    public String[] SHIP_NAMES;

    public ConfigJson() {}

}
