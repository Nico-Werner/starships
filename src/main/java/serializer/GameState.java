package serializer;

import dto.AsteroidDTO;
import dto.PickupDTO;
import dto.PlayerDTO;
import lombok.Data;
import model.Asteroid;
import model.Pickup;
import player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class GameState implements Serializable {

    private final List<PlayerDTO> players;
    private final List<Asteroid> asteroids = new ArrayList<>();
    private final List<Pickup> pickups = new ArrayList<>();

    public GameState(List<Player> players, List<Asteroid> asteroids, List<Pickup> pickups) {
        this.players = players.stream().map(Player::toDTO).toList();
//        this.asteroids = asteroids.stream().map(Asteroid::toDTO);
//        this.pickups = pickups.stream().map(Pickup::toDTO);
    }
}
